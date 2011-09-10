package org.ohm.lebetter.spring.service;


import org.apache.tapestry5.services.ContextPathEncoder;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyToCategoryEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.ObjectCreatorAwareManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

public interface CategoryManager
        extends GenericManager<CategoryEntity, UserEntity>,
                ObjectCreatorAwareManager<CategoryEntity, UserEntity> {

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

    public static final Comparator<PropertyValueEntity> VALUE_BY_INT_CODE_ENTITY_COMPARATOR =
            new Comparator<PropertyValueEntity>() {
                public int compare(PropertyValueEntity e1, PropertyValueEntity e2) {
                    return e1 == null || e2 == null || e1.getCode() == null || e2.getCode() == null ?
                           0 : Integer.decode(e1.getCode()).compareTo(Integer.decode(e2.getCode()));
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

    public static final Comparator<TagToValueEntity> TAG_ENTITY_BY_INT_CODE_COMPARATOR =
            new Comparator<TagToValueEntity>() {
                public int compare(TagToValueEntity e1, TagToValueEntity e2) {
                    return e1 == null || e2 == null ||
                           e1.getPropertyValue() == null || e2.getPropertyValue() == null ||
                           e1.getPropertyValue().getCode() == null ||
                           e2.getPropertyValue().getCode() == null ?
                           0 : Integer.decode(e1.getPropertyValue().getCode()).
                            compareTo(Integer.decode(e2.getPropertyValue().getCode()));
                }
            };

    public List<CategoryEntity> getAllRootCategories();

    public List<CategoryEntity> getAllReadyCategories(CategoryEntity parent);

    public List<CategoryEntity> getAllReadyCategoriesForUI(CategoryEntity parent,
                                                           boolean girls,
                                                           boolean boys);

    public List<CategoryEntity> getAllReadyLeafCategories();

    public List<CategoryEntity> getAllReadyLeafCategoriesDistinct();

    public List<CategoryEntity> getAllBrandCategories();

    public List<CategoryEntity> getAllCategories(CategoryEntity parent);

    public long getAllCategoriesCount(CategoryEntity parent);

    public List<PropertyEntity> getPropertiesByMultiple(CategoryEntity pType, boolean multiple);

    public List<PropertyEntity> getAllReadyPropertiesByType(CategoryEntity pType,
                                                            PropertyEntity.Type type);

    public List<PropertyEntity> getAllReadyProperties(CategoryEntity pType);

    public List<PropertyEntity> getAllProperties(CategoryEntity pType);

    public List<PropertyEntity> getAllPropertiesForUI(ProductEntity product);

    public List<PropertyEntity> getAllReadyPropertiesForSearch(CategoryEntity pType, String exclude);

    public List<CategoryEntity> getAllReadyCategoriesTreeForObjectSearch();

    @Transactional
    public void createLink(PropertyToCategoryEntity link);

    @Transactional
    public void setNewSearchChain(CategoryEntity ptype, List<Long> ids, List<Long> order, UserEntity caller);

    @Transactional
    public void clearProperties(CategoryEntity ptype, UserEntity caller);

    public String flattenProperties(ProductEntity product);

    public String flattenProperties(PropertyValueEntity[][] values);

    public String flattenProperties(String[][] values);

    public String flattenProperties(List<String> values);

    public String createFilterContextURL(String productId,
                                         String categoryId,
                                         List<String> values,
                                         ContextPathEncoder encoder);


    public List<Long> getAllSubCategoryIdsByName(String name);

    public List<CategoryEntity> getAllSubCategoriesByName(String name);

    public List<CategoryEntity> getRelativeCategories(CategoryEntity category);

    public CategoryEntity getTopCategoryByName(String name);

    public List<CategoryEntity> getAllForProduct(ProductEntity product);

    public boolean hasCategories(ProductEntity productEntity);
}