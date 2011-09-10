package org.ohm.lebetter.tapestry5.web.util.datasource;

import org.apache.tapestry5.grid.GridDataSource;
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
public class GenericEntityGridDS<T extends ObjectBaseEntity>
        implements GridDataSource {

    protected Class<T> persClass;
    protected final GenericManager<T, UserEntity> manager;
    protected final List<Long> ids;
    protected List<T> pageResult;
    protected int startIndex;
    protected int endIndex;

    public GenericEntityGridDS(ObjectBaseEntity.Status status, String order,
                               GenericManager<T, UserEntity> manager, Class persClass) {
        this(manager.getAllIds(status, order), manager, persClass);
    }

    public GenericEntityGridDS(List<Long> ids, GenericManager<T, UserEntity> manager, Class<T> persClass) {
        this.persClass = persClass;
        this.ids = ids;
        this.manager = manager;
    }

    @Override
    public int getAvailableRows() {
        return ids.size();
    }

    @Override
    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        pageResult = manager.getAll(ids, startIndex, endIndex);
    }

    @Override
    public Object getRowValue(int index) {
        return pageResult == null ? null : pageResult.get(index - startIndex);
    }

    @Override
    public Class getRowType() {
        return persClass;
    }
}