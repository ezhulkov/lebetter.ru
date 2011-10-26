package org.ohm.lebetter.spring.service;


import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyToCategoryEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.ImageAwareManager;
import org.room13.mallcore.spring.service.ObjectCreatorAwareManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

public interface CategoryManager
        extends GenericManager<CategoryEntity, UserEntity>,
                ObjectCreatorAwareManager<CategoryEntity, UserEntity>,
                ImageAwareManager<CategoryEntity, UserEntity>,
                SitemapAwareManager<CategoryEntity> {

    public static final Comparator<PropertyEntity> PROPERTY_ENTITY_COMPARATOR =
            new Comparator<PropertyEntity>() {
                public int compare(PropertyEntity e1, PropertyEntity e2) {
                    return e1 == null || e2 == null || e1.getName() == null || e2.getName() == null ?
                           0 : e1.getName().compareTo(e2.getName());
                }
            };

    public static final Comparator<PropertyValueEntity> VALUE_ENTITY_COMPARATOR =
            new Comparator<PropertyValueEntity>() {
                public int compare(PropertyValueEntity e1, PropertyValueEntity e2) {
                    return e1 == null || e2 == null || e1.getName() == null || e2.getName() == null ?
                           0 : e1.getName().compareTo(e2.getName());
                }
            };

    public static final Comparator<TagToValueEntity> TAG_ENTITY_COMPARATOR =
            new Comparator<TagToValueEntity>() {
                public int compare(TagToValueEntity e1, TagToValueEntity e2) {
                    return e1 == null || e2 == null ?
                           0 : e1.getPropertyValue().getProperty().getName().
                            compareTo(e2.getPropertyValue().getProperty().getName());
                }
            };

    public List<CategoryEntity> getAllRootCategories();

    public List<CategoryEntity> getAllReadyCategories(CategoryEntity parent);

    public List<CategoryEntity> getAllReadyCategoriesForUI(boolean showHidden);

    public List<CategoryEntity> getAllReadyLeafCategories();

    public List<CategoryEntity> getAllReadyLeafCategoriesDistinct();

    public List<CategoryEntity> getAllCategories(CategoryEntity parent);

    public long getAllCategoriesCount(CategoryEntity parent);

    public List<PropertyEntity> getPropertiesByMultiple(CategoryEntity pType, boolean multiple);

    public List<PropertyEntity> getAllReadyPropertiesByType(CategoryEntity pType,
                                                            PropertyEntity.Type type);

    public List<PropertyEntity> getAllReadyProperties(CategoryEntity pType);

    public List<PropertyEntity> getAllProperties(CategoryEntity pType);

    @Transactional
    public void createLink(PropertyToCategoryEntity link);

    @Transactional
    public void setNewSearchChain(CategoryEntity ptype, List<Long> ids, List<Long> order, UserEntity caller);

    @Transactional
    public void clearProperties(CategoryEntity ptype, UserEntity caller);

    public List<Long> getAllSubCategoryIdsByName(String name);

    public List<CategoryEntity> getAllSubCategoriesByName(String name);

    public CategoryEntity getTopCategoryByName(String name);

    public List<CategoryEntity> getAllForProduct(ProductEntity product);

    public boolean hasCategories(ProductEntity productEntity);

    public List<PropertyEntity> getAllPropertiesForUI(ProductEntity product);

    public CategoryEntity getByCode(String code);

    public List<CategoryEntity> getCategoriesToMainPage();

}