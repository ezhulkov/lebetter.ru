package org.ohm.lebetter.spring.dao;

import org.room13.mallcore.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.UserDao;

public interface UserKinderDao<T extends UserEntity, TU extends UserEntity>
        extends UserDao<T, TU> {

}
