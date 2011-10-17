package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.OrderDao;
import org.ohm.lebetter.spring.service.OrderManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.spring.dao.OwnerDao;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;


@SuppressWarnings("unchecked")
public class OrderManagerImpl
        extends LBGenericManagerImpl<OrderEntity, UserEntity>
        implements OrderManager {

    public static final RMLogger log = new RMLogger(OrderManagerImpl.class);

    protected OrderDao OrderDao;
    protected OwnerDao ownerDao;

    public OrderManagerImpl() {
        super(OrderEntity.class);
    }

    @Required
    public void setOwnerDao(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    @Required
    public void setOrderDao(OrderDao OrderDao) {
        this.OrderDao = OrderDao;
        this.genericDao = OrderDao;
    }

    @Override
    public void addProduct(Long pid, UserEntity caller) {

    }

    @Override
    public void removeProduct(Long pid, UserEntity caller) {

    }

    @Override
    public List<ProductEntity> getProducts(UserEntity caller) {
        return null;
    }
}
