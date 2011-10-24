package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.spring.service.DataManager;
import org.room13.mallcore.spring.service.DataManager.FileNames;

public class ShowProduct extends AbstractBaseComponent {

    @Property
    @Parameter(required = true, allowNull = false)
    private CategoryEntity selectedCategory;

    @Property
    @Parameter(required = true, allowNull = false)
    private ProductEntity selectedProduct;

    @Property
    private PropertyValueEntity oneValue;

    @Property
    private PropertyEntity oneProperty;

    @Property
    private ProductPhotoEntity onePhoto;

    @Cached
    public java.util.List<PropertyEntity> getProperties() {
        return getServiceFacade().getCategoryManager().getAllPropertiesForUI(selectedProduct);
    }

    public java.util.List<PropertyValueEntity> getValues() {
        return oneProperty.getValues();
    }

    public java.util.List<ProductPhotoEntity> getPhotos() {
        return getServiceFacade().getProductPhotoManager().getAllByProduct(selectedProduct);
    }

    public String getSmallImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(onePhoto, DataManager.FileNames.SMALL_PHOTO);
    }

    public String getMediumImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(onePhoto, DataManager.FileNames.MEDIUM_PHOTO);
    }

    public String getBigImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(onePhoto, DataManager.FileNames.BIG_PHOTO);
    }

    public String getOriginalImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(onePhoto, FileNames.ORIGINAL_FILE) + ".jpg";
    }

    public String getoneValPicURL() {
        return getServiceFacade().getDataManager().getDataFullURL(oneValue, FileNames.SMALL_AVATAR_FILE);
    }

}