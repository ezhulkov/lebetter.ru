package org.ohm.lebetter.tapestry5.web.util.datasource;

import org.apache.tapestry5.grid.SortConstraint;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.GenericManager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ahomyakov
 * Date: 22.01.2010
 * Time: 11:47:23
 * To change this template use File | Settings | File Templates.
 */
public class LanguageAwareGridDS<T extends ObjectBaseEntity>
        extends GenericEntityGridDS<T> {

    public LanguageAwareGridDS(ObjectBaseEntity.Status status,
                               String order, GenericManager<T, UserEntity> manager, Class persClass) {
        super(manager.getAllIds(status, order), manager, persClass);
    }

    public LanguageAwareGridDS(List<Long> ids, GenericManager<T, UserEntity> manager, Class<T> persClass) {
        super(ids, manager, persClass);
    }

    @Override
    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        pageResult = manager.getTranslated(manager.getAll(ids, startIndex, endIndex));
    }

}