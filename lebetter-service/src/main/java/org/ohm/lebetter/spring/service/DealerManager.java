package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.OwnerAwareManager;

public interface DealerManager
        extends GenericManager<DealerEntity, UserEntity>,
                SitemapAwareManager<DealerEntity>,
                OwnerAwareManager<DealerEntity, UserEntity> {

}
