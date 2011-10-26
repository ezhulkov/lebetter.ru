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
import org.ohm.lebetter.spring.service.Constants.FileNames;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;
import org.room13.mallcore.spring.service.DataManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Print extends AbstractBasePage {

    private static final transient ThreadLocal<DateFormat> DF = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("dd/MM/yyyy");
        }
    };

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
        return getServiceFacade().getCategoryManager().getAllPropertiesForUI(oneProduct.getProduct());
    }

    public String getOnePropertyValue() {
        PropertyValueEntity value = null;
        if (oneProperty.isMultiple()) {
            OrderToValueEntity link = getServiceFacade().getOrderManager().getOrderValue(oneProduct, oneProperty);
            if (link != null) {
                value = link.getValue();
            }
        } else {
            if (oneProperty.getValues().size() > 0) {
                value = oneProperty.getValues().get(0);
            }
        }
        return value == null ? "-" : value.getName();
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

    public String getDatePrintable() {
        return DF.get().format(new Date(System.currentTimeMillis()));
    }

}