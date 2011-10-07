package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.List;

public class CatalogWithChildren extends AbstractBaseComponent {

    @Property
    @Parameter(required = true, allowNull = false)
    private CategoryEntity selectedCategory;

    @Property
    private CategoryEntity oneCategory;

    @Property
    private CategoryEntity oneSubCategory;

    @Cached
    public List<CategoryEntity> getCategories() {
        return getServiceFacade().getCategoryManager().getAllReadyCategoriesForUI();
    }

}