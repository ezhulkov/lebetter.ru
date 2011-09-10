package org.ohm.lebetter.spring.service;

import org.hibernate.criterion.Order;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.GenericManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 20.10.2010
 * Time: 10:58:13
 * To change this template use File | Settings | File Templates.
 */
public interface ObjectSearchAwareManager<T extends ObjectBaseEntity> extends GenericManager<T, UserEntity> {

    public List<Long> getSearchObjectsIds(CategoryEntity category,
                                          PropertyValueEntity[][] values,
                                          Order sort);

    public int getSearchObjectsIdsCount(CategoryEntity pType,
                                        PropertyValueEntity[][] values);

}
