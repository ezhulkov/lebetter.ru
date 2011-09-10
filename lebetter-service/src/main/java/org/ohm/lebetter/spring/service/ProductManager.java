package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;

import java.util.List;
import java.util.Set;

public interface ProductManager
        extends GenericManager<ProductEntity, UserEntity>,
                ObjectSearchAwareManager<ProductEntity> {

    public ProductEntity getProductByArticul(String articul);

    public List<Long> getIdsByCategory(CategoryEntity category);

    public void removeByIdList(Set<Long> ids, UserEntity user);

}
