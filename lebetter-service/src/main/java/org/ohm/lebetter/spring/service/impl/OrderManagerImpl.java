package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.Constants;
import org.ohm.lebetter.model.SitemapAware;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.OrderDao;
import org.ohm.lebetter.spring.service.OrderManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.CreatorRepAware;
import org.room13.mallcore.model.ObjectBaseEntity.Status;
import org.room13.mallcore.spring.dao.OwnerDao;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.room13.mallcore.util.StringUtil;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.AccessControlException;
import java.util.List;


@SuppressWarnings("unchecked")
public class OrderManagerImpl
        extends LBGenericManagerImpl<OrderEntity, UserEntity>
        implements OrderManager {

    public static final RMLogger log = new RMLogger(OrderManagerImpl.class);

    protected OrderDao OrderDao;
    protected OwnerDao ownerDao;

    public OrderManagerImpl() {
        super(OrderEntity.class);
    }

    @Required
    public void setOwnerDao(OwnerDao ownerDao) {
        this.ownerDao = ownerDao;
    }

    @Required
    public void setOrderDao(OrderDao OrderDao) {
        this.OrderDao = OrderDao;
        this.genericDao = OrderDao;
    }

}
