package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.impl.BaseCreatorAwareEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class OrderEntity extends BaseCreatorAwareEntity {

    public enum OrderStatus {NEW, SUBMITTED, REJECTED, DONE}

    @Enumerated(EnumType.STRING)
    public OrderStatus orderStatus = OrderStatus.NEW;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private DealerEntity dealer;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "app_order_prod",
               joinColumns = {@JoinColumn(name = "order_id")},
               inverseJoinColumns = {@JoinColumn(name = "prod_id")})
    private List<ProductEntity> products = new ArrayList<ProductEntity>();

    public OrderEntity() {
        setEntityCode("Order");
    }

    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
