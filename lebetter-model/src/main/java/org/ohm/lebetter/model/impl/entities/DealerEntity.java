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
    private Integer discount;

    @Column
    private String description;

    @Column
    private int zoom;

    @Column
    private float lat;

    @Column
    private float lng;

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

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }
}
