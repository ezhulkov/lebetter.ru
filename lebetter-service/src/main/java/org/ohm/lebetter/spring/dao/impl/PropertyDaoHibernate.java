package org.ohm.lebetter.spring.dao.impl;

import org.hibernate.Query;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.PropertyDao;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 27.04.2009
 * Time: 15:19:26
 * To change this template use File | Settings | File Templates.
 */
public class PropertyDaoHibernate
        extends CreatorAwareDaoHibernate<PropertyEntity, UserEntity>
        implements PropertyDao {

    public static final String INSERT_TAG = "INSERT INTO app_tag" +
            "(id,rootid,entitycode,objectdeleted,objectstatus,language_id,searchstatus,rootobject," +
            "type,creator_id,product_id,propertyvalue_id,createddate) " +
            "VALUES" +
            "(nextval('hibernate_sequence')*10,currval('hibernate_sequence')*10,'Tag',false,1,1,0,true," +
            ":type,:creator_id,:product_id,:propertyvalue_id,now())";

    public static final String REMOVE_TAGS_FOR_PRODUCT = "DELETE FROM app_tag WHERE id in " +
            "(SELECT app_tag.id from app_tag INNER JOIN app_property_value ON " +
            "app_property_value.id=propertyvalue_id " +
            "INNER JOIN app_property ON app_property.id=property_id " +
            "WHERE app_property.code=:code AND app_tag.product_id=:product_id)";

    public static final String REMOVE_SELECTABLE_PROPERTIES = "DELETE FROM app_prod_prop " +
            "WHERE prod_id=:product_id AND prop_id in (" +
            "SELECT app_property.id from app_property WHERE code=:code)";


    public PropertyDaoHibernate() {
        super(PropertyEntity.class);
    }

    @Override
    public PropertyEntity getTypeByCode(String code) {
        Query query = getSession().createQuery("from PropertyEntity where lower(code)=? and " +
                "objectDeleted=false and rootObject=true");
        query.setString(0, code.toLowerCase());
        query.setCacheable(true);
        List types = query.list();
        if (types.isEmpty()) {
            return null;
        } else {
            return (PropertyEntity) types.get(0);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyEntity getTypeByName(String name) {
        Query query = getSession().createQuery("from PropertyEntity where lower(name)=? and " +
                "objectDeleted=false and rootObject=true");
        query.setString(0, name.toLowerCase());
        query.setCacheable(true);
        List types = query.list();
        if (types.isEmpty()) {
            return null;
        } else {
            return (PropertyEntity) types.get(0);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyEntity> getAllRootCategories() {
        Query query = getSession().createQuery("from PropertyEntity " +
                "where parent is null and objectDeleted=false " +
                "and rootObject=true order by name");
        query.setCacheable(true);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyEntity> getAllSubCategories(PropertyEntity parent) {
        Query query = getSession().createQuery("from PropertyEntity " +
                "where parent.rootId=? and parent.rootObject=true and " +
                "parent.objectDeleted=false and objectDeleted=false and " +
                "rootObject=true order by id");
        query.setLong(0, parent.getRootId());
        query.setCacheable(true);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyEntity> getAllReadyRootCategories() {
        Query query = getSession().createQuery("from PropertyEntity " +
                "where parent is null and objectDeleted=false and " +
                "objectStatus=? and rootObject=true order by name");
        query.setInteger(0, PropertyEntity.Status.READY.ordinal());
        query.setCacheable(true);
        return query.list();
    }

    @Override
    public List<PropertyValueEntity> getValuesByType(ObjectBaseEntity object, PropertyEntity propertyEntity) {
        Query query = getSession().createQuery("select tag.propertyValue from TagToValueEntity tag " +
                "where tag.propertyValue.property.rootId=? and " +
                "tag.propertyValue.property.objectDeleted=false and " +
                "tag.propertyValue.rootObject=true and " +
                "tag.propertyValue.objectDeleted=false and " +
                "tag.propertyValue.rootObject=true and tag.product.rootId=?");
        query.setLong(0, propertyEntity.getRootId());
        query.setLong(1, object.getRootId());
        query.setCacheable(true);
        return query.list();
    }

    @Override
    public List<PropertyValueEntity> getAllReadyValues(PropertyEntity propertyEntity) {
        Query query = getSession().createQuery("from PropertyValueEntity " +
                "where property.rootId=? and " +
                "property.rootObject=true and " +
                "objectStatus=? and objectDeleted=false " +
                "and rootObject=true order by additionalDictInfo2,name");
        query.setLong(0, propertyEntity.getRootId());
        query.setInteger(1, ObjectBaseEntity.Status.READY.ordinal());
        query.setCacheable(true);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyValueEntity> getAllValues(PropertyEntity propertyEntity) {
        Query query = getSession().createQuery("from PropertyValueEntity " +
                "where property.rootId=? and objectDeleted=false " +
                "and rootObject=true order by additionalDictInfo2,name");
        query.setLong(0, propertyEntity.getRootId());
        query.setCacheable(true);
        return query.list();
    }

    @Override
    public void addTagsToProductFast(Collection<TagToValueEntity> valueLinks,
                                     Long prodId, Long userId) {
        // добавляем теги
        Query query = getSession().createSQLQuery(INSERT_TAG);
        for (TagToValueEntity tag : valueLinks) {
            query.setInteger("type", tag.getType().ordinal());
            query.setLong("creator_id", userId);
            query.setLong("product_id", prodId);
            query.setLong("propertyvalue_id", tag.getPropertyValue().getRootId());
            query.executeUpdate();
        }
        getSession().flush();
    }

    @Override
    public void removeTagsForProduct(Long productId, String tag) {
        Query query = getSession().createSQLQuery(REMOVE_TAGS_FOR_PRODUCT);
        query.setString("code", tag);
        query.setLong("product_id", productId);
        query.executeUpdate();
        getSession().flush();

        Query deleteSelectableProps = getSession().createSQLQuery(REMOVE_SELECTABLE_PROPERTIES);
        deleteSelectableProps.setLong("product_id", productId);
        deleteSelectableProps.setString("code", tag);
        deleteSelectableProps.executeUpdate();
    }

    @Override
    public boolean existsByName(String name, PropertyEntity parent) {
        Query query;
        if (parent != null) {
            query = getSession().createQuery("select id from PropertyEntity where lower(name)=? " +
                    "and objectDeleted=false and rootObject=true " +
                    "and parent.rootId=?");
            query.setString(0, name.toLowerCase());
            query.setLong(1, parent.getRootId());
        } else {
            query = getSession().createQuery("select id from PropertyEntity where lower(name)=? " +
                    "and parent is null and objectDeleted=false " +
                    "and rootObject=true");
            query.setString(0, name.toLowerCase());
        }
        query.setCacheable(true);
        return !query.list().isEmpty();
    }

}