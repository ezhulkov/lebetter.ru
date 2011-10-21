package org.ohm.lebetter.spring.dao;

import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.GenericDao;
import org.room13.mallcore.spring.dao.ObjectCreatorAwareDao;

import java.util.List;


public interface ProductPhotoDao
        extends GenericDao<ProductPhotoEntity, UserEntity>,
                ObjectCreatorAwareDao {

    public List<Long> getAllIdsByProduct(ProductEntity product);

}

