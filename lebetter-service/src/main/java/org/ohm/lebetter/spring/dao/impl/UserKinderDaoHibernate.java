package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.UserKinderDao;
import org.room13.mallcore.spring.dao.impl.UserDaoHibernate;

public class UserKinderDaoHibernate
        extends UserDaoHibernate<UserEntity, UserEntity>
        implements UserKinderDao<UserEntity, UserEntity> {

    public UserKinderDaoHibernate() {
        super(UserEntity.class);
    }

}