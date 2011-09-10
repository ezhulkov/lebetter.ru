package org.ohm.lebetter.spring.dao;


import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.GenericDao;
import org.room13.mallcore.spring.dao.ObjectCreatorAwareDao;

import java.util.List;

public interface CategoryDao
        extends GenericDao<CategoryEntity, UserEntity>,
                ObjectCreatorAwareDao {

    public boolean exists(CategoryEntity pType);

    public List<CategoryEntity> getAllRootCategories();

    public List<CategoryEntity> getAllReadyLeafCategories();

    public List<CategoryEntity> getAllReadyRootCategories();

    public List<CategoryEntity> getAllReadyCategories(CategoryEntity parent);

    public long getAllRootCategoriesCount();

    public long getAllCategoriesCount(CategoryEntity parent);

    public List<CategoryEntity> getAllReadyCategories();

    public List<CategoryEntity> getAllCategories(CategoryEntity parent);

    public List<PropertyEntity> getPropertiesByMultiple(CategoryEntity category, boolean multiple);

    public List<PropertyEntity> getAllReadyPropertiesByType(CategoryEntity pType,
                                                            PropertyEntity.Type type);

    public List<PropertyEntity> getAllProperties(CategoryEntity pType);

    public List<PropertyEntity> getAllReadyProperties(CategoryEntity pType);

    public List<PropertyEntity> getAllReadyPropertiesForSearch(CategoryEntity pType, String exclude);

    public boolean hasCategories(Long prodId);
}