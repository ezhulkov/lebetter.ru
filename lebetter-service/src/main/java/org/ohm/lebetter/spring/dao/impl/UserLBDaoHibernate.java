package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.UserLBDao;
import org.room13.mallcore.spring.dao.impl.UserDaoHibernate;

public class UserLBDaoHibernate
        extends UserDaoHibernate<UserEntity, UserEntity>
        implements UserLBDao<UserEntity, UserEntity> {

    public UserLBDaoHibernate() {
        super(UserEntity.class);
    }

}