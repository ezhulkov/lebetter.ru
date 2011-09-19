package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.impl.entities.RoleEntity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.LinkedList;

@Entity(name = "org.ohm.lebetter.model.impl.entities.UserEntity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DiscriminatorValue("LB")
@AccessType("field")
public class UserEntity extends org.room13.mallcore.model.impl.entities.UserEntity {

    public UserEntity() {
        setEntityCode("User");
    }

    public UserEntity(String login, String password) {
        super(login, password);
    }

    public RoleEntity getRole() {
        return (super.getRoles() == null || super.getRoles().size() == 0) ? null : super.getRoles().get(0);
    }

    public void setRole(RoleEntity role) {
        if (super.getRoles() == null) {
            super.setRoles(new LinkedList<RoleEntity>());
        }
        super.getRoles().add(role);
    }
}