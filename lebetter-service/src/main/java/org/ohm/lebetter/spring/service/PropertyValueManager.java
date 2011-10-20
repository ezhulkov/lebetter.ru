package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.ImageAwareManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PropertyValueManager
        extends GenericManager<PropertyValueEntity, UserEntity>,
                ImageAwareManager<PropertyValueEntity, UserEntity> {

    @Transactional
    public void createValueForInsiders(PropertyValueEntity propertyValue,
                                       PropertyEntity property,
                                       UserEntity caller);

    @Transactional
    public void removeValueForInsiders(PropertyValueEntity propertyValue,
                                       PropertyEntity property,
                                       UserEntity caller);

    @Transactional
    public void saveValueForInsiders(PropertyValueEntity categoryValue,
                                     PropertyEntity property,
                                     UserEntity caller);

    public PropertyValueEntity getValueByName(PropertyEntity property, String name);

    public List<PropertyValueEntity> getValuesByObject(ObjectBaseEntity object, PropertyEntity category);

    public PropertyValueEntity getValueByCode(ObjectBaseEntity object, String propertyCode);

    public PropertyValueEntity getValueByAdditionalDictInfo2(PropertyEntity object,
                                                             String propertyCode);

    public PropertyValueEntity getValueByCode(String propertyCode);

}