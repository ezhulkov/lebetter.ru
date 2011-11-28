package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.CatalogDao;
import org.ohm.lebetter.spring.service.CatalogManager;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.model.impl.embedded.AnyObjectPK;
import org.room13.mallcore.model.impl.entities.ImageStatusEntity;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;


@SuppressWarnings("unchecked")
public class CatalogManagerImpl
        extends LBGenericManagerImpl<CatalogEntity, UserEntity>
        implements CatalogManager {

    protected CatalogDao catalogDao;

    public CatalogManagerImpl() {
        super(CatalogEntity.class);
    }

    @Required
    public void setCatalogDao(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
        this.genericDao = catalogDao;
    }

    @Override
    @Transactional
    public ImageAware.ImageStatus getImageStatus(CatalogEntity entity, UserEntity caller) {
        return super.getImageStatus(entity, caller);
    }

    @Override
    @Transactional
    public void setImageStatus(CatalogEntity obe, ImageAware.ImageStatus imageStatus,
                               UserEntity caller) {
        AnyObjectPK key = new AnyObjectPK(obe.getRootId(), obe.getEntityCode());
        ImageStatusEntity status = (ImageStatusEntity) catalogDao.getHibernateTemplate().get(ImageStatusEntity.class, key);
        status.setImageStatus(imageStatus);
        catalogDao.getHibernateTemplate().update(status);
    }

}
