package org.ohm.lebetter.spring.dao.impl;

import org.hibernate.Query;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.PropertyValueDao;
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
public class PropertyValueDaoHibernate
        extends CreatorAwareDaoHibernate<PropertyValueEntity, UserEntity>
        implements PropertyValueDao {


    public static final String ADD_SELECTABLE_PROP =
            "INSERT INTO app_prod_prop(prod_id,prop_id) " +
                    "(SELECT :prod,:prop WHERE NOT EXISTS " +
                    "(SELECT prod_id,prop_id FROM app_prod_prop " +
                    "WHERE prod_id=:prod AND prop_id=:prop))";

    public PropertyValueDaoHibernate() {
        super(PropertyValueEntity.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TagToValueEntity> getTagsForObjectAndGroupCategory(ObjectBaseEntity object,
                                                                   PropertyEntity propertyEntity) {
        Query query = getSession().createQuery("from TagToValueEntity " +
                "where propertyValue.objectDeleted=false and " +
                "propertyValue.objectStatus=? " +
                "and product.rootId=? " +
                "and rootObject=true " +
                "and propertyValue.property.parent.rootId=? " +
                "and propertyValue.property.parent.rootObject=true " +
                "order by propertyValue.property.parent.rootId, " +
                "propertyValue.name");
        query.setCacheable(true);
        query.setInteger(0, ObjectBaseEntity.Status.READY.ordinal());
        query.setLong(1, object.getRootId());
        query.setLong(2, propertyEntity.getRootId());
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TagToValueEntity> getTagsForObjectAndCategory(ObjectBaseEntity object,
                                                              PropertyEntity property) {
        Query query = getSession().createQuery("from TagToValueEntity where " +
                "propertyValue.objectDeleted=false and " +
                "propertyValue.objectStatus=? and " +
                "product.id=? and " +
                "rootObject=true and " +
                "propertyValue.property.rootId=? and " +
                "propertyValue.property.rootObject=true " +
                "order by propertyValue.property.rootId, propertyValue.name");
        query.setInteger(0, ObjectBaseEntity.Status.READY.ordinal());
        query.setLong(1, object.getRootId());
        query.setLong(2, property.getRootId());
        query.setCacheable(true);
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TagToValueEntity> getTagsForObject(ObjectBaseEntity object) {
        Query query = getSession().createQuery("from TagToValueEntity " +
                "where propertyValue.objectDeleted=false and " +
                "propertyValue.objectStatus=? " +
                "and product.rootId=? and rootObject=true " +
                "order by propertyValue.property.rootId, propertyValue.name");
        query.setCacheable(true);
        query.setInteger(0, ObjectBaseEntity.Status.READY.ordinal());
        query.setLong(1, object.getRootId());
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TagToValueEntity> getListTagsForObject(ObjectBaseEntity object) {
        Query query = getSession().createQuery("from TagToValueEntity " +
                "where propertyValue.objectDeleted=false and " +
                "propertyValue.objectStatus=? " +
                "and product.rootId=? and " +
                "rootObject=true and type=? " +
                "order by propertyValue.property.rootId, propertyValue.name");
        query.setCacheable(true);
        query.setInteger(0, ObjectBaseEntity.Status.READY.ordinal());
        query.setLong(1, object.getRootId());
        query.setInteger(2, TagToValueEntity.Type.LIST.ordinal());
        return query.list();

    }

    @Override
    @SuppressWarnings("unchecked")
    public List<TagToValueEntity> getTagsForObjectByMultiple(ObjectBaseEntity object,
                                                             PropertyEntity property,
                                                             boolean multiple) {
        Query query = getSession().createQuery("from TagToValueEntity " +
                "where propertyValue.objectDeleted=false and " +
                "propertyValue.objectStatus=:stat and " +
                "product.rootId=:pid and " +
                "propertyValue.property.multiple=:pmult and " +
                (property == null ?
                        "" : "propertyValue.property.id=:prid and ") +
                "rootObject=true " +
                "order by propertyValue.name");
        query.setCacheable(true);
        query.setInteger("stat", ObjectBaseEntity.Status.READY.ordinal());
        query.setLong("pid", object.getRootId());
        query.setBoolean("pmult", multiple);
        if (property != null) {
            query.setLong("prid", property.getRootId());
        }
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyValueEntity> getValuesByObject(ObjectBaseEntity object,
                                                       PropertyEntity propertyEntity) {
        Query query = getSession().createQuery("from PropertyValueEntity " +
                "where relation.objectType=? and relation.objectId=? and " +
                "objectDeleted=false and " +
                "property.rootId=? and rootObject=true");
        query.setCacheable(true);
        query.setString(0, object.getEntityCode());
        query.setLong(1, object.getRootId());
        query.setLong(2, propertyEntity.getRootId());
        return query.list();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<PropertyValueEntity> getValuesByObject(ObjectBaseEntity object) {
        Query query = getSession().createQuery("from PropertyValueEntity " +
                "where relation.objectType=? and relation.objectId=? and " +
                "objectDeleted=false and " +
                "rootObject=true");
        query.setCacheable(true);
        query.setString(0, object.getEntityCode());
        query.setLong(1, object.getRootId());
        return query.list();
    }

    @Override
    public PropertyValueEntity getValueByCode(String code) {
        Query query = getSession().createQuery("from PropertyValueEntity " +
                "where lower(code)=? and objectDeleted=false " +
                "and rootObject=true");
        query.setCacheable(true);
        query.setString(0, code.toLowerCase());
        List values = query.list();
        if (values.isEmpty()) {
            return null;
        } else {
            return (PropertyValueEntity) values.get(0);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyValueEntity getValueByName(String name, PropertyEntity type) {
        Query query = getSession().createQuery("from PropertyValueEntity " +
                "where lower(name)=? and objectDeleted=false and " +
                "property.rootId=? and " +
                "property.rootObject=true");
        query.setCacheable(true);
        query.setString(0, name.toLowerCase());
        query.setLong(1, type.getRootId());
        List values = query.list();
        if (values.isEmpty()) {
            return null;
        } else {
            return (PropertyValueEntity) values.get(0);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public PropertyValueEntity getValueByAdditionalDictInfo2(String name, PropertyEntity type) {
        Query query = getSession().createQuery("from PropertyValueEntity " +
                "where additionalDictInfo2=? and objectDeleted=false and " +
                "property.rootId=? and " +
                "property.rootObject=true");
        query.setCacheable(true);
        query.setString(0, name);
        query.setLong(1, type.getRootId());
        List values = query.list();
        if (values.isEmpty()) {
            return null;
        } else {
            return (PropertyValueEntity) values.get(0);
        }
    }

    @Override
    public boolean existsByName(String name, PropertyEntity type) {
        boolean result = !getHibernateTemplate().find("select id from PropertyValueEntity " +
                "where lower(name)=? and objectDeleted=false and " +
                "property.rootId=? and rootObject=true",
                new Object[]{name.toLowerCase(),
                        type.getId()}).isEmpty();
        if (getLogger().isTraceEnabled()) {
            getLogger().trace("PropertyValueDaoHibernate.existsByCode. Input: " +
                    name + ", Type: " + type + ", Result: " + result);
        }
        return result;
    }

    @Override
    public void addSelectableProperties(Collection<PropertyValueEntity> vals,
                                        Long prodId) {
        Query q = getSession().createSQLQuery(ADD_SELECTABLE_PROP);
        for (PropertyValueEntity pve : vals) {
            q.setLong("prod", prodId);
            q.setLong("prop", pve.getProperty().getId());
            q.executeUpdate();
        }
        getSession().flush();
    }

}