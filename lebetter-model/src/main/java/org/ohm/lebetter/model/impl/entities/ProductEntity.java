package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ohm.lebetter.model.DescriptionAware;
import org.ohm.lebetter.model.SitemapAware;
import org.room13.mallcore.model.impl.BaseCreatorRepAwareEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class ProductEntity
        extends BaseCreatorRepAwareEntity
        implements DescriptionAware, SitemapAware {

    public enum StockStatus {
        ONSTOCK, MISSING
    }

    @Column
    private String altId;

    @Column
    private String name;

    @Column
    private Float price;

    @Column
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private StockStatus stockStatus = StockStatus.ONSTOCK;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "app_prod_cat",
               joinColumns = {@JoinColumn(name = "prod_id")},
               inverseJoinColumns = {@JoinColumn(name = "cat_id")})
    private List<CategoryEntity> categories = new ArrayList<CategoryEntity>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "product")
    private List<TagToValueEntity> tagValues;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "product")
    private List<OrderToProductEntity> orders = new ArrayList<OrderToProductEntity>();

    public ProductEntity() {
        setEntityCode("Product");
    }

    @Override
    public String getAltId() {
        return altId;
    }

    public void setAltId(String altId) {
        this.altId = altId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<TagToValueEntity> getTagValues() {
        return tagValues;
    }

    public void setTagValues(List<TagToValueEntity> tagValues) {
        this.tagValues = tagValues;
    }

    public StockStatus getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(StockStatus stockStatus) {
        this.stockStatus = stockStatus;
    }

    public List<OrderToProductEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderToProductEntity> orders) {
        this.orders = orders;
    }
}
