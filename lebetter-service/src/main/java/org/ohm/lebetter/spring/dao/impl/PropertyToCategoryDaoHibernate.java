package org.ohm.lebetter.spring.dao.impl;

import org.ohm.lebetter.model.impl.entities.PropertyToCategoryEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.dao.PropertyToCategoryDao;
import org.room13.mallcore.spring.dao.impl.GenericDaoHibernate;
/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 27.04.2009
 * Time: 15:19:26
 * To change this template use File | Settings | File Templates.
 */
public class PropertyToCategoryDaoHibernate
        extends GenericDaoHibernate<PropertyToCategoryEntity, UserEntity>
        implements PropertyToCategoryDao {

    public PropertyToCategoryDaoHibernate() {
        super(PropertyToCategoryEntity.class);
    }

}