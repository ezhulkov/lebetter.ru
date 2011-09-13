package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.UserManager;

import java.util.Comparator;

public interface UserKinderManager
        extends UserManager<UserEntity, UserEntity> {

    public static final Comparator<UserEntity> USER_BY_LASTNAME_SORT =
            new Comparator<UserEntity>() {
                public int compare(UserEntity e1, UserEntity e2) {
                    if (e1.getLastName() == null || e2.getLastName() == null) {
                        return 0;
                    }
                    return e1.getLastName().compareTo(e2.getLastName());
                }
            };



}
