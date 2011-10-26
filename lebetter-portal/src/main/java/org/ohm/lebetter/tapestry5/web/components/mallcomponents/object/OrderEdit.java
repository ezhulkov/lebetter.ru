package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderEntity.OrderStatus;
import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.model.impl.entities.OrderToValueEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.service.Constants.FileNames;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.components.base.EditObjectCallback;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericSelectModel;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrderEdit extends AbstractEditComponent {

    @Component(id = "dealer", parameters = {"model=dealerModel",
                                            "encoder=dealerModel",
                                            "value=selectedObject.dealer",
                                            "validate=required"})
    private Select dealerField;

    @Component(id = "orderStatus", parameters = {"value=selectedObject.orderStatus", "validate=required"})
    private Select statusField;

    @Component(id = "comments", parameters = {"value=selectedObject.comments", "validate=maxlength=512"})
    private TextArea descField;

    @Component(id = "clientDiscount", parameters = {"value=selectedObject.clientDiscount", "validate=maxlength=64"})
    private TextField clientDiscountField;

    @Property
    private ValueEncoder<DealerEntity> dealerModel = null;

    @Property
    private OrderToValueEntity oneOrderValue;

    void onPrepare() throws Exception {
        if (dealerModel == null) {
            UserEntity creator =
                    getServiceFacade().getUserManager().get(getSelectedObject().getCreator().getId());
            List<DealerEntity> dealers = getServiceFacade().getDealerManager().getObjectsOwnedBy(creator);
            dealerModel = new GenericSelectModel<DealerEntity>(dealers, dealers,
                                                               DealerEntity.class, "name", "rootId",
                                                               getIOC().getPropertyAccess());
        }
    }

    public OrderEntity getSelectedObject() {
        return (OrderEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(OrderEntity object) {
        setSelectedObjectInternal(object);
    }

    public EditObjectCallback getCallback() {
        return new EditObjectCallback<OrderEntity>() {
            @Override
            public boolean onFormSubmit(OrderEntity object) throws Exception {
                //Submit order
                if (object.getOrderStatus().equals(OrderStatus.NEW)) {
                    object.setOrderStatus(OrderStatus.SUBMITTED);
                    object.setPlacedDate(new Date(System.currentTimeMillis()));
                }
                return true;
            }

            @Override
            public boolean onPostFormSubmit(OrderEntity object) throws Exception {
                //Set order values
                List<String> names = getIOC().getRequest().getParameterNames();
                Map<OrderToProductEntity, List<PropertyValueEntity>> orderValueSet =
                        new HashMap<OrderToProductEntity, List<PropertyValueEntity>>();
                for (String name : names) {
                    if (name.startsWith("LB-")) {
                        String val = getIOC().getRequest().getParameter(name);
                        String parts[] = name.split("-");
                        OrderToProductEntity link = getServiceFacade().getOrderManager().getOrderToProductLink(Long.parseLong(parts[1]));
                        PropertyValueEntity value = getServiceFacade().getPropertyValueManager().get(Long.parseLong(val));
                        List<PropertyValueEntity> l = orderValueSet.get(link);
                        if (l == null) {
                            l = new LinkedList<PropertyValueEntity>();
                            orderValueSet.put(link, l);
                        }
                        l.add(value);
                    }
                }
                for (OrderToProductEntity orderToProduct : orderValueSet.keySet()) {
                    List<PropertyValueEntity> values = orderValueSet.get(orderToProduct);
                    getServiceFacade().getOrderManager().setValuesToOrderLink(orderToProduct, values);
                }
                return true;
            }
        };
    }

    @Property
    private OrderToProductEntity oneProduct;

    @Property
    private OrderEntity oneOrder;

    @Property
    private PropertyEntity oneProperty;

    @Property
    private PropertyValueEntity oneValue;

    @Cached
    public List<OrderToProductEntity> getProducts() {
        return getServiceFacade().getOrderManager().getProducts(getSelectedObject());
    }

    public List<PropertyEntity> getProperties() {
        List<PropertyEntity> result = new LinkedList<PropertyEntity>();
        List<TagToValueEntity> tags =
                getServiceFacade().getPropertyManager().getTagsForProductByMultiple(oneProduct.getProduct(),
                                                                                    true);
        for (TagToValueEntity tag : tags) {
            if (!result.contains(tag.getPropertyValue().getProperty())) {
                result.add(tag.getPropertyValue().getProperty());
            }
        }
        return result;
    }

    public List<PropertyValueEntity> getValues() {
        List<PropertyValueEntity> result = new LinkedList<PropertyValueEntity>();
        List<TagToValueEntity> tags =
                getServiceFacade().getPropertyManager().getTagsForProduct(oneProduct.getProduct(),
                                                                          oneProperty);
        for (TagToValueEntity tag : tags) {
            result.add(tag.getPropertyValue());
        }
        return result;
    }

    public Object[] getOneProductContext() {
        return new Object[]{oneProduct.getProduct().getCategories().get(0).getAltId(),
                            oneProduct.getProduct().getAltId()};
    }

    public List<OrderToValueEntity> getOrderValues() {
        return getServiceFacade().getOrderManager().getOrderValues(getSelectedObject());
    }

    public Block onActionFromDelProduct(Long pid) {
        OrderToProductEntity link = getServiceFacade().getOrderManager().getOrderToProductLink(pid);
        getServiceFacade().getOrderManager().deleteOrderToProductLink(link);
        return super.getEditObject().getEditAreaBlock();
    }

    public boolean isSelectedOrderNew() {
        return getSelectedObject().getOrderStatus().equals(OrderStatus.NEW);
    }

    public float getOrderTotalDiscountSum() {
        return getServiceFacade().getOrderManager().getOrderTotal(getSelectedObject(),
                                                                  getSelectedObject().getDealer() == null ?
                                                                  0 : getSelectedObject().getDealer().getDiscount());
    }

    public float getOrderClientDiscountSum() {
        return getServiceFacade().getOrderManager().getOrderTotal(getSelectedObject(),
                                                                  getSelectedObject().getClientDiscount());
    }

    public float getOrderTotalSum() {
        return getServiceFacade().getOrderManager().getOrderTotal(getSelectedObject());
    }

    public ProductPhotoEntity getProductPhoto() {
        return getServiceFacade().getProductPhotoManager().getMainPhoto(oneProduct.getProduct());
    }

    public String getImageUrl() {
        ProductPhotoEntity photo = getProductPhoto();
        return photo == null ?
               null :
               getServiceFacade().getDataManager().getDataFullURL(photo, FileNames.SMALL_PHOTO.toString());
    }

    public String getSelectedObjectOrderStatus() {
        return getBase().getText(getSelectedObject().getOrderStatus().toString());
    }

}