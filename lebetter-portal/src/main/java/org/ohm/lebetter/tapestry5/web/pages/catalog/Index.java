package org.ohm.lebetter.tapestry5.web.pages.catalog;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBrowseBasePage;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 13.09.11
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class Index extends AbstractBrowseBasePage {

    @Property
    private CategoryEntity selectedCategory = null;

    @Property
    private CategoryEntity oneSubCategory;

    public void onActivate() {
        onActivate(null);
    }

    public void onActivate(Long cid) {
        if (selectedCategory == null) {
            if (cid == null) {
                List<CategoryEntity> rootCats = getServiceFacade().getCategoryManager().getAllReadyCategories(null);
                if (rootCats.size() != 0) {
                    selectedCategory = rootCats.get(0);
                }
            } else {
                selectedCategory = getServiceFacade().getCategoryManager().get(cid);
            }
        }
    }

    public Long onPassivate() {
        return selectedCategory == null ? null : selectedCategory.getRootId();
    }

    @Cached
    public List<CategoryEntity> getSubCategories() {
        return getServiceFacade().getCategoryManager().getAllReadyCategories(selectedCategory);
    }

}