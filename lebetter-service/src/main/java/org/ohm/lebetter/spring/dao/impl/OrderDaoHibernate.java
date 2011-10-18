package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.OrderDao;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;
import org.springframework.dao.support.DataAccessUtils;

import java.math.BigInteger;

public class OrderDaoHibernate
        extends CreatorAwareDaoHibernate<OrderEntity, UserEntity>
        implements OrderDao {

    public OrderDaoHibernate() {
        super(OrderEntity.class);
    }

    @Override
    public Long getNextOrderNumber() {
        String sql = "select nextval('order_sequence') ";
        BigInteger bint = (BigInteger) DataAccessUtils.uniqueResult(getSession().createSQLQuery(sql).list());
        return Long.valueOf(bint.longValue());
    }

}
