package org.ohm.lebetter.tapestry5.web.pages.po.order;

import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

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

    public java.util.List<OrderEntity> getOrders() {
        return getServiceFacade().getOrderManager().getDoneOrders(getAuth().getUser());
    }

    public OrderEntity getCurrentOrder() {
        return getServiceFacade().getOrderManager().getCurrentOrder(getAuth().getUser(), false);
    }

    public java.util.List<OrderToProductEntity> getProducts() {
        return getServiceFacade().getOrderManager().getProducts(oneOrder);
    }

    public Object[] getOneProductContext() {
        return new Object[]{oneProduct.getProduct().getCategories().get(0).getAltId(),
                            oneProduct.getProduct().getAltId()};
    }

    public float getOrderTotalSum() {
        return getServiceFacade().getOrderManager().getOrderTotal(oneOrder);
    }

    public float getOrderTotalDiscSum() {
        if (oneOrder.getDealer() == null) {
            return getServiceFacade().getOrderManager().getOrderTotal(oneOrder, 0);
        } else {
            return getServiceFacade().getOrderManager().getOrderTotal(oneOrder,
                                                                      oneOrder.getDealer().getDiscount());
        }
    }

}
