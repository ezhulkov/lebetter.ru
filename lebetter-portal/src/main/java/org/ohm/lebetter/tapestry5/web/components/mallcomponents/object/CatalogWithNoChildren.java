package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.spring.service.Constants;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.ObjectBaseEntity.Status;

import java.util.List;

@Import(library = {"proxy:/scripts/swfobject.js"})
public class CatalogWithNoChildren extends AbstractBaseComponent {

    @Property
    private ProductEntity oneProduct;

    @Property
    @Parameter(required = true, allowNull = false)
    private CategoryEntity selectedCategory;

    @Cached
    public List<ProductEntity> getProducts() {
        java.util.List<Long> ids = getServiceFacade().getProductManager().getIdsByCategory(selectedCategory,
                                                                                           Status.READY);
        return getServiceFacade().getProductManager().getAll(ids);
    }

    public String[] getProductContext() {
        CategoryEntity cat = null;
        oneProduct = getServiceFacade().getProductManager().get(oneProduct.getId());
        if (oneProduct.getCategories() != null && oneProduct.getCategories().size() != 0) {
            cat = oneProduct.getCategories().get(0);
        }
        return new String[]{cat == null ? "" : cat.getAltId(), oneProduct.getAltId()};
    }

    public ProductPhotoEntity getProductPhoto() {
        return getServiceFacade().getProductPhotoManager().getMainPhoto(oneProduct);
    }

    public String getImageUrl() {
        ProductPhotoEntity photo = getProductPhoto();
        return photo == null ?
               null :
               getServiceFacade().getDataManager().getDataFullURL(photo, Constants.FileNames.SMALL_PHOTO);
    }

}