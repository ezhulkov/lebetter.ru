package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.DealerDao;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;

public class DealerDaoHibernate
        extends CreatorAwareDaoHibernate<DealerEntity, UserEntity>
        implements DealerDao {

    public DealerDaoHibernate() {
        super(DealerEntity.class);
    }

}
