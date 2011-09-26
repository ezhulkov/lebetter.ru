package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.model.SitemapAware;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.DealerDao;
import org.ohm.lebetter.spring.service.DealerManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.spring.dao.OwnerDao;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.room13.mallcore.util.StringUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@SuppressWarnings("unchecked")
public class DealerManagerImpl
        extends LBGenericManagerImpl<DealerEntity, UserEntity>
        implements DealerManager {

    public static final RMLogger log = new RMLogger(DealerManagerImpl.class);

    protected DealerDao dealerDao;
    protected OwnerDao ownerDao;

    public DealerManagerImpl() {
        super(DealerEntity.class);
    }

    @Required
    public void setOwnerDao(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    @Required
    public void setDealerDao(DealerDao dealerDao) {
        this.dealerDao = dealerDao;
        this.genericDao = dealerDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(DealerEntity object, UserEntity caller) throws ObjectExistsException {
        synchronized (dealerDao) {
            String altId = StringUtil.translit(object.getName());
            DealerEntity prev = (DealerEntity) getByAltId(altId);
            if (prev != null && !prev.getId().equals(object.getId())) {
                altId += "-" + object.getId();
            }
            object.setAltId(altId);
            super.save(object, caller);
        }
    }

    @Override
    public SitemapAware getByAltId(String altid) {
        return SitemapAwareManagerImpl.getByAltId(altid, dealerDao);
    }
}
