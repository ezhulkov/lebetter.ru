package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;

import java.util.List;

public interface OrderManager
        extends GenericManager<OrderEntity, UserEntity> {

    public void addProduct(Long pid, OrderEntity order, UserEntity caller);

    public void removeProduct(Long pid, OrderEntity order, UserEntity caller);

    public OrderEntity getCurrentOrder(UserEntity caller, boolean createIfNeeded);

    public List<ProductEntity> getProducts(OrderEntity order);

}
