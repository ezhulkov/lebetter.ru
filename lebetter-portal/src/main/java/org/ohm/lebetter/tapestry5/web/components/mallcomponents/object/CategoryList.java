package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.List;


public class CategoryList extends AbstractBaseComponent {

    @Property
    private CategoryEntity oneCategory;

    @Cached
    public List<CategoryEntity> getAllCategories() {
       return getServiceFacade().getCategoryManager().getAllRootCategories();
    }

}