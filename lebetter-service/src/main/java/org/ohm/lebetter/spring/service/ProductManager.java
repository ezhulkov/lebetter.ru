package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity.Status;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.OwnerAwareManager;

import java.util.List;

public interface ProductManager
        extends GenericManager<ProductEntity, UserEntity>,
                ObjectSearchAwareManager<ProductEntity>,
                SitemapAwareManager<ProductEntity>,
                OwnerAwareManager<ProductEntity, UserEntity> {

    public List<Long> getIdsByCategory(CategoryEntity category, Status status);

}
