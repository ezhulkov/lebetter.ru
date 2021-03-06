package org.ohm.lebetter.tapestry5.web.pages.po.system.category;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;
import org.ohm.lebetter.tapestry5.web.util.datasource.GenericEntityGridDS;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class List extends AdminBasePage {

    @Property
    private CategoryEntity oneCategory;

    @Cached
    public GridDataSource getCategories() {
        java.util.List<Long> ids = getServiceFacade().getCategoryManager().getAllIds(null, "name", null, null);
        return new GenericEntityGridDS<CategoryEntity>(ids, getServiceFacade().getCategoryManager(), CategoryEntity.class);
    }

}
