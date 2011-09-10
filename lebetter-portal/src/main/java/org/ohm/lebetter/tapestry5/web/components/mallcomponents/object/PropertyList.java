package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.List;

public class PropertyList extends AbstractBaseComponent {

    @Property
    private PropertyEntity oneProperty;

    @Parameter(name = "parent", required = false, allowNull = true)
    private PropertyEntity parentProperty;

    public boolean isOnePropertyIsGroup() {
        return oneProperty != null &&
                oneProperty.getType().equals(PropertyEntity.Type.GROUP);
    }

    public boolean isOnePropertyIsList() {
        return oneProperty != null &&
                oneProperty.getType().equals(PropertyEntity.Type.LIST);
    }

    public boolean isOnePropertyIsDict() {
        return oneProperty != null &&
                oneProperty.getType().equals(PropertyEntity.Type.DICTIONARY);
    }

    public boolean isOnePropertyIsValue() {
        return oneProperty != null &&
                oneProperty.getType().equals(PropertyEntity.Type.VALUE);
    }

    public boolean isOnePropertyHasStub() {
        return isOnePropertyIsGroup() || isOnePropertyIsValue();
    }

    @Cached
    public List<PropertyEntity> getAllCategories() {
        List<PropertyEntity> result;
        if (parentProperty == null) {
            result = getServiceFacade().getPropertyManager().getAllRootProperties();
        } else {
            result = getServiceFacade().getPropertyManager().getAllSubProperties(parentProperty);
        }

        return getServiceFacade().getPropertyManager().getTranslated(result);
    }

    public PropertyEntity getOnePropertyRoot() {
        return getServiceFacade().getPropertyManager().getRoot(oneProperty.getId());
    }

}