package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.ColorEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.ColorDao;
import org.room13.mallcore.spring.dao.impl.CreatorAwareDaoHibernate;

import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 27.04.2009
 * Time: 15:19:26
 * To change this template use File | Settings | File Templates.
 */
public class ColorDaoHibernate
        extends CreatorAwareDaoHibernate<ColorEntity, UserEntity>
        implements ColorDao {

    public ColorDaoHibernate() {
        super(ColorEntity.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ColorEntity> getLikeColors(ColorEntity color) {
        if (getLogger().isTraceEnabled()) {
            getLogger().trace("ColorDaoHibernate.getLikeColors. Input: " + color);
        }
        List<ColorEntity> list = getHibernateTemplate().find("from ColorEntity where " +
                "objectDeleted=false and " +
                "likeColors.rootId=? and " +
                "likeColors.rootObject=true and " +
                "rootObject=true order by code desc", color.getId());
        if (list != null && getLogger().isTraceEnabled()) {
            getLogger().trace("ColorDaoHibernate.getLikeColors. Size: " + list.size());
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ColorEntity> getUIColors() {
        List<ColorEntity> list = getHibernateTemplate().find("from ColorEntity where " +
                "objectDeleted=false and uiColor=true and rootObject=true order by code desc");
        if (list != null && getLogger().isTraceEnabled()) {
            getLogger().trace("ColorDaoHibernate.getUIColors. Size: " + list.size());
        }
        return list;
    }

    @Override
    public ColorEntity getColorByCode(String code) {
        if (getLogger().isTraceEnabled()) {
            getLogger().trace("ColorDaoHibernate.getColorByCode. Input: " + code);
        }
        List colors = getHibernateTemplate().find("from ColorEntity where code=? and " +
                "objectDeleted=false and rootObject=true", code);

        if (colors == null || colors.isEmpty()) {
            if (getLogger().isTraceEnabled()) {
                getLogger().trace("ColorDaoHibernate.getColorByCode. Objects not found.");
            }
            return null;
        } else {
            if (getLogger().isTraceEnabled()) {
                getLogger().trace("ColorDaoHibernate.getColorByCode. Size: " +
                        colors.size() + ", object: " + ((ColorEntity) colors.get(0)).getId());
            }
            return (ColorEntity) colors.get(0);
        }
    }

    @Override
    public boolean existsByName(String name, Long id) {
        if (getLogger().isTraceEnabled()) {
            getLogger().trace("ColorDaoHibernate.existsByCode. Input: " + name + ", " + id);
        }
        boolean res;
        if (id != null) {
            res = !getHibernateTemplate().find("select rootId " +
                    "from ColorEntity where lower(name) = ? and objectDeleted=false and id != ? and " +
                    "rootObject=true", new Object[]{name.toLowerCase(), id}).isEmpty();
        } else {
            res = !getHibernateTemplate().find("select rootId " +
                    "from ColorEntity where lower(name) = ? and objectDeleted=false and " +
                    "rootObject=true", name.toLowerCase()).isEmpty();
        }
        if (getLogger().isTraceEnabled()) {
            getLogger().trace("ColorDaoHibernate.existsByCode. Result: " + res);
        }
        return res;
    }
}