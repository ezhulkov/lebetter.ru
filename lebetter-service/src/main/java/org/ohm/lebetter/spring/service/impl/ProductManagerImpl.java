package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.Constants;
import org.ohm.lebetter.Constants.AppCacheKeys;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ProductDao;
import org.ohm.lebetter.spring.service.ProductManager;
import org.room13.mallcore.annotations.AppCache;
import org.room13.mallcore.annotations.AppCacheFlush;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.dao.OwnerDao;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.room13.mallcore.util.StringUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@SuppressWarnings("unchecked")
public class ProductManagerImpl
        extends KinderGenericManagerImpl<ProductEntity, UserEntity>
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
    @AppCacheFlush(keys = {Constants.AppCacheKeys.PRODS_IN_CAT_COUNT,
                           Constants.AppCacheKeys.TOP_PRODUCTS,
                           Constants.AppCacheKeys.PROMO_PRODUCTS})
    public void save(ProductEntity object, UserEntity caller) throws ObjectExistsException {

        object.setAltId(StringUtil.translit(object.getName()) + "-" + object.getRootId());
        super.save(object, caller);

    }

    protected DetachedCriteria applySort(String sortField, String sortOrder, DetachedCriteria criteria) {
        if (sortField != null) {
            if (sortField.contains(".")) {
                String[] sAliases = sortField.split("\\.");
                for (int i = 0; i < sAliases.length - 1; i++) {
                    criteria = criteria.createAlias(sAliases[i], sAliases[i]);
                }
            }
            if ("desc".equalsIgnoreCase(sortOrder)) {
                criteria = criteria.addOrder(Order.desc(sortField));
            } else {
                criteria = criteria.addOrder(Order.asc(sortField));
            }
        }
        return criteria;
    }

    @Override
    public ProductEntity getProductByArticul(String articul) {
        DetachedCriteria criteria = DetachedCriteria.
                forClass(ProductEntity.class).
                add(Restrictions.eq("rootId", articul)).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY));
        List<ProductEntity> res = genericDao.findRootByCriteria(criteria, 0, 1);
        if (res.size() == 0) {
            return null;
        } else {
            return res.get(0);
        }
    }

    @Override
    public List<Long> getIdsByCategory(CategoryEntity category) {

        DetachedCriteria criteria = DetachedCriteria.forClass(ProductEntity.class);

        criteria.createAlias("categories", "cat");
        criteria.add(Restrictions.eq("cat.rootId", category.getRootId()));
        criteria.addOrder(Order.asc("name"));
        Projection projection = Projections.property("id");
        return genericDao.findProjectionByCriteria(criteria, projection, -1, -1);

    }

    @Override
    @Transactional
    public void removeByIdList(Set<Long> ids, UserEntity user) {
        for (Long id : ids) {
            remove(get(id), user);
        }
    }

    @Override
    @AppCache(key = AppCacheKeys.PRODUCTS)
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

}
