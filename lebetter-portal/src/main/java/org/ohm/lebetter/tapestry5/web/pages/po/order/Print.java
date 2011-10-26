package org.ohm.lebetter.tapestry5.web.pages.po.order;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.model.impl.entities.OrderToValueEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.spring.service.Constants.FileNames;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;
import org.room13.mallcore.spring.service.DataManager;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Print extends AbstractBasePage {

    @Property
    private OrderEntity selectedOrder;

    @Property
    private OrderToValueEntity oneOrderValue;

    @Property
    private OrderToProductEntity oneProduct;

    @Property
    private PropertyEntity oneProperty;

    @Property
    private PropertyValueEntity oneValue;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getOrderManager());
        selectedOrder = (OrderEntity) selectedObject.findSelectedObject();
    }

    public Long onPassivate() {
        return selectedOrder == null ? null : selectedOrder.getRootId();
    }

    public String getImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(selectedOrder.getDealer(),
                                                                  DataManager.FileNames.BIG_AVATAR_FILE);
    }

    @Cached
    public java.util.List<OrderToProductEntity> getProducts() {
        return getServiceFacade().getOrderManager().getProducts(selectedOrder);
    }

    public java.util.List<PropertyEntity> getProperties() {
        java.util.List<PropertyEntity> result = new LinkedList<PropertyEntity>();
        java.util.List<TagToValueEntity> tags =
                getServiceFacade().getPropertyManager().getTagsForProductByMultiple(oneProduct.getProduct(),
                                                                                    true);
        for (TagToValueEntity tag : tags) {
            if (!result.contains(tag.getPropertyValue().getProperty())) {
                result.add(tag.getPropertyValue().getProperty());
            }
        }
        return result;
    }

    public java.util.List<PropertyValueEntity> getValues() {
        java.util.List<PropertyValueEntity> result = new LinkedList<PropertyValueEntity>();
        java.util.List<TagToValueEntity> tags =
                getServiceFacade().getPropertyManager().getTagsForProduct(oneProduct.getProduct(),
                                                                          oneProperty);
        for (TagToValueEntity tag : tags) {
            result.add(tag.getPropertyValue());
        }
        return result;
    }

    public java.util.List<OrderToValueEntity> getOrderValues() {
        return getServiceFacade().getOrderManager().getOrderValues(selectedOrder);
    }

    public float getOrderTotalSum() {
        return getServiceFacade().getOrderManager().getOrderTotal(selectedOrder);
    }

    public float getOrderClientDiscountSum() {
        return getServiceFacade().getOrderManager().getOrderTotal(selectedOrder,
                                                                  selectedOrder.getClientDiscount());
    }

    public ProductPhotoEntity getProductPhoto() {
        return getServiceFacade().getProductPhotoManager().getMainPhoto(oneProduct.getProduct());
    }

    public String getProductImageUrl() {
        ProductPhotoEntity photo = getProductPhoto();
        return photo == null ?
               null :
               getServiceFacade().getDataManager().getDataFullURL(photo, FileNames.SMALL_PHOTO.toString());
    }

}