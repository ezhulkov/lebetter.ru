package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.CatalogDao;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;

@SuppressWarnings("unchecked")
public class CatalogDaoHibernate
        extends CreatorAwareDaoHibernate<CatalogEntity, UserEntity>
        implements CatalogDao {

    public CatalogDaoHibernate() {
        super(CatalogEntity.class);
    }

}
