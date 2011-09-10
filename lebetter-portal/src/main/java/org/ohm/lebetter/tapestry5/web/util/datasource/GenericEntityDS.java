package org.ohm.lebetter.tapestry5.web.util.datasource;

import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.tapestry5.web.components.mallcomponents.control.PagedSource;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ahomyakov
 * Date: 22.01.2010
 * Time: 11:47:23
 * To change this template use File | Settings | File Templates.
 */
public class GenericEntityDS<T extends ObjectBaseEntity>
        extends PagedSource<T>
        implements Cloneable {

    protected final GenericManager<T, UserEntity> manager;
    protected final List<Long> ids;
    protected int startIndex = 0;
    protected int endIndex = 0;
    protected boolean fetchImages = false;

    public GenericEntityDS(ObjectBaseEntity.Status status, String order,
                           String filter, GenericManager<T, UserEntity> manager,
                           boolean fetchImageStatus) {
        this(manager.getAllIds(status, order, filter, null), manager, fetchImageStatus);
    }

    public GenericEntityDS(List<Long> ids,
                           GenericManager<T, UserEntity> manager,
                           boolean fetchImageStatus) {
        super(Collections.<T>emptyList());
        this.ids = ids;
        this.manager = manager;
    }

    @Override
    public Iterator<T> iterator() {
        List<T> entities = manager.getAll(ids, startIndex, endIndex);
        return entities.iterator();
    }

    @Override
    public int getTotalRowCount() {
        return ids == null ? 0 : ids.size();
    }

    @Override
    public void prepare(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}