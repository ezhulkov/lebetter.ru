package org.ohm.lebetter.spring.dao;

import org.hibernate.criterion.Order;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.GenericDao;
import org.room13.mallcore.spring.dao.ObjectCreatorAwareDao;

import java.util.List;


public interface ProductDao
        extends GenericDao<ProductEntity, UserEntity>,
                ObjectCreatorAwareDao {

    public List<Long> getSearchProductIds(CategoryEntity category,
                                          PropertyValueEntity[][] values,
                                          Order sort);

}
