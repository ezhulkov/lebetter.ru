package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.List;

public class PropertySubTypeList extends AbstractBaseComponent {

    @Property
    private PropertyEntity oneSubProperty;

    @Property
    @Parameter(name = "parent", required = true, allowNull = false)
    private PropertyEntity parentProperty;

    public List<PropertyEntity> getSubCategories() {
        PropertyManager propertyManager = getServiceFacade().getPropertyManager();
        List<PropertyEntity> result = propertyManager.getAllSubProperties(parentProperty);
        return propertyManager.getTranslated(result);
    }

}