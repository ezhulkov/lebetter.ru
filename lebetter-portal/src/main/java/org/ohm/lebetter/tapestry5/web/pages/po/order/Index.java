package org.ohm.lebetter.tapestry5.web.pages.po.order;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderEntity.OrderStatus;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Index extends AdminBasePage {

    @Property
    private OrderEntity selectedOrder;

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

    public boolean isSelectedOrderNew() {
        return selectedOrder.getOrderStatus().equals(OrderStatus.NEW);
    }

    public float getOrderTotalDiscountSum() {
        return getServiceFacade().getOrderManager().getOrderTotal(selectedOrder,
                                                                  selectedOrder.getDealer().getDiscount());
    }

    public float getOrderTotalSum() {
        return getServiceFacade().getOrderManager().getOrderTotal(selectedOrder);
    }

}