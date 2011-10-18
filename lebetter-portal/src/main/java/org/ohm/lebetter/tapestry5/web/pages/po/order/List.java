package org.ohm.lebetter.tapestry5.web.pages.po.order;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class List extends AdminBasePage {

    @Property
    private OrderToProductEntity oneProduct;

    @Property
    private OrderEntity oneOrder;

    @Property
    private PropertyEntity oneProperty;

    @Property
    private PropertyValueEntity oneValue;

    @Cached
    public OrderEntity getCurrentOrder() {
        return getServiceFacade().getOrderManager().getCurrentOrder(getAuth().getUser(), false);

    }

    @Cached
    public java.util.List<OrderToProductEntity> getProducts() {
        return getServiceFacade().getOrderManager().getProducts(getCurrentOrder());
    }

    @Cached
    public java.util.List<OrderEntity> getOrders() {
        return getServiceFacade().getOrderManager().getDoneOrders(getAuth().getUser());
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

    public Object[] getOneProductContext() {
        return new Object[]{oneProduct.getProduct().getCategories().get(0).getAltId(),
                            oneProduct.getProduct().getAltId()};
    }

}
