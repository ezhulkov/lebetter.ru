package org.ohm.lebetter.spring.service.impl;


import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.ohm.lebetter.Constants;
import org.ohm.lebetter.Constants.AppCacheKeys;
import org.ohm.lebetter.model.impl.entities.ColorEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ColorDao;
import org.ohm.lebetter.spring.service.ColorManager;
import org.room13.mallcore.annotations.AppCache;
import org.room13.mallcore.annotations.AppCacheFlush;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.transaction.annotation.Transactional;

import java.security.AccessControlException;
import java.util.List;

import static org.room13.mallcore.annotations.CacheType.EHCACHE;

public class ColorManagerImpl
        extends KinderGenericManagerImpl<ColorEntity, UserEntity>
        implements ColorManager {

    private ColorDao colorDao;

    public ColorManagerImpl() {
        super(ColorEntity.class);
    }

    @Required
    public void setColorDao(ColorDao dao) {
        super.setGenericDao(dao);
        this.colorDao = dao;
    }

    @Override
    public List<ColorEntity> getLikeColors(ColorEntity color) {
        return colorDao.getLikeColors(color);
    }

    @Override
    @AppCache(cacheType = EHCACHE,
            key = AppCacheKeys.COLORS_BY_CODE,
            langAware = false)
    public ColorEntity getColorByCode(String code) {
        return colorDao.getColorByCode(code);
    }

    @Override
    public ColorEntity getColorByNameIgnoreCase(String color) {
        DetachedCriteria cr = DetachedCriteria.forClass(ColorEntity.class).
                add(Restrictions.ilike("name", color, MatchMode.EXACT));
        return (ColorEntity) DataAccessUtils.uniqueResult(
                colorDao.findRootByCriteria(cr, 0, 1));
    }

    @Override
    public List<ColorEntity> getUIColors() {
        return colorDao.getUIColors();
    }

    @Override
    @Transactional
    @AppCacheFlush(cacheType = EHCACHE, keys = {AppCacheKeys.COLORS_BY_CODE}, langAware = false)
    public void create(ColorEntity color, ColorEntity parent, UserEntity caller) {
        getRMLogger().debug("Create color...");

        //Check grants
        if (!getServiceManager().getRoleManager().isActionGranted(caller,
                Constants.Actions.MANAGE_COLOR,
                null)) {
            getRMLogger().errorSecurityViolation("tries to add new color", color);
            throw new AccessControlException("access denied");
        }

        //Check unique
        if (!colorDao.existsByName(color.getName(), color.getId())) {
            //Init new ctegory
            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Set creator: " + caller);
            }
            color.setCreator(caller);

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Set object status: " + ObjectBaseEntity.Status.READY);
            }
            color.setObjectStatus(ObjectBaseEntity.Status.READY);

            //Create persist object
            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Create color...", color);
            }
            colorDao.create(color);

            //Save it
            saveInternal(color, caller);
            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Color created.");
            }
        } else {
            throw new ObjectExistsException("Color exists exception");
        }
        getRMLogger().debug("Create color completed.");
    }

    @Override
    @Transactional
    @AppCacheFlush(cacheType = EHCACHE, keys = {AppCacheKeys.COLORS_BY_CODE}, langAware = false)
    public void save(ColorEntity color, UserEntity caller) throws ObjectExistsException {
        getRMLogger().debug("Save color...");

        //Check grants
        if (!getServiceManager().getRoleManager().isActionGranted(caller,
                Constants.Actions.MANAGE_COLOR,
                color)) {
            getRMLogger().errorSecurityViolation("Attempt to edit color", color);
            throw new AccessControlException("access denied");
        }

        saveInternal(color, caller);

        getRMLogger().debug("Save color completed.");
    }

    @Override
    @Transactional
    @AppCacheFlush(cacheType = EHCACHE, keys = {AppCacheKeys.COLORS_BY_CODE}, langAware = false)
    public void remove(ColorEntity color, UserEntity caller) {
        getRMLogger().debug("Remove color...");

        if (getServiceManager().getRoleManager().isActionGranted(caller,
                Constants.Actions.MANAGE_COLOR,
                color)) {

            //Get persist object

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Get persist object.", color);
                getRMLogger().debug("Clean all references from folders...");
            }


            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Clean all references from folders complete.");
                getRMLogger().debug("Removing persist...");
            }
            //Remove
            colorDao.safeRemove(color, caller);

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Removing persist complete.");
            }


        } else {
            getRMLogger().errorSecurityViolation("Tries to del color", color);
            throw new AccessControlException("Access denied");
        }
        getRMLogger().debug("Remove color completed.");
    }

    @Override
    @Transactional
    public void saveInternal(ColorEntity color, UserEntity caller) throws ObjectExistsException {

        //Get persist object
        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Get persist object");
        }

        ColorEntity colorPersist = colorDao.get(color.getId());

        if (color.getColorCode() != null) {
            color.setColorCode(color.getColorCode().toUpperCase());
        }
        if (color.getCode() != null) {
            color.setCode(color.getCode().toUpperCase());
        }
        color.setObjectStatus(ObjectBaseEntity.Status.READY);

        //Check unique
        boolean unique = true;
        String k1 = colorPersist.getName() + colorPersist.getCode();
        String k2 = color.getName() + color.getCode();
        if (!k1.equals(k2)) {
            unique = !colorDao.existsByName(color.getName(), color.getId());
        }

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Unique: " + unique);
        }

        if (unique) {
            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Sync categories and set properties to persist " +
                        "object(color, colorPersist, caller): " +
                        "(" + color + ", " + colorPersist + ", " + caller + ")");
            }

            //Set properties to persist object
            BeanUtils.copyProperties(color, colorPersist);

            //Explicit save
            colorDao.save(colorPersist);

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Sync categories and set properties complete.");
            }

        } else {
            throw new ObjectExistsException("Color exists exception");
        }
    }
}
