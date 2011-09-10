package org.ohm.lebetter.spring.dao.impl;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.CategoryDao;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 27.04.2009
 * Time: 15:19:26
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
public class CategoryDaoHibernate
        extends CreatorAwareDaoHibernate<CategoryEntity, UserEntity>
        implements CategoryDao {

    public CategoryDaoHibernate() {
        super(CategoryEntity.class);
    }

    @Override
    public boolean exists(CategoryEntity category) {

        if (category == null) {
            return false;
        }

        Query query;
        if (category.getParent() != null) {
            query = getSession().createQuery("select id from CategoryEntity " +
                                             "where lower(name)=? and rootObject=true " +
                                             "and parent.rootId=? and " +
                                             "parent.rootObject=true");
            query.setString(0, category.getName().toLowerCase());
            query.setLong(1, category.getParent().getRootId());
        } else {
            query = getSession().createQuery("select id from CategoryEntity where " +
                                             "lower(name)=? and rootObject=true " +
                                             "and parent is null");
            query.setString(0, category.getName().toLowerCase());
        }
        query.setCacheable(true);

        return !query.list().isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CategoryEntity> getAllRootCategories() {
        Query query = getSession().createQuery("from CategoryEntity where " +
                                               "parent is null and rootObject=true " +
                                               "order by position,name");
        query.setCacheable(true);
        return query.list();
    }

    @Override
    public List<CategoryEntity> getAllCategories(CategoryEntity parent) {
        Query query = getSession().createQuery("from CategoryEntity " +
                                               "where parent.rootId=? and parent.rootObject=true " +
                                               "and rootObject=true order by position,name");
        query.setCacheable(true);
        query.setLong(0, parent.getRootId());
        return query.list();
    }

    @Override
    public List<CategoryEntity> getAllReadyCategories() {
        Query query = getSession().createQuery("from CategoryEntity " +
                                               "where objectStatus=? and rootObject=true " +
                                               "order by position,name");
        query.setCacheable(true);
        query.setInteger(0, ObjectBaseEntity.Status.READY.ordinal());
        return query.list();
    }

    @Override
    public List<CategoryEntity> getAllReadyCategories(CategoryEntity parent) {

        StringBuffer hql = new StringBuffer();
        hql.append("from CategoryEntity ");
        hql.append("where parent.rootId=:id and parent.rootObject=true and ");
        hql.append("objectStatus=:status and rootObject=true ");
        hql.append("order by position,name");

        Query query = getSession().createQuery(hql.toString());
        query.setCacheable(true);
        query.setLong("id", parent.getRootId());
        query.setInteger("status", ObjectBaseEntity.Status.READY.ordinal());

        return query.list();
    }

    @Override
    public long getAllRootCategoriesCount() {
        Query query = getSession().createQuery("select count(ce.id) from CategoryEntity ce " +
                                               "where parent is null and rootObject=true");
        query.setCacheable(true);
        return (Long) query.list().get(0);
    }

    @Override
    public long getAllCategoriesCount(CategoryEntity parent) {
        Query query = getSession().createQuery("select count(ce.id) from CategoryEntity ce " +
                                               "where parent.rootId=? and parent.rootObject=true " +
                                               "and rootObject=true");
        query.setCacheable(true);
        query.setLong(0, parent.getRootId());
        return (Long) query.list().get(0);
    }

    @Override
    public List<CategoryEntity> getAllReadyLeafCategories() {

        StringBuffer sql = new StringBuffer();
        sql.append("select p.rootid  ");
        sql.append("from app_category p left join app_category p3 on p3.rootid=p.parent_id ");
        sql.append("where p.rootobject=true and p.objectdeleted=false and ");
        sql.append("((p3.rootobject=true and p3.objectdeleted=false and ");
        sql.append("p3.objectstatus=:p3status) or (p3 is null)) and ");
        sql.append("0=(select count(p2.id) from app_category p2 ");
        sql.append("where p2.parent_id=p.rootid and ");
        sql.append("p2.objectdeleted=false and p2.rootobject=true and p2.objectstatus=:p2status) ");
        sql.append("order by p3.name,p.name");

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("Grid select: " + sql);
        }

        SQLQuery query = getSession().createSQLQuery(sql.toString());
        query.setInteger("p2status", ObjectBaseEntity.Status.READY.ordinal());
        query.setInteger("p3status", ObjectBaseEntity.Status.READY.ordinal());

        List<BigInteger> ids = query.list();
        List<Long> result = new ArrayList<Long>(ids.size());

        for (BigInteger id : ids) {
            result.add(new Long(id.longValue()));
        }

        return ids.size() == 0 ? new ArrayList<CategoryEntity>() : getAll(result, 0, ids.size() - 1);

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CategoryEntity> getAllReadyRootCategories() {

        StringBuffer hql = new StringBuffer();
        hql.append("from CategoryEntity ");
        hql.append("where parent is null and objectStatus=? and rootObject=true ");
        hql.append("order by position,name");

        Query query = getSession().createQuery(hql.toString());
        query.setCacheable(true);
        query.setInteger(0, ObjectBaseEntity.Status.READY.ordinal());

        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyEntity> getAllProperties(CategoryEntity category) {
        Query query = getSession().createQuery("select c from CategoryEntity p join " +
                                               "p.properties lnk join lnk.property c " +
                                               "where p.rootObject=true and p.objectDeleted=false " +
                                               "and p.rootId=? and " +
                                               "c.rootObject=true and c.objectDeleted=false " +
                                               "order by lnk.position");
        query.setCacheable(true);
        query.setLong(0, category.getRootId());
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyEntity> getAllReadyPropertiesForSearch(CategoryEntity category, String exclude) {
        Query query = getSession().createQuery("select c from CategoryEntity p join " +
                                               "p.properties lnk join lnk.property c " +
                                               "where p.rootObject=true and p.objectDeleted=false " +
                                               "and p.rootId=? and " +
                                               (exclude == null ?
                                                "" :
                                                " (c.code is null or c.code != '" + exclude + "') and ") +
                                               "c.rootObject=true and c.objectDeleted=false " +
                                               "and c.objectStatus=? and " +
                                               "(c.type=? or c.type=?) order by lnk.position");
        query.setCacheable(true);
        query.setLong(0, category.getRootId());
        query.setInteger(1, ObjectBaseEntity.Status.READY.ordinal());
        query.setInteger(2, PropertyEntity.Type.LIST.ordinal());
        query.setInteger(3, PropertyEntity.Type.DICTIONARY.ordinal());
        return query.list();
    }

    @Override
    public boolean hasCategories(Long prodId) {
        SQLQuery query = getSession().createSQLQuery("SELECT count(*) FROM app_prod_cat WHERE prod_id=:prod");
        query.setLong("prod", prodId);
        return ((BigInteger) query.list().get(0)).intValue() != 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyEntity> getAllReadyProperties(CategoryEntity category) {
        Query query = getSession().createQuery("select c from CategoryEntity p " +
                                               "join p.properties lnk join lnk.property c " +
                                               "where p.rootObject=true and p.objectDeleted=false " +
                                               "and p.rootId=? and " +
                                               "c.rootObject=true and c.objectDeleted=false " +
                                               "and c.objectStatus=? " +
                                               "order by lnk.position");
        query.setCacheable(true);
        query.setLong(0, category.getRootId());
        query.setInteger(1, ObjectBaseEntity.Status.READY.ordinal());
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyEntity> getAllReadyPropertiesByType(CategoryEntity category,
                                                            PropertyEntity.Type type) {
        Query query = getSession().createQuery("select c from CategoryEntity p " +
                                               "join p.properties lnk join lnk.property c " +
                                               "where p.rootObject=true and p.objectDeleted=false " +
                                               "and p.rootId=? and " +
                                               "c.rootObject=true and c.objectDeleted=false " +
                                               "and c.objectStatus=? and c.type=? " +
                                               "order by lnk.position");
        query.setCacheable(true);
        query.setLong(0, category.getRootId());
        query.setInteger(1, ObjectBaseEntity.Status.READY.ordinal());
        query.setInteger(2, type.ordinal());
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyEntity> getPropertiesByMultiple(CategoryEntity category, boolean multiple) {
        Query query = getSession().createQuery("select c from CategoryEntity p join " +
                                               "p.properties lnk join lnk.property c " +
                                               "where p.rootObject=true and p.objectDeleted=false " +
                                               "and p.rootId=? " +
                                               "and c.rootObject=true and c.objectDeleted=false " +
                                               "and c.objectStatus=? and c.multiple=? " +
                                               "order by lnk.position");
        query.setCacheable(true);
        query.setLong(0, category.getRootId());
        query.setInteger(1, ObjectBaseEntity.Status.READY.ordinal());
        query.setBoolean(2, multiple);
        return query.list();
    }

}