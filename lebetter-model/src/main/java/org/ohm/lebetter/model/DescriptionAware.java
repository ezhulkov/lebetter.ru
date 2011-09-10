package org.ohm.lebetter.model;

import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.room13.mallcore.model.ObjectBaseEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 09.12.2009
 * Time: 11:14:10
 * To change this template use File | Settings | File Templates.
 */
public interface DescriptionAware extends ObjectBaseEntity {

    public List<CategoryEntity> getCategories();

}
