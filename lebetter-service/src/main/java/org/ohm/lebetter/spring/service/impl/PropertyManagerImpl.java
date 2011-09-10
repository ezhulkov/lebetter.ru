package org.ohm.lebetter.spring.service.impl;

import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.type.LongType;
import org.ohm.lebetter.Constants;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ProductDao;
import org.ohm.lebetter.spring.dao.PropertyDao;
import org.ohm.lebetter.spring.dao.PropertyValueDao;
import org.ohm.lebetter.spring.dao.TagDao;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.spring.sync.SyncDictProcessor;
import org.room13.mallcore.annotations.AppCache;
import org.room13.mallcore.annotations.AppCacheFlush;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.ObjectCreatorAwareManager;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.transaction.annotation.Transactional;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.ohm.lebetter.model.impl.entities.TagToValueEntity.Type.LIST;

@SuppressWarnings("unchecked")
public class PropertyManagerImpl
        extends KinderGenericManagerImpl<PropertyEntity, UserEntity>
        implements PropertyManager {

    private PropertyDao propertyDao;
    private PropertyValueDao propertyValueDao;
    private TagDao tagToValueDao;
    private ProductDao productDao;


    public PropertyManagerImpl() {
        super(PropertyEntity.class);
    }

    @Required
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Required
    public void setTagToValueDao(TagDao tagToValueDao) {
        this.tagToValueDao = tagToValueDao;
    }

    @Required
    public void setPropertyDao(PropertyDao dao) {
        super.setGenericDao(dao);
        this.propertyDao = dao;
    }

    @Required
    public void setPropertyValueDao(PropertyValueDao propertyValueDao) {
        this.propertyValueDao = propertyValueDao;
    }

    @Override
    public List<PropertyEntity> getObjectsCreatedBy(UserEntity creator, String order, String limit) {
        return ObjectCreatorAwareManager.ObjectCreatorAwareManagerImpl.getObjectsCreatedBy(creator,
                                                                                           order,
                                                                                           limit,
                                                                                           this,
                                                                                           propertyDao);
    }

    @Override
    public List<Long> getObjectsIdsCreatedBy(UserEntity creator, String order, String filter, String limit) {
        return ObjectCreatorAwareManager.ObjectCreatorAwareManagerImpl.getObjectsIdsCreatedBy(creator,
                                                                                              order,
                                                                                              filter,
                                                                                              limit,
                                                                                              propertyDao);
    }

    @Override
    public List<Long> getObjectsIdsCreatedBy(UserEntity creator, String order) {
        return getObjectsIdsCreatedBy(creator, order, null, "ALL");
    }

    @Override
    @Transactional
    @AppCacheFlush(keys = {Constants.AppCacheKeys.OBJECT_SEARCH_COMPONENT_DATA})
    public void remove(PropertyEntity property, UserEntity caller) {

        getRMLogger().debug("Remove category...");

        //Remove category
        if (getServiceManager().getRoleManager().isRoleAssigned(caller, Constants.Roles.ROLE_ADMIN)) {

            PropertyEntity propertyPersist = propertyDao.get(property.getId());

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Clean all references from product types...");
            }

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Clean all references from product types complete.");
            }

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Removing from dictToManager map...");
            }

            //Remove from dictToManager map
            if (propertyPersist.getType().equals(PropertyEntity.Type.DICTIONARY)) {
                getServiceManager().getDictPropertyHolder().removeDictCategory(property);
            }

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Remove from dictToManager map complete.");
                getRMLogger().debug("Removing values...");
            }

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Remove values complete.");
            }

            propertyDao.remove(propertyPersist);

        } else {
            getRMLogger().errorSecurityViolation("Tries to del category", property);
            throw new AccessControlException("access denied");
        }
        getRMLogger().debug("Remove category completed.");
    }

    @Override
    @Transactional
    @AppCacheFlush(keys = {Constants.AppCacheKeys.OBJECT_SEARCH_COMPONENT_DATA})
    public void create(PropertyEntity property,
                       PropertyEntity parentCategoryType,
                       UserEntity caller) {
        getRMLogger().debug("Create category...");

        //Check grants
        if (!getServiceManager().getRoleManager().isRoleAssigned(caller, Constants.Roles.ROLE_ADMIN)) {
            getRMLogger().errorSecurityViolation("Tries to add new category.", property);
            throw new AccessControlException("access denied");
        }

        //Check unique
        if (!propertyDao.existsByName(property.getName(), property.getParent())) {

            property.setCreator(caller);

            //Init new ctegory
            property.setCreator(caller);
            if (parentCategoryType != null) {
                property.setParent(parentCategoryType);
            }

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Create persist object...", property);
            }

            //Create persist object
            propertyDao.create(property);

            if (property.getType().equals(PropertyEntity.Type.DICTIONARY)) {
                //If DICTIONARY - register in DictToManager map and fill with default values
                if (getRMLogger().isDebugEnabled()) {
                    getRMLogger().debug("Init new dictionary category.", property);
                }

                getServiceManager().getDictPropertyHolder().addNewDictCategory(property);
                GenericManager genericManager =
                        getServiceManager().
                                getGenericManagerMap().
                                get(property.getDictionary());
                List objs = genericManager.getAll();
                for (Object obj : objs) {
                    List trObjs = genericManager.getTranslatedObjects((ObjectBaseEntity) obj);
                    Long rootId = null;
                    for (Object trObj : trObjs) {
                        ObjectBaseEntity trOBE = (ObjectBaseEntity) trObj;
                        String valueName = trOBE.getName();
                        PropertyValueEntity catValue = new PropertyValueEntity();
                        catValue.setName(valueName);
                        catValue.setRootObject(trOBE.isRootObject());
                        catValue.setLanguage(trOBE.getLanguage());
                        catValue.setRootId(rootId);
                        Map map = getServiceManager().getDictSyncProcessors();
                        SyncDictProcessor processor = (SyncDictProcessor)
                                (!map.containsKey(trOBE.getEntityCode()) ?
                                 map.get("default") :
                                 map.get(trOBE.getEntityCode()));
                        processor.processDictValue(trOBE, catValue);
                        if (trOBE.isRootObject()) {
                            catValue.setProperty(property);
                            catValue.setObjectStatus(trOBE.getObjectStatus());
                            getServiceManager().getPropertyValueManager().
                                    createValueForInsiders(catValue, property, caller);
                        } else {
                            PropertyValueEntity parent =
                                    getServiceManager().getPropertyValueManager().getRoot(rootId);
                            catValue = getServiceManager().
                                    getPropertyValueManager().createL(parent,
                                                                      trOBE.getLanguage().getCode(),
                                                                      caller);
                            catValue.setName(valueName);
                            processor.processDictValue(trOBE, catValue);
                            getServiceManager().getPropertyValueManager().save(catValue, caller);
                        }
                        if (trOBE.isRootObject()) {
                            rootId = catValue.getRootId();
                        }
                    }
                }
            } else if (property.getType().equals(PropertyEntity.Type.VALUE) ||
                       property.getType().equals(PropertyEntity.Type.GROUP)) {
                //If VALUE or GROUP - register STUB value
                PropertyValueEntity catValue = new PropertyValueEntity();
                catValue.setName(Constants.CAT_VALUE_STUB);
                catValue.setObjectStatus(ObjectBaseEntity.Status.READY);
                getServiceManager().getPropertyValueManager().
                        createValueForInsiders(catValue, property, caller);
            }

            //Save it
            saveInternal(property, caller);

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Persist object created.", property);
            }

        } else {
            throw new ObjectExistsException("Category exists exception");
        }

        getRMLogger().debug("Create category completed.");
    }

    @Override
    public void saveInternal(PropertyEntity property, UserEntity caller)
            throws ObjectExistsException {

        //Get persist object
        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Get persist object");
        }
        PropertyEntity propertyPersist = propertyDao.get(property.getId());

        //Check unique
        boolean unique = true;
        if (!propertyPersist.getName().equals(property.getName())) {
            unique = !propertyDao.existsByName(property.getName(), property.getParent());
        }

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Unique: " + unique);
        }

        if (unique) {
            if (property.getType().equals(PropertyEntity.Type.GROUP) ||
                property.getType().equals(PropertyEntity.Type.VALUE)) {
                property.setMultiple(false);
            }

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Set properties to persist object(property, propertyPersist): " +
                                    "(" + property + ", " + propertyPersist + ")", caller);
            }

            //Set properties to persist object
            BeanUtils.copyProperties(property,
                                     propertyPersist, new String[]{"children", "parent", "values"});

            //Explicit save
            propertyDao.save(property);

        } else {
            throw new ObjectExistsException("Type exists exception");
        }

    }

    @Override
    @Transactional
    @AppCacheFlush(keys = {Constants.AppCacheKeys.OBJECT_SEARCH_COMPONENT_DATA})
    public void save(PropertyEntity property, UserEntity caller) throws ObjectExistsException {
        getRMLogger().debug("Save category...");

        //Check grants
        if (!getServiceManager().getRoleManager().isActionGranted(caller,
                                                                  Constants.Actions.EDIT_PROPERTY,
                                                                  property)) {
            getRMLogger().errorSecurityViolation("Tries to edit category.", property);
            throw new AccessControlException("access denied");
        }

        saveInternal(property, caller);

        getRMLogger().debug("Save category completed.");
    }

    @Override
    public List<PropertyEntity> getAllRootProperties() {
        return propertyDao.getAllRootCategories();
    }

    @Override
    public List<PropertyEntity> getAllSubProperties(PropertyEntity parent) {
        return propertyDao.getAllSubCategories(parent);
    }

    @Override
    public List<PropertyEntity> getAllReadyRootProperties() {
        return propertyDao.getAllReadyRootCategories();
    }

    @Override
    public List<PropertyValueEntity> getAllValues(PropertyEntity property) {
        return propertyDao.getAllValues(property);
    }

    @Override
    @AppCache(key = Constants.AppCacheKeys.ALL_READY_VALS)
    public List<PropertyValueEntity> getAllReadyValues(PropertyEntity property) {
        return propertyDao.getAllReadyValues(property);
    }

    /**
     * vals -> tags -> products -> tags -> allvals
     * We need to get ALL values connected to products that have vals :)
     */
    @Override
    @AppCache(key = Constants.AppCacheKeys.FILTER_SET_ALL_VALS)
    public List<Long> getAllProductValueIdsBySeveralValues(CategoryEntity category,
                                                           List<PropertyValueEntity> vals) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PropertyValueEntity.class, "pv");
        criteria.createAlias("pv.tags", "t");
        DetachedCriteria prodsCriteria = criteria.createCriteria("t.product", "pr");
        prodsCriteria.createAlias("pr.categories", "c");
        if (category.getParent() != null) {
            prodsCriteria.add(Restrictions.eq("c.rootId", category.getRootId()));
        } else {
            prodsCriteria.createAlias("c.parent", "cp", CriteriaSpecification.INNER_JOIN);
            prodsCriteria.add(Restrictions.eq("cp.rootId", category.getRootId()));
        }
        for (PropertyValueEntity val : vals) {
            StringBuffer strBuf = new StringBuffer();
            strBuf.append("{alias}.rootid in (select p.rootid from app_product p ");
            strBuf.append("join app_tag t on t.product_id=p.id ");
            strBuf.append("join app_shop sh on p.shop_id=sh.id ");
            strBuf.append("join app_property_value pv on pv.id=t.propertyvalue_id ");
            strBuf.append("where pv.id=? and sh.objectstatus=1 and p.objectstatus=1 and p.missing=false)");
            prodsCriteria.add(Restrictions.sqlRestriction(strBuf.toString(),
                                                          val.getRootId(),
                                                          new LongType()));
        }
        List<Long> vids = (List<Long>) propertyValueDao.
                findProjectionByCriteria(criteria,
                                         Projections.distinct(Projections.property("pv.rootId")),
                                         -1, -1);
        return vids;
    }

    @Override
    @SuppressWarnings("unchecked")
    @AppCache(key = Constants.AppCacheKeys.FILTER_SET_VALS)
    public List<PropertyValueEntity> getAllTaggedValuesForCategoryStrict(PropertyEntity property,
                                                                         CategoryEntity category,
                                                                         List<PropertyValueEntity> vals) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PropertyValueEntity.class, "pv").
                createAlias("property", "p", CriteriaSpecification.INNER_JOIN).
                createAlias("tags", "t", CriteriaSpecification.INNER_JOIN).
                createAlias("t.product", "pr", CriteriaSpecification.INNER_JOIN).
                createAlias("pr.categories", "c", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.eq("pv.objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.eq("pr.objectStatus", ObjectBaseEntity.Status.READY));
        if (category.getParent() != null) {
            criteria.add(Restrictions.eq("c.rootId", category.getRootId()));
        } else {
            criteria.createAlias("c.parent", "cp", CriteriaSpecification.INNER_JOIN);
            criteria.add(Restrictions.eq("cp.rootId", category.getRootId()));
        }
        criteria.add(Restrictions.eq("p.rootId", property.getRootId()));
        if (vals != null && vals.size() != 0) {
            List<Long> allVals = getAllProductValueIdsBySeveralValues(category, vals);
            if (allVals.size() != 0) {
                criteria.add(Restrictions.in("pv.rootId", allVals));
            }
        }
        List<Long> vids = (List<Long>) propertyValueDao.
                findProjectionByCriteria(criteria,
                                         Projections.distinct(Projections.property("pv.id")),
                                         -1, -1);
        return propertyValueDao.getAll(vids, 0, vids.size() - 1);
    }

    @Override
    public List<TagToValueEntity> getTagsForProduct(ProductEntity product) {
        return propertyValueDao.getTagsForObject(product);
    }

    @Override
    public List<TagToValueEntity> getTagsForProduct(ProductEntity product, PropertyEntity property) {
        if (property.getType().equals(PropertyEntity.Type.GROUP)) {
            return propertyValueDao.getTagsForObjectAndGroupCategory(product, property);
        } else {
            return propertyValueDao.getTagsForObjectAndCategory(product, property);
        }
    }

    @Override
    public List<TagToValueEntity> getListTagsForProduct(ProductEntity product) {
        return propertyValueDao.getListTagsForObject(product);
    }

    @Transactional
    @Override
    public void removeTagsForProduct(ProductEntity product, PropertyEntity property) {
        List<TagToValueEntity> tags;
        if (property.getType().equals(PropertyEntity.Type.GROUP)) {
            tags = propertyValueDao.getTagsForObjectAndGroupCategory(product, property);
        } else {
            tags = propertyValueDao.getTagsForObjectAndCategory(product, property);
        }
        for (TagToValueEntity tag : tags) {
            tagToValueDao.remove(tag);
        }
    }

    @Override
    public List<PropertyEntity> getAllMandatoryFields(ProductEntity product) {
        DetachedCriteria criteria = createMandatoryFieldsCriteria(product);
        List<Long> res = propertyDao.
                findRootProjectionByCriteria(criteria,
                                             Projections.distinct(Projections.property("prop.id")),
                                             -1, -1);
        return getAll(res);
    }

    @Override
    public int getAllMandatoryFieldsCount(ProductEntity product) {
        return propertyDao.getCountByCriteria(createMandatoryFieldsCriteria(product), "prop.id", true);
    }

    protected DetachedCriteria createMandatoryFieldsCriteria(ProductEntity product) {
        return DetachedCriteria.forClass(ProductEntity.class).
                createAlias("categories", "prcat", CriteriaSpecification.INNER_JOIN).
                createAlias("prcat.properties", "prop2cat", CriteriaSpecification.INNER_JOIN).
                createAlias("prop2cat.property", "prop", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.eq("id", product.getId())).
                add(Restrictions.eq("prop.mandatory", true)).
                add(Restrictions.eq("prop.objectStatus", ObjectBaseEntity.Status.READY));
    }

    @Override
    public List<PropertyEntity> getSetMandatoryFields(ProductEntity product) {
        DetachedCriteria criteria = getSetMandatoryFieldsCriteria(product);
        List<Long> res = propertyDao.
                findRootProjectionByCriteria(criteria,
                                             Projections.distinct(Projections.property("prop.id")),
                                             -1, -1);
        return getAll(res);
    }

    @Override
    public int getSetMandatoryFieldsCount(ProductEntity product) {
        DetachedCriteria criteria = getSetMandatoryFieldsCriteria(product);
        return propertyDao.getCountByCriteria(criteria, "prop.id", true);
    }

    protected DetachedCriteria getSetMandatoryFieldsCriteria(ProductEntity product) {
        return DetachedCriteria.forClass(ProductEntity.class).
                createAlias("categories", "prcat", CriteriaSpecification.INNER_JOIN).
                createAlias("prcat.properties", "prop2cat", CriteriaSpecification.INNER_JOIN).
                createAlias("prop2cat.property", "prop", CriteriaSpecification.INNER_JOIN).
                createAlias("prop.values", "pvals", CriteriaSpecification.INNER_JOIN).
                createAlias("pvals.tags", "t2v", CriteriaSpecification.INNER_JOIN).
                createAlias("t2v.product", "ps", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.eq("id", product.getId())).
                add(Restrictions.eq("ps.id", product.getId())).
                add(Restrictions.eq("prop.mandatory", true)).
                add(Restrictions.eq("prop.objectStatus", ObjectBaseEntity.Status.READY));
    }

    @Override
    @Transactional
    public void removeTagsForProduct(ProductEntity product, String tag) {
        propertyDao.removeTagsForProduct(product.getRootId(), tag);
    }

    @Override
    public List<PropertyEntity> getPropertiesByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PropertyEntity.class).
                add(Restrictions.eq("code", code)).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.eq("objectDeleted", false));
        return propertyDao.findRootByCriteria(criteria, -1, -1);
    }

    @Override
    public List<PropertyValueEntity> getValuesByType(ProductEntity product, PropertyEntity property) {
        return propertyDao.getValuesByType(product, property);
    }

    @Override
    public PropertyEntity getPropertyByCode(String code) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PropertyEntity.class).
                add(Restrictions.eq("code", code)).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.eq("objectDeleted", false));
        return (PropertyEntity) DataAccessUtils.uniqueResult(
                propertyDao.findRootByCriteria(criteria, 0, 1));
    }

    @Override
    public List<ProductEntity> getTaggedProductsByValue(PropertyValueEntity value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductEntity.class).
                add(Subqueries.propertyIn("id", initTaggedProductsCriteria(value))).
                addOrder(Order.asc("name"));
        return productDao.findRootByCriteria(criteria, -1, -1);
    }

    protected DetachedCriteria initTaggedProductsCriteria(PropertyValueEntity value) {
        return DetachedCriteria.forClass(TagToValueEntity.class).
                createAlias("product", "prod", CriteriaSpecification.INNER_JOIN).
                createAlias("prod.shop", "ps", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.eq("prod.objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.eq("prod.missing", false)).
                add(Restrictions.eq("ps.objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.eq("propertyValue", value)).
                setProjection(Projections.distinct(Projections.property("product.id")));
    }

    @Override
    public List<Long> getTaggedProductIdsByValue(PropertyValueEntity value) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ProductEntity.class).
                add(Subqueries.propertyIn("id", initTaggedProductsCriteria(value))).
                setProjection(Projections.property("id")).
                addOrder(Order.asc("name"));
        return productDao.findByCriteria(criteria, -1, -1);
    }

    @Override
    public List<TagToValueEntity> getTagsForProductByMultiple(ProductEntity product,
                                                              PropertyEntity property,
                                                              boolean multiple) {
        return propertyValueDao.getTagsForObjectByMultiple(product, property, multiple);
    }

    @Override
    public List<TagToValueEntity> getTagsForProductByMultiple(ProductEntity product, boolean multiple) {
        return propertyValueDao.getTagsForObjectByMultiple(product, null, multiple);
    }

    @Override
    @Transactional
    public void setManuallyTagsToProduct(Collection<TagToValueEntity> valueLinks,
                                         ProductEntity product, UserEntity caller) {

        // Фильтрация по уникальности
        valueLinks = filterTags(valueLinks);

        if (!valueLinks.isEmpty()) {

            List<PropertyValueEntity> listTags = new ArrayList<PropertyValueEntity>();
            for (TagToValueEntity tagToValueEntity : valueLinks) {
                PropertyValueEntity val = tagToValueEntity.getPropertyValue();
                listTags.add(val);
                tagToValueEntity.setObjectDeleted(false);
                tagToValueEntity.setObjectStatus(ObjectBaseEntity.Status.READY);
                tagToValueEntity.setCreator(caller);
                tagToValueEntity.setProduct(product);
                tagToValueEntity.setPropertyValue(val);
                tagToValueDao.create(tagToValueEntity);
            }

            //Set tagsvalue property
            StringBuilder tags = new StringBuilder();
            for (PropertyValueEntity tempValue : listTags) {
                PropertyValueEntity tempValueRoot =
                        getServiceManager().getPropertyValueManager().getRoot(tempValue.getRootId());
                if (tempValueRoot.getProperty().getType().equals(PropertyEntity.Type.LIST) ||
                    tempValueRoot.getProperty().getType().equals(PropertyEntity.Type.DICTIONARY)) {
                    tags.append(tempValue.getName());
                }
                tags.append(" ");
            }

            if (tags.length() > 512) {
                tags = new StringBuilder(tags.substring(0, 511));
            }

        }

        getServiceManager().getProductManager().save(product, caller);

    }


    /**
     * Предназначено исключительно импорта и других массовых операций
     * Крайне не рекомендуется использовать в UI.
     * <p/>
     * Для ускорений убрана проверка прав.
     *
     * @param valueLinks
     * @param product
     * @param caller
     */
    @Override
    @Transactional
    public void addTagsToProductFast(Collection<TagToValueEntity> valueLinks,
                                     ProductEntity product, UserEntity caller) {
        // если список тегов пуст, выходим
        if (valueLinks.isEmpty()) {
            return;
        }
        propertyDao.addTagsToProductFast(filterTags(valueLinks), product.getRootId(), caller.getRootId());
    }

    /**
     * Фильтрация по уникальности
     *
     * @param valueLinks
     * @return
     */
    protected LinkedList<TagToValueEntity> filterTags(Collection<TagToValueEntity> valueLinks) {
        LinkedList<TagToValueEntity> uniqueTags = new LinkedList<TagToValueEntity>();
        for (TagToValueEntity tag : valueLinks) {
            boolean exists = false;

            // проверяем, что аналогичный тег ещё не содержится в наборе
            for (TagToValueEntity existing : uniqueTags) {
                boolean eqName = tag.getName() == null && existing.getName() == null ||
                                 tag.getName() != null && tag.getName().equals(existing.getName());
                boolean eqVal = tag.getValue() == null && existing.getValue() == null ||
                                tag.getValue() != null && tag.getValue().equals(existing.getValue());
                boolean eqNameVal = eqName && eqVal;
                boolean eqProd = tag.getProduct() != null &&
                                 existing.getProduct() != null &&
                                 tag.getProduct().equals(existing.getProduct());
                boolean eqPropVal = tag.getPropertyValue().equals(existing.getPropertyValue());
                if (existing == tag || eqProd && eqPropVal && (existing.getType() == LIST || eqNameVal)) {
                    exists = true;
                }
            }
            if (!exists) {
                uniqueTags.add(tag);
            }
        }
        return uniqueTags;
    }

    private void internalClearTagsForProduct(ProductEntity product) {

        //Get all tag links for object
        List<TagToValueEntity> tags = tagToValueDao.getByProduct(product);

        //For every link - remove it and decrease tag count in value for tagcloud component
        for (TagToValueEntity tag : tags) {
            tagToValueDao.remove(tag);
        }

        productDao.save(product);

    }

    @Override
    @Transactional
    public void clearTagsForProduct(ProductEntity product, UserEntity caller) {

        //Check grants
        internalClearTagsForProduct(product);

    }

}