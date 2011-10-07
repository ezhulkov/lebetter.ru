package org.ohm.lebetter.spring.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.Constants.Roles;
import org.ohm.lebetter.model.SitemapAware;
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
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unchecked")
public class CategoryManagerImpl
        extends LBGenericManagerImpl<CategoryEntity, UserEntity>
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

            synchronized (categoryDao) {
                String altId = StringUtil.translit(categoryPersist.getName());
                CategoryEntity prev = (CategoryEntity) getByAltId(altId);
                if (prev != null && !prev.getId().equals(categoryPersist.getId())) {
                    altId += "-" + categoryPersist.getId();
                }
                categoryPersist.setAltId(altId);
                categoryDao.save(categoryPersist);
            }

        } else {
            throw new ObjectExistsException("Type exists exception");
        }


    }

    @Override
    @Transactional
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
    public List<CategoryEntity> getAllReadyLeafCategoriesDistinct() {
        List<CategoryEntity> cats = categoryDao.getAllReadyLeafCategories();
        List<String> names = new ArrayList<String>();
        List<CategoryEntity> result = new ArrayList<CategoryEntity>();
        for (CategoryEntity cat : cats) {
            if (!names.contains(cat.getName())) {
                names.add(cat.getName());
                result.add(cat);
            }
        }
        return result;
    }

    @Override
    public List<CategoryEntity> getAllReadyCategoriesForUI() {
        List<CategoryEntity> result = new LinkedList<CategoryEntity>();
        List<CategoryEntity> roots = getAllReadyCategories(null);
        for (CategoryEntity root : roots) {
            CategoryEntity rootElement = new CategoryEntity();
            rootElement.setId(root.getId());
            rootElement.setRootId(root.getRootId());
            rootElement.setName(root.getName());
            rootElement.setCode(root.getCode());
            List<CategoryEntity> subs = getAllReadyCategories(root);
            List<CategoryEntity> resSubs = new LinkedList<CategoryEntity>();
            for (CategoryEntity sub : subs) {
                CategoryEntity subElement = new CategoryEntity();
                subElement.setParent(rootElement);
                subElement.setId(sub.getId());
                subElement.setRootId(sub.getRootId());
                subElement.setName(sub.getName());
                subElement.setCode(sub.getCode());
                resSubs.add(subElement);
            }
            rootElement.setChildren(resSubs);
            result.add(rootElement);
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
                String cname = cType.getName();
                val.setName(cname + ": " + tag.getValue());
            } else if (type.getType().equals(PropertyEntity.Type.VALUE)) {
                val.setName(tag.getValue());
            } else if (tag.getPropertyValue() != null) {
                String cname = tag.getPropertyValue().getName();
                val.setName(cname);
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
    public void clearProperties(CategoryEntity categoryEntity, UserEntity caller) {

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
    public void setNewSearchChain(CategoryEntity ptype, List<Long> newChain,
                                  List<Long> prevChain, UserEntity caller) {

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
    public List<Long> getAllSubCategoryIdsByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class).
                add(Restrictions.eq("name", name)).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.isNotNull("parent"));

        return categoryDao.findRootProjectionByCriteria(criteria, "id", -1, -1);
    }

    @Override
    public List<CategoryEntity> getAllSubCategoriesByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(CategoryEntity.class).
                add(Restrictions.like("name", "%" + name.toLowerCase() + "%").ignoreCase()).
                add(Restrictions.eq("objectStatus", ObjectBaseEntity.Status.READY)).
                add(Restrictions.isNotNull("parent"));

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

    @Override
    public SitemapAware getByAltId(String altid) {
        return SitemapAwareManagerImpl.getByAltId(altid, categoryDao);
    }
}