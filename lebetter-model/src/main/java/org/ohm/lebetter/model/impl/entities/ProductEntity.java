package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ohm.lebetter.model.DescriptionAware;
import org.room13.mallcore.model.impl.BaseCreatorAwareEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
        extends BaseCreatorAwareEntity
        implements DescriptionAware {

    @Column
    private String altId;

    @Column
    private String name;

    @Column
    private Float price;

    @Column
    private String description;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "app_prod_cat",
               joinColumns = {@JoinColumn(name = "prod_id")},
               inverseJoinColumns = {@JoinColumn(name = "cat_id")})
    private List<CategoryEntity> categories = new ArrayList<CategoryEntity>();

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "app_prod_prop",
               joinColumns = {@JoinColumn(name = "prod_id")},
               inverseJoinColumns = {@JoinColumn(name = "prop_id")})
    private List<PropertyEntity> selectableProperties;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, mappedBy = "product")
    private List<TagToValueEntity> tagValues;

    public ProductEntity() {
        setEntityCode("Product");
    }

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

    public List<PropertyEntity> getSelectableProperties() {
        return selectableProperties;
    }

    public void setSelectableProperties(List<PropertyEntity> selectableProperties) {
        this.selectableProperties = selectableProperties;
    }

    public List<TagToValueEntity> getTagValues() {
        return tagValues;
    }

    public void setTagValues(List<TagToValueEntity> tagValues) {
        this.tagValues = tagValues;
    }
}
