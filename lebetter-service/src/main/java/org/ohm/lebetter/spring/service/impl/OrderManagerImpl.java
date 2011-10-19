package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderEntity.OrderStatus;
import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.model.impl.entities.OrderToValueEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.OrderDao;
import org.ohm.lebetter.spring.dao.OrderToProductDao;
import org.ohm.lebetter.spring.dao.OrderToValueDao;
import org.ohm.lebetter.spring.service.OrderManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.ObjectBaseEntity.Status;
import org.room13.mallcore.spring.dao.OwnerDao;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@SuppressWarnings("unchecked")
public class OrderManagerImpl
        extends LBGenericManagerImpl<OrderEntity, UserEntity>
        implements OrderManager {

    public static final RMLogger log = new RMLogger(OrderManagerImpl.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("MMdd");

    protected OrderDao orderDao;
    protected OrderToProductDao orderToProductDao;
    protected OrderToValueDao orderToValueDao;
    protected OwnerDao ownerDao;

    public OrderManagerImpl() {
        super(OrderEntity.class);
    }

    @Required
    public void setOrderToValueDao(OrderToValueDao orderToValueDao) {
        this.orderToValueDao = orderToValueDao;
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
    public OrderToProductEntity setValuesToOrderLink(OrderToProductEntity link, List<PropertyValueEntity> values) {
        DetachedCriteria criteria = DetachedCriteria.forClass(OrderToValueEntity.class).add(Restrictions.eq("product", link));
        List<OrderToValueEntity> oldVals = orderToValueDao.findByCriteria(criteria, -1, -1);
        orderToValueDao.getHibernateTemplate().deleteAll(oldVals);
        for (PropertyValueEntity value : values) {
            OrderToValueEntity orderValue = new OrderToValueEntity();
            orderValue.setProduct(link);
            orderValue.setValue(value);
            orderValue.setRootObject(true);
            orderValue.setObjectDeleted(false);
            orderToValueDao.create(orderValue);
        }
        return orderToProductDao.get(link.getId());
    }

    @Override
    public OrderToProductEntity getOrderToProductLink(Long id) {
        return orderToProductDao.get(id);
    }

    @Override
    @Transactional
    public OrderToProductEntity addOrderToProductLink(Long pid, OrderEntity order, UserEntity caller) {
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
    public void deleteOrderToProductLink(OrderToProductEntity link) {
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

    @Override
    @Transactional
    public void create(OrderEntity object, OrderEntity parent, UserEntity caller) throws ObjectExistsException {
        super.create(object, parent, caller);
        Long on = orderDao.getNextOrderNumber();
        object.setPlacedDate(new Date(System.currentTimeMillis()));
        synchronized (SDF) {
            object.setOrderNumber(SDF.format(object.getPlacedDate()) + "-" + on);
        }
        orderDao.save(object);
    }

    @Override
    public List<OrderToValueEntity> getOrderValues(OrderEntity order) {
        DetachedCriteria criteria = DetachedCriteria.forClass(OrderToValueEntity.class);
        criteria.createAlias("product", "p", CriteriaSpecification.INNER_JOIN);
        criteria.createAlias("p.order", "o", CriteriaSpecification.INNER_JOIN);
        criteria.add(Restrictions.eq("o.rootId", order.getRootId()));
        return orderToValueDao.findRootByCriteria(criteria, -1, -1);
    }

    @Override
    public float getOrderTotal(OrderEntity order) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductEntity.class).
                createAlias("orders", "o2p", CriteriaSpecification.INNER_JOIN).
                createAlias("o2p.order", "o", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.eq("o.rootId", order.getRootId())).
                setProjection(Projections.sum("price"));
        Object result = orderDao.findByCriteria(criteria, -1, -1).get(0);
        return result == null ? 0 : ((Float) result).floatValue();
    }

    @Override
    public float getOrderTotal(OrderEntity order, int discount) {
        float res = getOrderTotal(order);
        return Math.round(res - ((res / 100) * discount));
    }
}
