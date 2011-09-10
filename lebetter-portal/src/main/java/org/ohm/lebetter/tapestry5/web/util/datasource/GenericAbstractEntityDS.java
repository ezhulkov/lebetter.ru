package org.ohm.lebetter.tapestry5.web.util.datasource;

import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.services.ServiceFacade;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.tapestry5.web.components.mallcomponents.control.PagedSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GenericAbstractEntityDS<T extends ObjectBaseEntity, MV extends Serializable>
        extends PagedSource<T>
        implements Cloneable {

    protected final Map<Long, MV> map;
    protected final ServiceFacade serviceFacade;
    protected int startIndex = 0;
    protected int endIndex = 0;
    private String sortType;

    public GenericAbstractEntityDS(Map<Long, MV> map, ServiceFacade serviceFacade) {
        super(Collections.<T>emptyList());
        this.map = map;
        this.serviceFacade = serviceFacade;
    }

    public GenericAbstractEntityDS(Map<Long, MV> map, ServiceFacade serviceFacade, String sortType) {
        super(Collections.<T>emptyList());
        this.map = map;
        this.serviceFacade = serviceFacade;
        this.sortType = sortType;
    }

    protected abstract T getValue(MV entityType, Long entityKey);

    @Override
    public Iterator<T> iterator() {
        List<T> resList = new ArrayList<T>(endIndex - startIndex + 1);
        Set<Long> keys = map.keySet();
        int count = 0;
        for (Long key : keys) {
            if (count >= endIndex + 1) {
                break;
            }
            if (count >= startIndex) {
                MV entityType = map.get(key);
                resList.add(getValue(entityType, key));
            }
            count++;
        }
        if (sortType != null && (sortType.equals("namef") || sortType.equals("rating"))) {
            sort(resList, sortType);
        }
        return resList.iterator();
    }

    private void sort(List<T> list, final String sortType) {
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                String f1 = getType(o1);
                String f2 = getType(o2);

                if (sortType.equals("namef")) {
                    return f1.compareToIgnoreCase(f2);
                } else {
                    return f2.compareTo(f1);
                }
            }

            private String getType(Object obj) {
                if (obj instanceof UserEntity) {
                    return ((UserEntity) obj).getName();
                } else {
                    return ((ObjectBaseEntity) obj).getName();
                }
            }

        });
    }

    @Override
    public int getTotalRowCount() {
        return map.size();
    }

    @Override
    public void prepare(int startIndex, int endIndex) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

}