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

public interface PropertyDao
        extends GenericDao<PropertyEntity, UserEntity>,
                ObjectCreatorAwareDao {

    public PropertyEntity getTypeByName(String name);

    public PropertyEntity getTypeByCode(String name);

    public List<PropertyEntity> getAllRootCategories();

    public List<PropertyEntity> getAllSubCategories(PropertyEntity parent);

    public List<PropertyEntity> getAllReadyRootCategories();

    public List<PropertyValueEntity> getAllValues(PropertyEntity category);

    public List<PropertyValueEntity> getAllReadyValues(PropertyEntity category);

    public List<PropertyValueEntity> getValuesByType(ObjectBaseEntity object, PropertyEntity category);

    public boolean existsByName(String name, PropertyEntity type);

    public void addTagsToProductFast(Collection<TagToValueEntity> valueLinks, Long prodId, Long userId);

    public void removeTagsForProduct(Long productId, String tag);
}