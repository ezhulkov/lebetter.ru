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
    private Integer position;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity parent = null;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}