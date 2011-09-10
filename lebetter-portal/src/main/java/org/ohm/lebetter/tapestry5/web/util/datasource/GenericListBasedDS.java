package org.ohm.lebetter.tapestry5.web.util.datasource;

import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.tapestry5.web.components.mallcomponents.control.PagedSource;

import java.util.Iterator;
import java.util.List;

public class GenericListBasedDS<T extends ObjectBaseEntity>
        extends PagedSource<T>
        implements Cloneable {

    protected int startIndex = 0;
    protected int endIndex = 0;
    protected List<T> src;
    protected List<T> pageSrc;

    public GenericListBasedDS(List<T> src) {
        super(src);
        this.src = src;
    }

    @Override
    public Iterator<T> iterator() {
        return pageSrc.iterator();
    }

    @Override
    public int getTotalRowCount() {
        return src.size();
    }

    @Override
    public void prepare(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        pageSrc = src.subList(startIndex, Math.min(endIndex + 1, getTotalRowCount()));
    }
}