package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.OrderToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.OrderToValueDao;
import org.room13.mallcore.spring.dao.impl.GenericDaoHibernate;

public class OrderToValueDaoHibernate
        extends GenericDaoHibernate<OrderToValueEntity, UserEntity>
        implements OrderToValueDao {

    public OrderToValueDaoHibernate() {
        super(OrderToValueEntity.class);
    }

}
