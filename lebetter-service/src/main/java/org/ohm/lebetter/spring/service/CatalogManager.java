package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.ImageAwareManager;

public interface CatalogManager
        extends GenericManager<CatalogEntity, UserEntity>,
                ImageAwareManager<CatalogEntity, UserEntity> {


}
