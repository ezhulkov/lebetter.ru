package org.ohm.lebetter.spring.dao;

import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.GenericDao;
import org.room13.mallcore.spring.dao.ObjectCreatorAwareDao;


public interface OrderDao
        extends GenericDao<OrderEntity, UserEntity>, ObjectCreatorAwareDao {

}
