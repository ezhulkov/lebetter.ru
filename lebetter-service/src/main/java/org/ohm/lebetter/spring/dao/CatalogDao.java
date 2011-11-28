package org.ohm.lebetter.spring.dao;

import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.GenericDao;
import org.room13.mallcore.spring.dao.ObjectCreatorAwareDao;


public interface CatalogDao
        extends GenericDao<CatalogEntity, UserEntity>,
                ObjectCreatorAwareDao {

}

