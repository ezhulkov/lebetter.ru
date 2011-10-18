package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderEntity.OrderStatus;
import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.OrderDao;
import org.ohm.lebetter.spring.dao.OrderToProductDao;
import org.ohm.lebetter.spring.service.OrderManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.ObjectBaseEntity.Status;
import org.room13.mallcore.spring.dao.OwnerDao;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


@SuppressWarnings("unchecked")
public class OrderManagerImpl
        extends LBGenericManagerImpl<OrderEntity, UserEntity>
        implements OrderManager {

    public static final RMLogger log = new RMLogger(OrderManagerImpl.class);

    protected OrderDao orderDao;
    protected OrderToProductDao orderToProductDao;
    protected OwnerDao ownerDao;

    public OrderManagerImpl() {
        super(OrderEntity.class);
    }

    @Required
    public void setOrderToProductDao(OrderToProductDao orderToProductDao) {
        this.orderToProductDao = orderToProductDao;
    }

    @Required
    public void setOwnerDao(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    @Required
    public void setOrderDao(OrderDao OrderDao) {
        this.orderDao = OrderDao;
        this.genericDao = OrderDao;
    }

    @Override
    @Transactional
    public OrderEntity getCurrentOrder(UserEntity caller, boolean createIfNeeded) {
        DetachedCriteria criteria = DetachedCriteria.forClass(OrderEntity.class).
                add(Restrictions.eq("creator", caller)).
                add(Restrictions.eq("orderStatus", OrderStatus.NEW));
        List<OrderEntity> orders = orderDao.findRootByCriteria(criteria, -1, -1);
        if (orders.size() > 0) {
            return orders.get(0);
        } else if (createIfNeeded) {
            OrderEntity order = getNewInstance();
            order.setCreator(caller);
            order.setOrderStatus(OrderStatus.NEW);
            create(order, null, caller);
            return order;
        }
        return null;
    }

    @Override
    @Transactional
    public OrderToProductEntity addProduct(Long pid, OrderEntity order, UserEntity caller) {
        if (order == null) {
            throw new RuntimeException("Order must not be null");
        }
        ProductEntity product = getServiceManager().getProductManager().get(pid);
        OrderToProductEntity link = new OrderToProductEntity();
        link.setProduct(product);
        link.setOrder(order);
        link.setCreator(caller);
        link.setRootObject(true);
        link.setObjectStatus(Status.READY);
        orderToProductDao.create(link);
        return link;
    }

    @Override
    @Transactional
    public void removeProduct(OrderToProductEntity link) {
        orderToProductDao.remove(link);
    }

    @Override
    public List<OrderEntity> getDoneOrders(UserEntity caller) {
        DetachedCriteria criteria = DetachedCriteria.forClass(OrderEntity.class).
                add(Restrictions.eq("creator", caller)).
                add(Restrictions.ne("orderStatus", OrderStatus.NEW));
        return orderDao.findRootByCriteria(criteria, -1, -1);
    }

    @Override
    public List<OrderToProductEntity> getProducts(OrderEntity order) {
        if (order == null) {
            return Collections.emptyList();
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(OrderToProductEntity.class).
                createAlias("order", "o", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.eq("o.rootId", order.getRootId()));
        return orderDao.findByCriteria(criteria, -1, -1);
    }

}
