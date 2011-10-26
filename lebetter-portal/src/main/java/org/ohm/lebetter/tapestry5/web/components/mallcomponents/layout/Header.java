package org.ohm.lebetter.tapestry5.web.components.mallcomponents.layout;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 18.03.2009
 * Time: 13:02:41
 * To change this template use File | Settings | File Templates.
 */
public class Header extends AbstractBaseComponent {

    @Property
    private CategoryEntity oneCategory;

    @Property
    private CategoryEntity oneSubCategory;

    @Cached
    public List<CategoryEntity> getCategories() {
        return getServiceFacade().getCategoryManager().getAllReadyCategoriesForUI(false);
    }

}