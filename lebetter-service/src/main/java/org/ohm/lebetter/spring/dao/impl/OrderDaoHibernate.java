package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.OrderDao;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;

public class OrderDaoHibernate
        extends CreatorAwareDaoHibernate<OrderEntity, UserEntity>
        implements OrderDao {

    public OrderDaoHibernate() {
        super(OrderEntity.class);
    }

}
