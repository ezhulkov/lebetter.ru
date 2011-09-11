package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity(name = "org.room13.kinder.model.impl.entities.UserEntity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DiscriminatorValue("LB")
@AccessType("field")
public class UserEntity extends org.room13.mallcore.model.impl.entities.UserEntity {

    public UserEntity() {
    }

    public UserEntity(String login, String password) {
        super(login, password);
    }

}