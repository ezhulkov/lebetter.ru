package org.ohm.lebetter.model.impl.entities;

import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ohm.lebetter.model.SitemapAware;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.model.impl.BaseOwnerAwareEntity;
import org.room13.mallcore.model.impl.entities.ImageStatusEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "app_dealer")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@AccessType("field")
public class DealerEntity
        extends BaseOwnerAwareEntity
        implements SitemapAware, ImageAware {

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
    private String name;

    @Column(name = "address")
    private String addressLine;

    @Column
    private String city;

    @Column
    private String site;

    @Column
    private String email;

    @Column
    private String telephone;

    @Column
    private int discount = 0;

    @Column
    private String description;

    @Column
    private int zoom = 12;

    @Column
    private double lat = 55.754167897761d;

    @Column
    private double lng = 37.624053955078125d;

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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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
}
