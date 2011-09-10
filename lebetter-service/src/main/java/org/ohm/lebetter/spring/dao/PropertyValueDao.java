package org.ohm.lebetter.spring.dao;

import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.dao.GenericDao;
import org.room13.mallcore.spring.dao.ObjectCreatorAwareDao;

import java.util.Collection;
import java.util.List;

public interface PropertyValueDao
        extends GenericDao<PropertyValueEntity, UserEntity>,
                ObjectCreatorAwareDao {

    public PropertyValueEntity getValueByName(String name, PropertyEntity type);

    public PropertyValueEntity getValueByAdditionalDictInfo2(String name, PropertyEntity type);

    public PropertyValueEntity getValueByCode(String code);

    public List<PropertyValueEntity> getValuesByObject(ObjectBaseEntity object,
                                                       PropertyEntity category);

    public List<PropertyValueEntity> getValuesByObject(ObjectBaseEntity object);

    public List<TagToValueEntity> getListTagsForObject(ObjectBaseEntity object);

    public List<TagToValueEntity> getTagsForObject(ObjectBaseEntity object);

    public List<TagToValueEntity> getTagsForObjectAndCategory(ObjectBaseEntity object,
                                                              PropertyEntity category);

    public List<TagToValueEntity> getTagsForObjectAndGroupCategory(ObjectBaseEntity object,
                                                                   PropertyEntity category);

    public List<TagToValueEntity> getTagsForObjectByMultiple(ObjectBaseEntity object,
                                                             PropertyEntity property,
                                                             boolean multiple);

    public boolean existsByName(String name, PropertyEntity type);

    public void addSelectableProperties(Collection<PropertyValueEntity> vals, Long prodId);
}