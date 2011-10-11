package org.ohm.lebetter.tapestry5.web.pages.po.catalog;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
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
    private ProductEntity oneProduct;

    @Property
    private CategoryEntity selectedCategory;

    public void onActivate(Long cid) {
        selectedCategory = getServiceFacade().getCategoryManager().get(cid);
    }

    public Long onPassivate() {
        return selectedCategory == null ? null : selectedCategory.getRootId();
    }

    @Cached
    public GridDataSource getProducts() {
        java.util.List<Long> ids = getServiceFacade().getProductManager().getIdsByCategory(selectedCategory);
        return new GenericEntityGridDS<ProductEntity>(ids,
                                                      getServiceFacade().getProductManager(),
                                                      ProductEntity.class);
    }

    public CategoryEntity getProductCategory() {
        return oneProduct.getCategories() == null ? null :
               (oneProduct.getCategories().size() == 0 ? null :
                (oneProduct.getCategories().get(0)));
    }

}
