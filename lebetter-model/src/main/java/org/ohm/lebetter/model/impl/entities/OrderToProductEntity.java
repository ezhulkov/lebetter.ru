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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_order_prod")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class OrderToProductEntity
        extends BaseCreatorAwareEntity {

    @JoinColumn(name = "prod_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private ProductEntity product;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private OrderEntity order;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "product")
    private List<OrderToValueEntity> values = new ArrayList<OrderToValueEntity>();

    public OrderToProductEntity() {
        setEntityCode("OrderToProduct");
    }

    public List<OrderToValueEntity> getValues() {
        return values;
    }

    public void setValues(List<OrderToValueEntity> values) {
        this.values = values;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }
}
