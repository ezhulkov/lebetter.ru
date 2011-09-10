package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.impl.BaseCreatorRepAwareEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 21.04.2009
 * Time: 14:24:09
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "app_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class TagToValueEntity
        extends BaseCreatorRepAwareEntity {

    private static final long serialVersionUID = 1411712925325416811L;

    public enum Type {
        LIST, VALUE
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private PropertyValueEntity propertyValue;

    @Column
    private String value;

    @Enumerated
    private Type type;

    @Transient
    private String tempValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ProductEntity product;

    public TagToValueEntity() {
        setEntityCode("Tag");
    }

    public PropertyValueEntity getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(PropertyValueEntity value) {
        this.propertyValue = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTempValue() {
        return tempValue;
    }

    public void setTempValue(String tempValue) {
        this.tempValue = tempValue;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

}