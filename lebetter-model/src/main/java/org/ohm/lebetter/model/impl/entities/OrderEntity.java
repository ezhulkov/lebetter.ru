package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.impl.BaseCreatorAwareEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "app_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class OrderEntity
        extends BaseCreatorAwareEntity {

    public OrderEntity() {
        setEntityCode("Order");
    }

}
