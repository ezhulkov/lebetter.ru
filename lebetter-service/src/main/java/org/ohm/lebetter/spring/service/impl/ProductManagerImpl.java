package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.spring.service.Constants;
import org.ohm.lebetter.model.SitemapAware;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ProductDao;
import org.ohm.lebetter.spring.service.ProductManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.CreatorRepAware;
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
public class ProductManagerImpl
        extends LBGenericManagerImpl<ProductEntity, UserEntity>
        implements ProductManager {

    public static final RMLogger log = new RMLogger(ProductManagerImpl.class);

    protected ProductDao productDao;
    protected OwnerDao ownerDao;

    public ProductManagerImpl() {
        super(ProductEntity.class);
    }

    @Required
    public void setOwnerDao(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    @Required
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
        this.genericDao = productDao;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ProductEntity object, UserEntity caller) throws ObjectExistsException {

        object.setAltId(StringUtil.translit(object.getName()) + "-" + object.getRootId());
        super.save(object, caller);

    }

    @Override
    public List<Long> getIdsByCategory(CategoryEntity category, Status status) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductEntity.class);

        if (status != null) {
            criteria.add(Restrictions.eq("objectStatus", status));
        }

        if (category != null) {
            if (category.getParent() == null) {
                criteria.createAlias("categories", "cat", CriteriaSpecification.INNER_JOIN);
                criteria.createAlias("cat.parent", "rcat", CriteriaSpecification.INNER_JOIN);
                criteria.add(Restrictions.eq("rcat.rootId", category.getRootId()));
            } else {
                criteria.createAlias("categories", "cat");
                criteria.add(Restrictions.eq("cat.rootId", category.getRootId()));
            }
        }
        criteria.addOrder(Order.asc("name"));
        Projection projection = Projections.property("id");
        return genericDao.findProjectionByCriteria(criteria, projection, -1, -1);
    }

    @Override
    public List<Long> getSearchObjectsIds(CategoryEntity category,
                                          PropertyValueEntity[][] values,
                                          Order sort) {
        long startTime = System.currentTimeMillis();
        getRMLogger().debug("Entering getSearchObjectsIds");
        List<Long> result = productDao.getSearchProductIds(category, values, sort);
        getRMLogger().debug("Time: " + (System.currentTimeMillis() - startTime));
        return result;
    }

    @Override
    public int getSearchObjectsIdsCount(CategoryEntity pType, PropertyValueEntity[][] values) {
        return getSearchObjectsIds(pType, values, null).size();
    }

    @Override
    public SitemapAware getByAltId(String altid) {
        return SitemapAwareManagerImpl.getByAltId(altid, productDao);
    }

    @Override
    @Transactional
    public void setOwner(ProductEntity object, String ownerName,
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
    public void setOwners(ProductEntity object, String roleName, List<UserEntity> userEntities) {
        OwnerAwareManagerImpl.setOwners(object, userEntities, ownerDao);
    }


    @Override
    @Transactional
    public void addOwner(ProductEntity object, String roleName, UserEntity user) {
        OwnerAwareManagerImpl.addOwner(object, user, ownerDao);
    }

    @Override
    @Transactional
    public void removeOwner(ProductEntity object, String roleName, UserEntity user) {
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
                                                productDao);
        } else {
            getRMLogger().errorSecurityViolation("Tries to set creator for object.", caller);
            throw new AccessControlException("access denied");
        }
    }

    @Override
    public List<ProductEntity> getObjectsOwnedBy(UserEntity user) {
        List<Long> ids = ownerDao.getObjectsIdsOwnedBy(user, "Dealer", null, null, null);
        return getAll(ids);
    }

}
