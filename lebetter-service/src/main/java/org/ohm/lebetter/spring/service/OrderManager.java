package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;

public interface OrderManager
        extends GenericManager<OrderEntity, UserEntity> {

}
