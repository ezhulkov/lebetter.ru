package org.ohm.lebetter.spring.service;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.model.SitemapAware;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.spring.dao.GenericDao;
import org.room13.mallcore.spring.service.GenericManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 20.10.2010
 * Time: 10:58:13
 * To change this template use File | Settings | File Templates.
 */
public interface SitemapAwareManager<T extends SitemapAware> extends GenericManager<T, UserEntity> {

    public SitemapAware getByAltId(String altid);

    public class SitemapAwareManagerImpl {

        public static final SitemapAware getByAltId(String id, GenericDao dao) {
            DetachedCriteria criteria = DetachedCriteria.forClass(dao.getPersistentClass());
            criteria.add(Restrictions.eq("altId", id));
            List res = dao.findByCriteria(criteria, -1, -1);
            if (res == null || res.size() == 0) {
                return null;
            } else {
                return (SitemapAware) res.get(0);
            }
        }

    }

}
