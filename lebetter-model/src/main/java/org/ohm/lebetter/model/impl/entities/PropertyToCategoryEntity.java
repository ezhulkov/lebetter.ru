package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.impl.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 21.04.2009
 * Time: 13:50:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "app_property2category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class PropertyToCategoryEntity extends BaseEntity {

    private static final long serialVersionUID = 1121174133421179213L;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    private PropertyEntity property;

    @Column
    private int position;

    public PropertyToCategoryEntity() {
        setEntityCode("PropertyToCategory");
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public PropertyEntity getProperty() {
        return property;
    }

    public void setProperty(PropertyEntity property) {
        this.property = property;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}