package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ohm.lebetter.model.SitemapAware;
import org.room13.mallcore.model.CreatorAware;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.model.OwnerAware;
import org.room13.mallcore.model.impl.BaseOwnerAwareEntity;
import org.room13.mallcore.model.impl.entities.ImageStatusEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
        implements OwnerAware, CreatorAware, SitemapAware, ImageAware {

    private static final long serialVersionUID = 2111426163273315211L;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, optional = false)
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    @JoinColumns({
                         @JoinColumn(name = "rootId", referencedColumnName = "object_id",
                                     insertable = false, updatable = false),
                         @JoinColumn(name = "entityCode", referencedColumnName = "object_code",
                                     insertable = false, updatable = false)
                 })
    private ImageStatusEntity imageStatus;

    @Column
    private String altId;

    @Column
    private String name = "";

    @Column
    private String code = "";

    @Column
    private String description = "";

    @Column
    private boolean tomain = false;

    @Column
    private String tomainname = "";

    @Column
    private boolean hidemain = false;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getAltId() {
        return altId;
    }

    public void setAltId(String altId) {
        this.altId = altId;
    }

    public boolean isTomain() {
        return tomain;
    }

    public void setTomain(boolean tomain) {
        this.tomain = tomain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTomainname() {
        return tomainname;
    }

    public void setTomainname(String tomainname) {
        this.tomainname = tomainname;
    }

    @Override
    public ImageStatusEntity getImageStatus() {
        return imageStatus;
    }

    @Override
    public boolean isImageReady() {
        return ObjectMediaImageAwareImpl.isImageReady(imageStatus);
    }

    @Override
    public boolean isImageError() {
        return ObjectMediaImageAwareImpl.isImageError(imageStatus);
    }

    @Override
    public boolean isImageProcessing() {
        return ObjectMediaImageAwareImpl.isImageProcessing(imageStatus);
    }

    @Override
    public boolean isImageNotSet() {
        return ObjectMediaImageAwareImpl.isImageNotSet(imageStatus);
    }

    @Override
    public void setImageStatus(ImageStatusEntity imageStatus) {
        this.imageStatus = imageStatus;
    }

    public boolean isShowmain() {
        return hidemain;
    }

    public void setShowmain(boolean hidemain) {
        this.hidemain = hidemain;
    }
}