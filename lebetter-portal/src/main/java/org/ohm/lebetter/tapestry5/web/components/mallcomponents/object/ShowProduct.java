package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.Collections;
import java.util.LinkedList;

public class ShowProduct extends AbstractBaseComponent {

    @Property
    @Parameter(required = true, allowNull = false)
    private CategoryEntity selectedCategory;

    @Property
    @Parameter(required = true, allowNull = false)
    private ProductEntity selectedProduct;

    @Property
    private PropertyValueEntity oneValue;

    @Property
    private PropertyEntity oneProperty;

    @Cached
    public java.util.List<PropertyEntity> getProperties() {
        return getServiceFacade().getCategoryManager().getAllPropertiesForUI(selectedProduct);
    }

    public java.util.List<PropertyValueEntity> getValues() {
        return oneProperty.getValues();
    }

}