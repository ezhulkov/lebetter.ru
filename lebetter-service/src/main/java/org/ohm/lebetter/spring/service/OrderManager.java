package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.model.impl.entities.OrderToValueEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;

import java.util.List;

public interface OrderManager
        extends GenericManager<OrderEntity, UserEntity> {

    public OrderToProductEntity getOrderToProductLink(Long id);

    public OrderToProductEntity setValuesToOrderLink(OrderToProductEntity link, List<PropertyValueEntity> values);

    public OrderToProductEntity addOrderToProductLink(Long pid, OrderEntity order, UserEntity caller);

    public void deleteOrderToProductLink(OrderToProductEntity link);

    public OrderEntity getCurrentOrder(UserEntity caller, boolean createIfNeeded);

    public List<OrderToProductEntity> getProducts(OrderEntity order);

    public List<OrderEntity> getDoneOrders(UserEntity caller);

    public List<OrderEntity> getDoneOrders();

    public float getOrderTotal(OrderEntity order);

    public float getOrderTotal(OrderEntity order, int discount);

    public List<OrderToValueEntity> getOrderValues(OrderEntity order);

}
