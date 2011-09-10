package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.TagDao;
import org.room13.mallcore.spring.dao.impl.GenericDaoHibernate;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 27.04.2009
 * Time: 15:19:26
 * To change this template use File | Settings | File Templates.
 */
public class TagDaoHibernate
        extends GenericDaoHibernate<TagToValueEntity, UserEntity>
        implements TagDao {

    public TagDaoHibernate() {
        super(TagToValueEntity.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TagToValueEntity> getByProduct(ProductEntity product) {

        List<TagToValueEntity> list = getHibernateTemplate().find(
                "from TagToValueEntity where objectDeleted=false and " +
                "product=? ", product);

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("TagDaoHibernate.getByObject. Size: " + list.size());
        }

        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyValueEntity> getTagCloud() {
        List<PropertyValueEntity> tagCloud = new LinkedList<PropertyValueEntity>();
        List res = getHibernateTemplate().find("select id " +
                                               "from PropertyValueEntity " +
                                               "where objectDeleted=false and tagcount>0 order by id");
        for (Object re : res) {
            Long vid = (Long) re;
            PropertyValueEntity tagValue = new PropertyValueEntity();
            tagValue.setId(vid);
            tagCloud.add(tagValue);
        }

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("TagValue.getTagCloud. Size: " + tagCloud.size());
        }
        return tagCloud;
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyValueEntity> getTagCloud(int fromTop, int fromBottom, int fromMandatory) {
        List<PropertyValueEntity> tagCloud = new LinkedList<PropertyValueEntity>();
        // Here we insert params into queue directly because there is only few
        // value variants. So, all modifications of this query should be cached by database.
        String columns = "id";
        String queryTemplate = "select distinct id from ((select {0}" +
                               "            from app_property_value" +
                               "            where tagcount!=0 and tagcount >= " +
                               "            (select avg(tagcount) from app_property_value)" +
                               "            ORDER BY random()" +
                               "            LIMIT {1})" +
                               "            union" +
                               "            (select {2}" +
                               "            from app_property_value" +
                               "            where tagcount!=0 and tagcount < " +
                               "                (select avg(tagcount) from app_property_value)" +
                               "            ORDER BY random()" +
                               "            LIMIT {3})" +
                               "            union" +
                               "            (select {4}" +
                               "            from app_property_value" +
                               "            where uitagmandatory=true" +
                               "            ORDER BY random()" +
                               "            LIMIT {5})) as tags";
        String queryString = MessageFormat.format(queryTemplate,
                                                  columns,
                                                  fromTop,
                                                  columns,
                                                  fromBottom,
                                                  columns,
                                                  fromMandatory);
        List res = getHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(
                queryString).list();


        for (Object re : res) {
            BigInteger vid = (BigInteger) re;
            PropertyValueEntity tagValue = new PropertyValueEntity();
            tagValue.setId(new Long(vid.intValue()));
            tagCloud.add(tagValue);
        }

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("TagValue.getTagCloud. Size: " + tagCloud.size());
        }
        return tagCloud;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean existsByProduct(ProductEntity product, PropertyValueEntity value) {
        if (getLogger().isTraceEnabled()) {
            getLogger().trace("TagDaoHibernate.existsByObject. Input: " + product, value);
        }

        boolean result = !getHibernateTemplate().find(
                "select id from TagToValueEntity where " +
                "type=? and objectDeleted=false and " +
                "propertyValue.rootId=? and " +
                "propertyValue.rootObject=true and product=?",
                new Object[]{TagToValueEntity.Type.LIST,
                             value.getId(),
                             product}
        ).isEmpty();

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("TagDaoHibernate.existsByObject. Result: " + result);
        }

        return result;
    }
}