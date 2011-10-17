package org.ohm.lebetter.spring.service.impl;


import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.PropertyDao;
import org.ohm.lebetter.spring.dao.PropertyValueDao;
import org.ohm.lebetter.spring.service.PropertyValueManager;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class PropertyValueManagerImpl
        extends LBGenericManagerImpl<PropertyValueEntity, UserEntity>
        implements PropertyValueManager {

    private PropertyValueDao propertyValueDao;
    private PropertyDao propertyDao;

    public PropertyValueManagerImpl() {
        super(PropertyValueEntity.class);
    }

    @Required
    public void setPropertyDao(PropertyDao propertyDao) {
        this.propertyDao = propertyDao;
    }

    @Required
    public void setPropertyValueDao(PropertyValueDao propertyValueDao) {
        super.setGenericDao(propertyValueDao);
        this.propertyValueDao = propertyValueDao;
    }

    public ServiceManager getServiceManagerExt() {
        return (ServiceManager) super.getServiceManager();
    }


    @Override
    @Transactional
    public void createValueForInsiders(PropertyValueEntity propertyValue,
                                       PropertyEntity property,
                                       UserEntity caller) {

        //Check unique
        if (!propertyValueDao.existsByName(propertyValue.getName(), property)) {

            //Init property value
            propertyValue.setCreator(caller);
            if (propertyValue.isRootObject()) {
                propertyValue.setProperty(property);
            }

            //Create property value
            propertyValueDao.create(propertyValue);

            //Get persistent property type
            PropertyEntity propertyPersist =
                    getServiceManagerExt().getPropertyManager().get(property.getId());

            //Set persist properties
            if (propertyValue.isRootObject()) {
                propertyPersist.getValues().add(propertyValue);
                propertyValue.setProperty(propertyPersist);
            }

        } else {
            throw new ObjectExistsException("Value exists exception");
        }
    }

    @Override
    @Transactional
    public void create(PropertyValueEntity propertyValue,
                       PropertyValueEntity parentCategoryValue,
                       UserEntity caller) {

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Creating property value for insiders...");
        }

        propertyValue.setObjectStatus(ObjectBaseEntity.Status.READY);
        createValueForInsiders(propertyValue, propertyValue.getProperty(), caller);

    }

    @Override
    @Transactional
    public void save(PropertyValueEntity propertyValue, UserEntity caller) throws ObjectExistsException {

        //Get persist object
        PropertyValueEntity propertyValuePersist = propertyValueDao.get(propertyValue.getId());

        //Check unique
        boolean unique = true;
        if (!propertyValuePersist.getName().equals(propertyValue.getName())) {
            unique = !propertyValueDao.existsByName(propertyValue.getName(),
                                                    getRoot(propertyValue.getId()).getProperty());
        }

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Unique: " + unique);
        }

        if (unique) {

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Set properties to persist object" +
                                    "(propertyValue, propertyValuePersist): " + "(" +
                                    propertyValue + ", " + propertyValuePersist + ")", caller);
            }

            saveValueForInsiders(propertyValue, propertyValuePersist.getProperty(), caller);

        } else {
            throw new ObjectExistsException("Value exists exception");
        }

    }

    @Override
    @Transactional
    public void saveValueForInsiders(PropertyValueEntity propertyValue,
                                     PropertyEntity property,
                                     UserEntity caller) {
        PropertyValueEntity propertyValuePersist = propertyValueDao.get(propertyValue.getId());
        BeanUtils.copyProperties(propertyValue, propertyValuePersist);
        propertyValueDao.save(propertyValue);
    }

    @Override
    @Transactional
    public void removeValueForInsiders(PropertyValueEntity propertyValue,
                                       PropertyEntity property,
                                       UserEntity caller) {
        PropertyEntity propertyPersist =
                getServiceManagerExt().getPropertyManager().get(property.getId());
        PropertyValueEntity propertyValuePersist = propertyValueDao.get(propertyValue.getId());
        List<PropertyValueEntity> propertyValuePersistAll =
                propertyValueDao.getTranslatedObjects(propertyValuePersist);
        for (PropertyValueEntity propertyValueEntity : propertyValuePersistAll) {
            propertyPersist.getValues().remove(propertyValueEntity);
        }
        //Remove value object
        propertyValueDao.remove(propertyValuePersist);
    }

    @Override
    public PropertyValueEntity getValueByName(PropertyEntity property, String name) {
        return propertyValueDao.getValueByName(name, property);
    }

    @Override
    public PropertyValueEntity getValueByAdditionalDictInfo2(PropertyEntity property, String name) {
        return propertyValueDao.getValueByAdditionalDictInfo2(name, property);
    }

    @Override
    public List<PropertyValueEntity> getValuesByObject(ObjectBaseEntity object, PropertyEntity property) {
        if (property == null) {
            return propertyValueDao.getValuesByObject(object);
        } else {
            return propertyValueDao.getValuesByObject(object, property);
        }
    }

    @Override
    public PropertyValueEntity getValueByCode(String propertyCode) {
        return propertyValueDao.getValueByCode(propertyCode);
    }

    @Override
    public PropertyValueEntity getValueByCode(ObjectBaseEntity object, String propertyCode) {
        PropertyEntity property = propertyDao.getTypeByCode(propertyCode);
        if (property == null) {
            return null;
        }
        List<TagToValueEntity> values = propertyValueDao.getTagsForObjectAndCategory(object, property);
        return values.size() == 0 ? null : values.get(0).getPropertyValue();
    }

    @Override
    @Transactional
    public void remove(PropertyValueEntity propertyValue, UserEntity caller) {
        removeValueForInsiders(propertyValue, propertyValue.getProperty(), caller);
    }

}