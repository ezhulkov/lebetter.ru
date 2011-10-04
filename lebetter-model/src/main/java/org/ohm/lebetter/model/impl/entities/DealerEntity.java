package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ohm.lebetter.model.DictSyncAware;
import org.ohm.lebetter.model.SitemapAware;
import org.room13.mallcore.model.impl.BaseCreatorRepAwareEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "app_dealer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class DealerEntity
        extends BaseCreatorRepAwareEntity
        implements SitemapAware, DictSyncAware {

    @Column
    private String altId;

    @Column
    private String name;

    @Column
    private Integer discount;

    @Column
    private String description;

    public DealerEntity() {
        setEntityCode("Dealer");
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

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
