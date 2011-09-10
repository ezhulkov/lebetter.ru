package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.List;


public class CategorySubList extends AbstractBaseComponent {

    @Property
    private CategoryEntity oneCategory;

    @Property
    @Parameter(name = "parent", required = true, allowNull = false)
    private CategoryEntity parent = null;

    @Cached
    public List<CategoryEntity> getAllCategories() {
        CategoryManager categoryManager = getServiceFacade().getCategoryManager();
        List<CategoryEntity> result = categoryManager.getAllCategories(parent);
        return categoryManager.getTranslated(result);
    }

}