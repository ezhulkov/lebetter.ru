package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.model.SitemapAware;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ProductDao;
import org.ohm.lebetter.spring.service.ProductManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.spring.dao.OwnerDao;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.room13.mallcore.util.StringUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    public List<Long> getIdsByCategory(CategoryEntity category) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductEntity.class);

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
}
