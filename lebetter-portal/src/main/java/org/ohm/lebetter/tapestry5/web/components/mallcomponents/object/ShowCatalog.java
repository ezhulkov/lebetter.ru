package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

public class ShowCatalog extends AbstractBaseComponent {

    @Property
    @Parameter(required = true, allowNull = false)
    private CategoryEntity selectedCategory;

}