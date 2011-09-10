package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.ObjectCreatorAwareManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface PropertyManager
        extends GenericManager<PropertyEntity, UserEntity>,
        ObjectCreatorAwareManager<PropertyEntity, UserEntity> {

    @Transactional
    public void clearTagsForProduct(ProductEntity product, UserEntity caller);

    @Transactional
    public void setManuallyTagsToProduct(Collection<TagToValueEntity> valueLinks,
                                         ProductEntity product,
                                         UserEntity caller);

    @Transactional
    public void addTagsToProductFast(Collection<TagToValueEntity> valueLinks,
                                     ProductEntity product,
                                     UserEntity caller);

    public List<PropertyEntity> getAllRootProperties();

    public List<PropertyEntity> getAllSubProperties(PropertyEntity parent);

    public List<PropertyEntity> getAllReadyRootProperties();

    public List<TagToValueEntity> getListTagsForProduct(ProductEntity product);

    public List<TagToValueEntity> getTagsForProduct(ProductEntity product);

    public List<TagToValueEntity> getTagsForProduct(ProductEntity product, PropertyEntity property);

    public List<TagToValueEntity> getTagsForProductByMultiple(ProductEntity product, boolean multiple);

    public List<TagToValueEntity> getTagsForProductByMultiple(ProductEntity product,
                                                              PropertyEntity property,
                                                              boolean multiple);

    public List<PropertyValueEntity> getAllValues(PropertyEntity property);

    public List<PropertyValueEntity> getAllReadyValues(PropertyEntity property);

    public List<Long> getAllProductValueIdsBySeveralValues(CategoryEntity category,
                                                           List<PropertyValueEntity> vals);

    public List<PropertyValueEntity> getAllTaggedValuesForCategoryStrict(PropertyEntity property,
                                                                         CategoryEntity category,
                                                                         List<PropertyValueEntity> vals);

    public List<PropertyValueEntity> getValuesByType(ProductEntity product, PropertyEntity property);

    public List<ProductEntity> getTaggedProductsByValue(PropertyValueEntity value);

    public List<Long> getTaggedProductIdsByValue(PropertyValueEntity value);

    public PropertyEntity getPropertyByCode(String code);

    public void removeTagsForProduct(ProductEntity product, PropertyEntity property);

    public List<PropertyEntity> getPropertiesByCode(String code);

    public List<PropertyEntity> getAllMandatoryFields(ProductEntity product);

    public int getAllMandatoryFieldsCount(ProductEntity product);

    public List<PropertyEntity> getSetMandatoryFields(ProductEntity product);

    public int getSetMandatoryFieldsCount(ProductEntity product);

    @Transactional
    public void removeTagsForProduct(ProductEntity product, String tag);

}