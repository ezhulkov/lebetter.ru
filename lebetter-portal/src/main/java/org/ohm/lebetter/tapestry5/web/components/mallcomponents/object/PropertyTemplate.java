package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.Collections;
import java.util.List;

public class PropertyTemplate extends AbstractBaseComponent {

    @Property
    @Parameter(name = "property", required = true, allowNull = false)
    private PropertyEntity selectedProperty = null;

    @Property
    private PropertyValueEntity oneValue;

    @Property
    private PropertyEntity oneProperty;

    public boolean getPropertyIsList() {
        return selectedProperty != null &&
               (selectedProperty.getType().equals(PropertyEntity.Type.DICTIONARY) ||
                selectedProperty.getType().equals(PropertyEntity.Type.LIST));
    }

    public boolean getPropertyIsValue() {
        return selectedProperty != null && selectedProperty.getType().equals(PropertyEntity.Type.VALUE);
    }

    public boolean getPropertyIsGroup() {
        return selectedProperty != null && selectedProperty.getType().equals(PropertyEntity.Type.GROUP);
    }

    public List<PropertyEntity> getSubProperties() {
        PropertyManager propertyManager = getServiceFacade().getPropertyManager();
        return propertyManager.getAllSubProperties(selectedProperty);
    }

    public List<PropertyValueEntity> getListValues() {
        PropertyManager propertyManager = getServiceFacade().getPropertyManager();
        List<PropertyValueEntity> result = propertyManager.getAllValues(selectedProperty);
        if ("C_SIZE".equals(selectedProperty.getCode())) {
            Collections.sort(result, CategoryManager.VALUE_BY_INT_CODE_ENTITY_COMPARATOR);
        } else {
            Collections.sort(result, CategoryManager.VALUE_ENTITY_COMPARATOR);
        }
        return result;
    }

    public boolean isColor() {
        return "Color".equals(selectedProperty.getDictionary());
    }

}