package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.ImageAwareManager;

import java.awt.*;
import java.util.Collection;
import java.util.List;

public interface ProductPhotoManager
        extends GenericManager<ProductPhotoEntity, UserEntity>,
                ImageAwareManager<ProductPhotoEntity, UserEntity> {

    public List<ProductPhotoEntity> getAllByProduct(ProductEntity product);

    public List<ProductPhotoEntity> getAllByProduct(ProductEntity product, int count);

    public void removeByIdList(Collection<Long> ids, UserEntity caller);

    public ProductPhotoEntity getMainPhoto(ProductEntity product);

    public void setMainPhoto(ProductPhotoEntity photo);

}
