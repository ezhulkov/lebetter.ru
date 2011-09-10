package org.ohm.lebetter.tapestry5.web.util.datasource;

import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.GenericManager;

import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ahomyakov
 * Date: 22.01.2010
 * Time: 11:47:23
 * To change this template use File | Settings | File Templates.
 */
public class LanguageAwareDS<T extends ObjectBaseEntity>
        extends GenericEntityDS<T> {

    public LanguageAwareDS(ObjectBaseEntity.Status status, String order,
                           String filter, GenericManager<T, UserEntity> manager) {
        super(status, order, filter, manager, false);
    }

    public LanguageAwareDS(List<Long> ids, GenericManager<T, UserEntity> manager) {
        super(ids, manager, false);
    }

    @Override
    public Iterator<T> iterator() {
        List<T> result;
        if (startIndex > endIndex ||
                startIndex == endIndex && startIndex == 0) {
            result = manager.getAll(ids);
        } else {
            result = manager.getAll(ids, startIndex, endIndex);
        }
        return manager.getTranslated(result).iterator();
    }

}