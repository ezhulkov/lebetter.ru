package org.ohm.lebetter.spring.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ProductPhotoDao;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;

import java.util.List;

@SuppressWarnings("unchecked")
public class ProductPhotoDaoHibernate
        extends CreatorAwareDaoHibernate<ProductPhotoEntity, UserEntity>
        implements ProductPhotoDao {

    public ProductPhotoDaoHibernate() {
        super(ProductPhotoEntity.class);
    }

    @Override
    public List<Long> getAllIdsByProduct(ProductEntity product) {
        return getHibernateTemplate().find("select rootId from ProductPhotoEntity " +
                                           "where objectDeleted=false and rootObject=true " +
                                           "and product.rootId=? order by rootId", product.getRootId());
    }

    @Override
    public List<ProductPhotoEntity> findRootByCriteria(DetachedCriteria criteria,
                                                       int startIndex, int count) {
        criteria = criteria.add(Restrictions.eq("objectDeleted", false)).
                add(Restrictions.eq("rootObject", true));
        Criteria cr = criteria.getExecutableCriteria(getSession()).setCacheable(false);
        if (count > 0) {
            cr.setFirstResult(startIndex);
            cr.setMaxResults(count);
        }
        return cr.list();
    }
}
