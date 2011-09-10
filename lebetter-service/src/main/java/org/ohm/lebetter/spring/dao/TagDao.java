package org.ohm.lebetter.spring.dao;


import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.GenericDao;

import java.util.List;

public interface TagDao extends GenericDao<TagToValueEntity, UserEntity> {

    public boolean existsByProduct(ProductEntity product, PropertyValueEntity value);

    public List<TagToValueEntity> getByProduct(ProductEntity product);

    public List<PropertyValueEntity> getTagCloud();

    public List<PropertyValueEntity> getTagCloud(int fromTop, int fromBottom, int fromMandatory);

}