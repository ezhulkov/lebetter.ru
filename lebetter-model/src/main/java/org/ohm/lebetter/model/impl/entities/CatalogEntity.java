package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.model.impl.BaseCreatorAwareEntity;
import org.room13.mallcore.model.impl.entities.ImageStatusEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "app_catalog")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@AccessType("field")
public class CatalogEntity
        extends BaseCreatorAwareEntity
        implements ImageAware {

    @Column
    protected String name;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH, optional = false)
    @Cache(usage = CacheConcurrencyStrategy.NONE)
    @JoinColumns({
            @JoinColumn(name = "rootId", referencedColumnName = "object_id",
                    insertable = false, updatable = false),
            @JoinColumn(name = "entityCode", referencedColumnName = "object_code",
                    insertable = false, updatable = false)
    })
    private ImageStatusEntity imageStatus;

    public CatalogEntity() {
        setEntityCode("Catalog");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setImageStatus(ImageStatusEntity imageStatus) {
        this.imageStatus = imageStatus;
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

}
