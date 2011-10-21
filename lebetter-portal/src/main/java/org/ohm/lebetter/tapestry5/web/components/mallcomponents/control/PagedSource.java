package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 21.10.11
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 07.04.2011
 * Time: 13:25:19
 * To change this template use File | Settings | File Templates.
 */
public class PagedSource<T> implements Iterable<T> {

    private List<T> intSource = new ArrayList<T>();

    private List<T> intPageSource = new ArrayList<T>();

    private Integer intIterableSize;

    public PagedSource(Iterable<T> source) {
        Iterator<T> i = source.iterator();
        while (i.hasNext()) {
            intSource.add(i.next());
        }
    }

    public Iterator<T> iterator() {
        return intPageSource.iterator();
    }

    public int getTotalRowCount() {

        if (intIterableSize != null) {
            return intIterableSize.intValue();
        }

        intIterableSize = 0;

        Iterator<?> it = intSource.iterator();
        while (it.hasNext()) {
            it.next();
            intIterableSize++;
        }

        return intIterableSize.intValue();
    }

    public void prepare(int startIndex, int endIndex) {
        for (int i = startIndex; i <= endIndex; i++) {
            intPageSource.add(intSource.get(i));
        }
    }

}