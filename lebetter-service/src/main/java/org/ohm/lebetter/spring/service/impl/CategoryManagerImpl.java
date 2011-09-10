package org.ohm.lebetter.spring.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.services.ContextPathEncoder;
import org.hibernate.Criteria;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.Constants;
import org.ohm.lebetter.Constants.Roles;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyToCategoryEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.CategoryDao;
import org.ohm.lebetter.spring.dao.PropertyToCategoryDao;
import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.room13.mallcore.annotations.AppCache;
import org.room13.mallcore.annotations.AppCacheFlush;
import org.room13.mallcore.annotations.Permission;
import org.room13.mallcore.annotations.PermissionsCheckType;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.room13.mallcore.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.transaction.annotation.Transactional;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.ohm.lebetter.Constants.AppCacheKeys.CATEGORIES;
import static org.ohm.lebetter.Constants.AppCacheKeys.CATEGORIES_BY_NAME;
import static org.room13.mallcore.Constants.AppCacheKeys.OBJECT_SEARCH_COMPONENT_DATA;

@SuppressWarnings("unchecked")
public class CategoryManagerImpl
        extends KinderGenericManagerImpl<CategoryEntity, UserEntity>
        implements CategoryManager {

    private CategoryDao categoryDao;
    private PropertyToCategoryDao propertyToCategoryDao;

    public CategoryManagerImpl() {
        super(CategoryEntity.class);
    }

    @Required
    public void setPropertyToCategoryDao(PropertyToCategoryDao propertyToCategoryDao) {
        this.propertyToCategoryDao = propertyToCategoryDao;
    }

    @Required
    public void setCategoryDao(CategoryDao dao) {
        super.setGenericDao(dao);
        this.categoryDao = dao;
    }

    public ServiceManager getServiceManagerExt() {
        return (ServiceManager) super.getServiceManager();
    }

    @Override
    public List<CategoryEntity> getObjectsCreatedBy(UserEntity creator, String order, String limit) {
        return ObjectCreatorAwareManagerImpl.getObjectsCreatedBy(creator, order, limit, this, categoryDao);
    }

    @Override
    public List<Long> getObjectsIdsCreatedBy(UserEntity creator, String order,
                                             String filter, String limit) {
        return ObjectCreatorAwareManagerImpl.getObjectsIdsCreatedBy(creator, order, filter,
                                                                    limit, categoryDao);
    }

    @Override
    public List<Long> getObjectsIdsCreatedBy(UserEntity creator, String order) {
        return getObjectsIdsCreatedBy(creator, order, null, null);
    }

    @Override
    @Transactional
    @Permission(values = "ADD_", checkType = PermissionsCheckType.ACTION_PREFIX)
    public void create(CategoryEntity category,
                       CategoryEntity parentCategory,
                       UserEntity caller) {

        getRMLogger().debug("Category...", category);

        if (!getServiceManager().getRoleManager().isRoleAssigned(caller, Roles.ROLE_ADMIN)) {
            getRMLogger().errorSecurityViolation("Tries to add new category", category);
            throw new AccessControlException("access denied");
        }

        //Check unique
        if (!categoryDao.exists(category)) {

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Init new category...");
            }

            //Init new category
            category.setCreator(caller);
            if (parentCategory != null) {
                category.setParent(parentCategory);
            }

            //Create persist object
            categoryDao.create(category);

            //Save it
            saveInternal(category, caller);

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Create persist object complete.", category);
            }

        } else {
            throw new ObjectExistsException("PType exists exception");
        }

        getRMLogger().debug("Create category complete.");
    }

    @Override
    @Transactional
    @Permission(values = "DEL_", checkType = PermissionsCheckType.ACTION_PREFIX)
    public void remove(CategoryEntity category, UserEntity caller) {

        getRMLogger().debug("Remove category...");

        if (getServiceManager().getRoleManager().isRoleAssigned(caller, Roles.ROLE_ADMIN)) {

            //Remove
            categoryDao.remove(category);

            //Evict 2nd level cache
            ((AbstractPersistentCollection) category.getProducts()).dirty();
            ((AbstractPersistentCollection) category.getProperties()).dirty();
            ((AbstractPersistentCollection) category.getChildren()).dirty();

        } else {
            getRMLogger().errorSecurityViolation("Tries to del category.", category);
            throw new AccessControlException("access denied");
        }

        getRMLogger().debug("Remove category complete.");
    }

    @Override
    @Transactional
    public void saveInternal(CategoryEntity category, UserEntity caller) throws ObjectExistsException {

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Get persist object...");
        }

        //Get persist object
        CategoryEntity categoryPersist = categoryDao.get(category.getId());

        //Check unique
        boolean unique = true;
        if (!categoryPersist.getName().equals(category.getName())) {
            unique = !categoryDao.exists(category);
        }

        if (unique) {

            //Set properties to persist object
            BeanUtils.copyProperties(category,
                                     categoryPersist,
                                     new String[]{"children",
                                                  "parent",
                                                  "properties"});

            //Explicit save
            categoryDao.save(categoryPersist);

        } else {
            throw new ObjectExistsException("Type exists exception");
        }


    }

    @Override
    @Transactional
    @AppCacheFlush(keys = {CATEGORIES})
    @Permission(values = "EDIT_", checkType = PermissionsCheckType.ACTION_PREFIX)
    public void save(CategoryEntity category, UserEntity caller) throws ObjectExistsException {

        getRMLogger().debug("Save category...", category);

        //Check grants
        if (!getServiceManager().getRoleManager().isRoleAssigned(caller, Roles.ROLE_ADMIN)) {
            getRMLogger().errorSecurityViolation("Tries to edit category.", category);
            throw new AccessControlException("access denied");
        }

        saveInternal(category, caller);

        getRMLogger().debug("Save category complete.");
    }

    @Override
    public List<CategoryEntity> getAllRootCategories() {
        return categoryDao.getAllRootCategories();
    }

    @Override
    public List<CategoryEntity> getAllReadyLeafCategories() {
        return categoryDao.getAllReadyLeafCategories();
    }

    @Override
    public List<CategoryEntity> getAllBrandCategories() {
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class).
                createAlias("brands", "b", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.eq("b.objectStatus", ObjectBaseEntity.Status.READY)).
                addOrder(Order.asc("name")).
                setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        return categoryDao.findRootByCriteria(criteria, -1, -1);
    }

    @Override
    public List<CategoryEntity> getAllReadyLeafCategoriesDistinct() {
        List<CategoryEntity> cats = categoryDao.getAllReadyLeafCategories();
        List<String> names = new ArrayList<String>();
        List<CategoryEntity> result = new ArrayList<CategoryEntity>();
        for (CategoryEntity cat : cats) {
            if (!names.contains(cat.getName()) && !names.contains(cat.getAlias())) {
                names.add(cat.getName());
                if (!StringUtil.isEmpty(cat.getAlias())) {
                    names.add(cat.getAlias());
                }
                result.add(cat);
            }
        }
        return result;
    }

    @Override
    public List<CategoryEntity> getAllReadyCategories(CategoryEntity parent) {
        return parent == null ?
               categoryDao.getAllReadyRootCategories() :
               categoryDao.getAllReadyCategories(parent);
    }

    @Override
    @AppCache(key = Constants.AppCacheKeys.PRODS_IN_CAT_COUNT)
    public List<CategoryEntity> getAllReadyCategoriesForUI(CategoryEntity parent,
                                                           boolean girls,
                                                           boolean boys) {
        List<CategoryEntity> result = getAllReadyCategories(parent);
        for (CategoryEntity categoryEntity : result) {
            String count = categoryEntity.getProductCount();
            if (count != null) {
                try {
                    Properties props = parseInfo(categoryEntity.getProductCount());
                    if (girls) {
                        categoryEntity.setProductCount2(props.getProperty("G"));
                    } else if (boys) {
                        categoryEntity.setProductCount2(props.getProperty("B"));
                    } else {
                        categoryEntity.setProductCount2(props.getProperty("A"));
                    }
                } catch (Exception ex) {
                    getRMLogger().error("", ex);
                }
            }
        }
        return result;
    }

    private Properties parseInfo(String info) {
        Properties result = new Properties();
        if (info != null) {
            String[] pairs = info.split("#");
            for (String pair : pairs) {
                String[] param = pair.split(":");
                if (param.length == 2 && !org.room13.mallcore.news.util.StringUtils.isEmpty(param[1])) {
                    result.put(param[0], param[1]);
                }
            }
        }
        return result;
    }

    @Override
    @AppCache(key = CATEGORIES)
    public List<CategoryEntity> getAllCategories(CategoryEntity parent) {
        return parent == null ?
               categoryDao.getAllRootCategories() :
               categoryDao.getAllCategories(parent);
    }

    @Override
    public long getAllCategoriesCount(CategoryEntity parent) {
        return parent == null ?
               categoryDao.getAllRootCategoriesCount() :
               categoryDao.getAllCategoriesCount(parent);
    }

    @Override
    public List<PropertyEntity> getAllPropertiesForUI(ProductEntity product) {

        Map<String, PropertyEntity> properties = new HashMap<String, PropertyEntity>();
        for (CategoryEntity category : getAllReadyForProduct(product)) {
            for (PropertyEntity catProp : getAllReadyProperties(category)) {
                String propKey = StringUtil.isEmpty(catProp.getAlias()) ?
                                 catProp.getName() : catProp.getAlias();
                PropertyEntity distinctProp = properties.get(propKey);
                if (distinctProp == null) {
                    distinctProp = getServiceManager().getPropertyManager().getNewInstance();
                    distinctProp.setName(catProp.getName());
                    distinctProp.setCode(catProp.getCode());
                    distinctProp.setAlias(catProp.getAlias());
                    distinctProp.setId(catProp.getId());
                    distinctProp.setRootId(catProp.getRootId());
                    distinctProp.setDictionary(catProp.getDictionary());
                    properties.put(propKey, distinctProp);
                }

                List<PropertyValueEntity> vals = getParamValues(catProp, product);
                for (PropertyValueEntity val : vals) {
                    boolean exists = false;
                    for (PropertyValueEntity propertyValueEntity : distinctProp.getValues()) {
                        if (propertyValueEntity.getName().equals(val.getName()) ||
                            !StringUtil.isEmpty(val.getAlias()) &&
                            !StringUtil.isEmpty(propertyValueEntity.getAlias()) &&
                            propertyValueEntity.getAlias().equals(val.getAlias())) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        distinctProp.getValues().add(val);
                    }
                }
            }
        }

        Collection<PropertyEntity> vals = properties.values();
        List<PropertyEntity> result = new LinkedList<PropertyEntity>();
        for (PropertyEntity val : vals) {
            result.add(val);
        }
        Collections.sort(result, PROPERTY_ENTITY_COMPARATOR);

        return result;

    }

    private List<PropertyValueEntity> getParamValues(PropertyEntity property, ProductEntity product) {
        PropertyManager propertyManager = getServiceManager().getPropertyManager();
        List<TagToValueEntity> tags = propertyManager.getTagsForProduct(product, property);
        List<PropertyValueEntity> result = new ArrayList<PropertyValueEntity>();

        for (TagToValueEntity tag : tags) {
            PropertyEntity type = tag.getPropertyValue().getProperty();
            PropertyValueEntity val = new PropertyValueEntity();
            val.setAdditionalDictInfo(tag.getPropertyValue().getAdditionalDictInfo());
            val.setId(tag.getPropertyValue().getId());
            val.setRootId(tag.getPropertyValue().getRootId());
            if (tag.getPropertyValue().getRelation() != null) {
                val.getRelation().setObjectId(tag.getPropertyValue().getRelation().getObjectId());
                val.getRelation().setObjectType(tag.getPropertyValue().getRelation().getObjectType());
            }

            if (type.getParent() != null &&
                type.getParent().getType().equals(PropertyEntity.Type.GROUP)) {
                PropertyEntity cType = tag.getPropertyValue().getProperty();
                String cname = StringUtils.isEmpty(cType.getAlias()) ?
                               cType.getName() : cType.getAlias();
                val.setName(cname + ": " + tag.getValue());
            } else if (type.getType().equals(PropertyEntity.Type.VALUE)) {
                val.setName(tag.getValue());
            } else if (tag.getPropertyValue() != null) {
                String cname = StringUtils.isEmpty(tag.getPropertyValue().getAlias()) ?
                               tag.getPropertyValue().getName() :
                               tag.getPropertyValue().getAlias();
                val.setName(cname);
                val.setCode(tag.getPropertyValue().getCode());
                val.setRootId(tag.getPropertyValue().getRootId());
            }
            result.add(val);
        }
        return result;
    }

    @Override
    public List<PropertyEntity> getAllProperties(CategoryEntity pType) {
        return categoryDao.getAllProperties(pType);
    }

    @Override
    @AppCache(key = Constants.AppCacheKeys.PROPS_FOR_SEARCH)
    public List<PropertyEntity> getAllReadyPropertiesForSearch(CategoryEntity pType, String exclude) {
        return categoryDao.getAllReadyPropertiesForSearch(pType, exclude);
    }

    @Override
    @AppCache(key = Constants.AppCacheKeys.OBJECT_SEARCH_COMPONENT_DATA)
    public List<CategoryEntity> getAllReadyCategoriesTreeForObjectSearch() {
        List<CategoryEntity> result = new ArrayList<CategoryEntity>();

        //For every root type
        List<CategoryEntity> rootPTypes = getAllReadyCategories(null);
        rootPTypes = getTranslated(rootPTypes);
        for (CategoryEntity rootPType : rootPTypes) {

            //Create duplicate
            CategoryEntity rootPTypeDup = new CategoryEntity();
            BeanUtils.copyProperties(rootPType,
                                     rootPTypeDup,
                                     new String[]{"children", "parent", "categories"});
            if (!StringUtils.isEmpty(rootPType.getAlias())) {
                rootPTypeDup.setName(rootPType.getAlias());
            }
            result.add(rootPTypeDup);

            if (getAllReadyCategories(rootPTypeDup).size() != 0) {
                //For every sub type
                List<CategoryEntity> subPTypes = getAllReadyCategories(rootPType);
                subPTypes = getTranslated(subPTypes);
                for (CategoryEntity subPType : subPTypes) {

                    //Create duplicate
                    CategoryEntity subPTypeDup = new CategoryEntity();
                    BeanUtils.copyProperties(subPType,
                                             subPTypeDup,
                                             new String[]{"children", "parent", "categories"});
                    if (!StringUtils.isEmpty(subPType.getAlias())) {
                        subPTypeDup.setName(subPType.getAlias());
                    }
                    subPTypeDup.setParent(rootPTypeDup);
                    rootPTypeDup.getChildren().add(subPTypeDup);

                    processCategories(subPTypeDup);

                }
            } else {

                processCategories(rootPTypeDup);

            }
        }

        return result;
    }

    private void processCategories(CategoryEntity pType) {

        //For every category
        List<PropertyEntity> categories = getAllReadyPropertiesForSearch(pType, null);
        categories = getServiceManagerExt().getPropertyManager().getTranslated(categories);
        for (PropertyEntity category : categories) {

            //Create duplicate
            PropertyEntity categoryDup = new PropertyEntity();
            PropertyToCategoryEntity link = new PropertyToCategoryEntity();
            BeanUtils.copyProperties(category, categoryDup, new String[]{"children", "parent", "values"});
            if (!StringUtils.isEmpty(category.getAlias())) {
                categoryDup.setName(category.getAlias());
            }
            link.setProperty(categoryDup);
            link.setCategory(pType);
            pType.getProperties().add(link);

            //Get every value
            List<PropertyValueEntity> values =
                    getServiceManagerExt().getPropertyManager().getAllReadyValues(category);
            values = getServiceManagerExt().getPropertyValueManager().getTranslated(values);
            for (PropertyValueEntity value : values) {

                //Create duplicate
                PropertyValueEntity valueDup = new PropertyValueEntity();
                BeanUtils.copyProperties(value, valueDup, new String[]{"property", "shingles", "tags"});
                if (!StringUtils.isEmpty(value.getAlias())) {
                    valueDup.setName(value.getAlias());
                }
                valueDup.setProperty(categoryDup);
                categoryDup.getValues().add(valueDup);

            }
        }

    }

    @Override
    public List<PropertyEntity> getAllReadyProperties(CategoryEntity pType) {
        return categoryDao.getAllReadyProperties(pType);
    }

    @Override
    public List<PropertyEntity> getAllReadyPropertiesByType(CategoryEntity pType,
                                                            PropertyEntity.Type type) {
        return categoryDao.getAllReadyPropertiesByType(pType, type);
    }

    @Override
    public List<PropertyEntity> getPropertiesByMultiple(CategoryEntity pType, boolean multiple) {
        return categoryDao.getPropertiesByMultiple(pType, multiple);
    }

    @Override
    @Transactional
    @AppCacheFlush(keys = {Constants.AppCacheKeys.OBJECT_SEARCH_COMPONENT_DATA}, langAware = true)
    @Permission(values = "EDIT_", checkType = PermissionsCheckType.ACTION_PREFIX)
    public void clearProperties(CategoryEntity categoryEntity, UserEntity caller) {

        //Check grants
        if (!getServiceManagerExt().getRoleManager().isActionGranted(caller,
                                                                     Constants.Actions.EDIT_CATEGORY,
                                                                     categoryEntity)) {
            getRMLogger().errorSecurityViolation("Tries to clear properties in category", categoryEntity);
            throw new AccessControlException("access denied");
        }

        CategoryEntity ptypePersist = categoryDao.get(categoryEntity.getId());
        List<PropertyToCategoryEntity> cats = ptypePersist.getProperties();
        if (cats != null) {
            List<PropertyToCategoryEntity> catDup = new ArrayList<PropertyToCategoryEntity>();
            catDup.addAll(cats);
            for (PropertyToCategoryEntity cat : catDup) {
                cat.getProperty().getCategories().remove(cat);
                cat.getCategory().getProperties().remove(cat);
                propertyToCategoryDao.remove(cat);
            }
        }
        categoryEntity.setProperties(null);

    }


    @Override
    @Transactional
    public void createLink(PropertyToCategoryEntity link) {
        propertyToCategoryDao.create(link);
    }

    @Override
    @Transactional
    @AppCacheFlush(keys = {OBJECT_SEARCH_COMPONENT_DATA}, langAware = true)
    @SuppressWarnings("unchecked")
    public void setNewSearchChain(CategoryEntity ptype, List<Long> newChain,
                                  List<Long> prevChain, UserEntity caller) {

        //Check grants
        if (!getServiceManagerExt().getRoleManager().isActionGranted(caller,
                                                                     Constants.Actions.EDIT_CATEGORY,
                                                                     ptype)) {
            getRMLogger().errorSecurityViolation("Tries to edit category", ptype);
            throw new AccessControlException("access denied");
        }

        if (!CollectionUtils.isEqualCollection(newChain, prevChain)) {
            //Intersect collections to get values that has not been changed
            List<Long> immCatsIds = new ArrayList<Long>();
            for (Long el : prevChain) {
                if (newChain.contains(el)) {
                    immCatsIds.add(el);
                }
            }
            //Subtract old from new to get only new values
            List<Long> newCatsIds = (List<Long>) CollectionUtils.subtract(newChain, prevChain);
            //Unite collections to get proper order
            newChain = new ArrayList<Long>(immCatsIds.size() + newCatsIds.size());
            newChain.addAll(immCatsIds);
            newChain.addAll(newCatsIds);

        } else {
            newChain = prevChain;
        }

        //Clear old
        clearProperties(ptype, caller);

        //Set new order
        CategoryEntity ptypePersist = categoryDao.get(ptype.getId());
        List<PropertyToCategoryEntity> newOrder = new LinkedList<PropertyToCategoryEntity>();
        int itemPos = 0;
        for (Long id : newChain) {
            PropertyEntity cat = getServiceManagerExt().getPropertyManager().get(id);
            PropertyToCategoryEntity link = new PropertyToCategoryEntity();
            link.setProperty(cat);
            link.setCategory(ptypePersist);
            link.setPosition(itemPos++);
            propertyToCategoryDao.create(link);
            newOrder.add(link);
        }
        ptypePersist.setProperties(newOrder);

    }

    @Override
    public String flattenProperties(ProductEntity product) {
        StringBuffer result = new StringBuffer();
        List<TagToValueEntity> tags =
                getServiceManagerExt().getPropertyManager().getTagsForProduct(product);
        if (tags.size() != 0) {
            for (TagToValueEntity tag : tags) {
                result.append(tag.getPropertyValue().getRootId()).append(",;");
            }
        }
        return result.toString();
    }

    @Override
    public String flattenProperties(PropertyValueEntity[][] values) {
        StringBuffer result = new StringBuffer();
        for (PropertyValueEntity[] value : values) {
            for (PropertyValueEntity propertyValueEntity : value) {
                result.append(propertyValueEntity.getRootId()).append(",");
            }
            result.append(";");
        }
        return result.toString();
    }

    @Override
    public String flattenProperties(String[][] values) {
        StringBuffer result = new StringBuffer();
        if (values == null) {
            return null;
        }
        for (String[] value : values) {
            for (String propertyValueEntity : value) {
                result.append(propertyValueEntity).append(",");
            }
            result.append(";");
        }
        return result.toString();
    }

    @Override
    public String flattenProperties(List<String> values) {
        StringBuffer result = new StringBuffer();
        for (String value : values) {
            result.append(value).append(",;");
        }
        return result.toString();
    }

    @Override
    public String createFilterContextURL(String productId,
                                         String category,
                                         List<String> values,
                                         ContextPathEncoder encoder) {
        return encoder.encodeIntoPath(new Object[]{productId,
                                                   category,
                                                   flattenProperties(values)});
    }

    @Override
    public List<Long> getAllSubCategoryIdsByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class).
                add(Restrictions.eq("name", name)).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.isNotNull("parent"));

        return categoryDao.findRootProjectionByCriteria(criteria, "id", -1, -1);
    }

    @Override
    @AppCache(key = CATEGORIES_BY_NAME, langAware = true)
    public List<CategoryEntity> getAllSubCategoriesByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class).
                add(Restrictions.like("name", "%" + name.toLowerCase() + "%").ignoreCase()).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.isNotNull("parent"));

        return categoryDao.findRootByCriteria(criteria, -1, -1);
    }

    @Override
    public List<CategoryEntity> getRelativeCategories(CategoryEntity category) {
        if (StringUtils.isEmpty(category.getRelCode())) {
            return new ArrayList<CategoryEntity>();
        }
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class);
        criteria.add(Restrictions.eq("relCode", category.getRelCode()));
        criteria.createAlias("parent", "par", CriteriaSpecification.INNER_JOIN);
        criteria.addOrder(Order.asc("par.position"));
        return categoryDao.findRootByCriteria(criteria, -1, -1);
    }

    @Override
    public CategoryEntity getTopCategoryByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class).
                add(Restrictions.eq("name", name)).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.isNull("parent"));

        return (CategoryEntity) DataAccessUtils.uniqueResult(
                categoryDao.findRootByCriteria(criteria, -1, -1));
    }

    @Override
    public List<CategoryEntity> getAllForProduct(ProductEntity product) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class).
                createAlias("products", "prod", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.isNotNull("parent")).
                add(Restrictions.eq("prod.id", product.getId()));
        return categoryDao.findRootByCriteria(criteria, -1, -1);
    }

    @Override
    public boolean hasCategories(ProductEntity productEntity) {
        return categoryDao.hasCategories(productEntity.getRootId());
    }

    public List<CategoryEntity> getAllReadyForProduct(ProductEntity product) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class).
                createAlias("products", "prod", CriteriaSpecification.INNER_JOIN).
                add(Restrictions.isNotNull("parent")).
                add(Restrictions.eq("prod.id", product.getId())).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY));
        return categoryDao.findRootByCriteria(criteria, -1, -1);
    }
}