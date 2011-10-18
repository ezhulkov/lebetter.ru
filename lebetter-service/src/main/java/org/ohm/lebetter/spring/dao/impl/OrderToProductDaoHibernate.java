package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.OrderToProductDao;
import org.room13.mallcore.spring.dao.impl.GenericDaoHibernate;

public class OrderToProductDaoHibernate
        extends GenericDaoHibernate<OrderToProductEntity, UserEntity>
        implements OrderToProductDao {

    public OrderToProductDaoHibernate() {
        super(OrderToProductEntity.class);
    }

}
