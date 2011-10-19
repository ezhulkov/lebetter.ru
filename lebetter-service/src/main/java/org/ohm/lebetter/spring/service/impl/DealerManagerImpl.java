package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.Constants;
import org.ohm.lebetter.model.SitemapAware;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.DealerDao;
import org.ohm.lebetter.spring.service.DealerManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.CreatorRepAware;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.model.ObjectBaseEntity.Status;
import org.room13.mallcore.spring.dao.OwnerDao;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.room13.mallcore.util.StringUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.AccessControlException;
import java.util.List;


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

        if (!StringUtil.isEmpty(object.getSite())) {
            object.setSite(StringUtil.normalizeSiteName(object.getSite()));
        }

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

    @Override
    @Transactional
    public void setOwner(DealerEntity object, String ownerName,
                         String roleName, UserEntity caller)
            throws ObjectExistsException {
        if (getServiceManager().getRoleManager().isRoleAssigned(caller, Constants.Roles.ROLE_ADMIN)) {
            OwnerAwareManagerImpl.setOwner(object, ownerName,
                                           getServiceManager().getUserManager(),
                                           ownerDao);
        } else {
            getRMLogger().errorSecurityViolation("Tries to set owner for object.", object);
            throw new AccessControlException("access denied");
        }
    }

    @Override
    @Transactional
    public void setOwners(DealerEntity object, String roleName, List<UserEntity> userEntities) {
        OwnerAwareManagerImpl.setOwners(object, userEntities, ownerDao);
    }


    @Override
    @Transactional
    public void addOwner(DealerEntity object, String roleName, UserEntity user) {
        OwnerAwareManagerImpl.addOwner(object, user, ownerDao);
    }

    @Override
    @Transactional
    public void removeOwner(DealerEntity object, String roleName, UserEntity user) {
        OwnerAwareManagerImpl.removeOwner(object, user, ownerDao);
    }

    @Override
    @Transactional
    public void setCreatorRep(CreatorRepAware object, String creatorName, UserEntity caller)
            throws ObjectExistsException {
        if (getServiceManager().getRoleManager().isRoleAssigned(caller, Constants.Roles.ROLE_ADMIN)) {
            OwnerAwareManagerImpl.setCreatorRep(object,
                                                creatorName,
                                                getServiceManager().getUserManager(),
                                                dealerDao);
        } else {
            getRMLogger().errorSecurityViolation("Tries to set creator for object.", caller);
            throw new AccessControlException("access denied");
        }
    }

    @Override
    public List<DealerEntity> getObjectsOwnedBy(UserEntity user) {
        List<Long> ids = ownerDao.getObjectsIdsOwnedBy(user, "Dealer", null, null, null);
        return getAll(ids);
    }

    @Override
    public List<String> getCities() {
        DetachedCriteria criteria = DetachedCriteria.forClass(DealerEntity.class);
        criteria.add(Restrictions.eq("objectStatus", Status.READY));
        criteria.setProjection(Projections.distinct(Projections.projectionList().
                add(Projections.property("city"))));
        criteria.addOrder(Order.asc("city"));
        return (List<String>) dealerDao.findByCriteria(criteria, -1, -1);
    }

    @Override
    public List<DealerEntity> getAllReadyByCity(String city) {
        DetachedCriteria criteria = DetachedCriteria.forClass(DealerEntity.class);
        criteria.add(Restrictions.eq("city", city));
        criteria.add(Restrictions.eq("objectStatus", Status.READY));
        return dealerDao.findRootByCriteria(criteria, -1, -1);
    }

    @Override
    public ImageAware.ImageStatus getImageStatus(DealerEntity entity, UserEntity caller) {
        return super.getImageStatus(entity, caller);
    }

    @Override
    @Transactional
    public void setImageStatus(DealerEntity obe, ImageAware.ImageStatus imageStatus, UserEntity caller) {
        super.setImageStatus(obe, imageStatus, caller);
    }

}
