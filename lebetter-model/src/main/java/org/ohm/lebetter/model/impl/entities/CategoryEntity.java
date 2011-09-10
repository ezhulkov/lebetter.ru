package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.CreatorAware;
import org.room13.mallcore.model.OwnerAware;
import org.room13.mallcore.model.impl.BaseOwnerAwareEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 21.04.2009
 * Time: 13:50:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "app_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class CategoryEntity
        extends BaseOwnerAwareEntity
        implements OwnerAware, CreatorAware {

    private static final long serialVersionUID = 2111426163273315211L;

    @Column
    private String name = "";

    @Column
    private String alias = "";

    @Column
    private String relCode = "";

    @Column(length = 255)
    private String comment = "";

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity parent = null;

    @Column
    private Integer position;

    @Column(name = "product_count")
    private String productCount;

    @Transient
    private String productCount2;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "parent")
    private List<CategoryEntity> children = new ArrayList<CategoryEntity>();

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private List<PropertyToCategoryEntity> properties = new ArrayList<PropertyToCategoryEntity>();

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "app_prod_cat",
               joinColumns = {@JoinColumn(name = "cat_id")},
               inverseJoinColumns = {@JoinColumn(name = "prod_id")})
    private List<ProductEntity> products = new ArrayList<ProductEntity>();

    public CategoryEntity() {
        setEntityCode("Category");
    }

    public String getProductCount2() {
        return productCount2;
    }

    public void setProductCount2(String productCount2) {
        this.productCount2 = productCount2;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    public List<CategoryEntity> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryEntity> children) {
        this.children = children;
    }


    public List<PropertyToCategoryEntity> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyToCategoryEntity> properties) {
        this.properties = properties;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getRelCode() {
        return relCode;
    }

    public void setRelCode(String relCode) {
        this.relCode = relCode;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}