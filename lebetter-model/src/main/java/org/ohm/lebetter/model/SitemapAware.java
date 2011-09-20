package org.ohm.lebetter.model;

import org.room13.mallcore.model.ObjectBaseEntity;


public interface SitemapAware extends ObjectBaseEntity {

    public String getAltId();

    public void setAltId(String altId);

}
