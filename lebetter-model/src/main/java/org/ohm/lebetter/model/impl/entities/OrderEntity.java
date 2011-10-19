package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.impl.BaseCreatorAwareEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
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

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "order")
    private List<OrderToProductEntity> products = new ArrayList<OrderToProductEntity>();

    @Column
    private Date placedDate;

    @Column
    private String orderNumber;

    @Column
    private String comments;

    public OrderEntity() {
        setEntityCode("Order");
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public DealerEntity getDealer() {
        return dealer;
    }

    public void setDealer(DealerEntity dealer) {
        this.dealer = dealer;
    }

    public List<OrderToProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<OrderToProductEntity> products) {
        this.products = products;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getPlacedDate() {
        return placedDate;
    }

    public void setPlacedDate(Date placedDate) {
        this.placedDate = placedDate;
    }

    @Transient
    public String getPlacedDatePrintable() {
        return getPlacedDate() == null ? "" :
               DF.get().format(getPlacedDate());
    }

}
