package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.impl.BaseCreatorAwareEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "app_order_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class OrderToValueEntity
        extends BaseCreatorAwareEntity {

    @JoinColumn(name = "prod_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private OrderToProductEntity product;

    @JoinColumn(name = "value_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private PropertyValueEntity value;

    public OrderToValueEntity() {
        setEntityCode("OrderToValueEntity");
    }

    public OrderToProductEntity getProduct() {
        return product;
    }

    public void setProduct(OrderToProductEntity product) {
        this.product = product;
    }

    public PropertyValueEntity getValue() {
        return value;
    }

    public void setValue(PropertyValueEntity value) {
        this.value = value;
    }
}
