package org.ohm.lebetter.tapestry5.web.pages;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBrowseBasePage;

import java.util.List;

public class Sitemap extends AbstractBrowseBasePage {

    @Property
    private CategoryEntity oneCategory;

    @Property
    private CategoryEntity oneSubCategory;

    @Cached
    public List<CategoryEntity> getCategories() {
        return getServiceFacade().getCategoryManager().getAllReadyCategoriesForUI(false);
    }

}