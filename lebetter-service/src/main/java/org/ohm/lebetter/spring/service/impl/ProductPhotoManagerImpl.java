package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ProductPhotoDao;
import org.ohm.lebetter.spring.service.ProductPhotoManager;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.model.impl.embedded.AnyObjectPK;
import org.room13.mallcore.model.impl.entities.ImageStatusEntity;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;


@SuppressWarnings("unchecked")
public class ProductPhotoManagerImpl
        extends LBGenericManagerImpl<ProductPhotoEntity, UserEntity>
        implements ProductPhotoManager {

    protected ProductPhotoDao productPhotoDao;

    public ProductPhotoManagerImpl() {
        super(ProductPhotoEntity.class);
    }

    @Required
    public void setProductPhotoDao(ProductPhotoDao productPhotoDao) {
        this.productPhotoDao = productPhotoDao;
        this.genericDao = productPhotoDao;
    }

    @Override
    @Transactional
    public void save(ProductPhotoEntity object, UserEntity caller) throws ObjectExistsException {
        if (object.getImageStatus() == null) {
            object.setImageStatus(new ImageStatusEntity());
        }
        if (object.getImageStatus().getId() == null) {
            object.getImageStatus().setId(new AnyObjectPK());
            object.getImageStatus().getId().setObjectId(object.getRootId());
            object.getImageStatus().getId().setObjectCode(object.getEntityCode());
        }
        super.save(object, caller);
    }

    @Override
    @Transactional
    public void create(ProductPhotoEntity object, ProductPhotoEntity parent, UserEntity caller)
            throws ObjectExistsException {
        super.create(object, parent, caller);
    }

    @Override
    public List<ProductPhotoEntity> getAllByProduct(ProductEntity product) {
        return getAllByProduct(product, -1);
    }

    @Override
    public List<ProductPhotoEntity> getAllByProduct(ProductEntity product, int count) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductPhotoEntity.class).
                add(Restrictions.eq("product", product)).
                addOrder(Order.asc("id"));
        return count == -1 ?
               productPhotoDao.findRootByCriteria(criteria, -1, -1) :
               productPhotoDao.findRootByCriteria(criteria, 0, count);
    }

    @Override
    @Transactional
    public void removeByIdList(Collection<Long> ids, UserEntity caller) {
        for (Long id : ids) {
            remove(get(id), caller);
        }
    }

    @Override
    public ProductPhotoEntity getMainPhoto(ProductEntity product) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductPhotoEntity.class).
                add(Restrictions.eq("product", product)).
                add(Restrictions.eq("main", true));
        List<ProductPhotoEntity> res = productPhotoDao.findRootByCriteria(criteria, -1, -1);
        if (res.size() == 0) {
            res = getAllByProduct(product, 1);
        }
        if (res.size() == 0) {
            return null;
        } else {
            return res.get(0);
        }
    }

    @Override
    @Transactional
    public ImageAware.ImageStatus getImageStatus(ProductPhotoEntity entity, UserEntity caller) {
        return super.getImageStatus(entity, caller);
    }

    @Override
    @Transactional
    public void setImageStatus(ProductPhotoEntity obe, ImageAware.ImageStatus imageStatus,
                               UserEntity caller) {
        super.setImageStatus(obe, imageStatus, caller);
    }

    @Override
    @Transactional
    public void setMainPhoto(ProductPhotoEntity photo) {
        if (photo.isMain()) {
            return;
        }
        ProductPhotoEntity prevMain = getMainPhoto(photo.getProduct());
        if (prevMain != null) {
            prevMain.setMain(false);
            productPhotoDao.save(prevMain);
        }
        photo.setMain(true);
        productPhotoDao.save(photo);
    }

}
