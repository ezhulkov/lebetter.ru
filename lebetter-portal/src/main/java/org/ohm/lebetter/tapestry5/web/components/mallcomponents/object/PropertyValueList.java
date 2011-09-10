package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.List;


public class PropertyValueList extends AbstractBaseComponent {

    @Property
    @Parameter(name = "property", required = true, allowNull = false)
    private PropertyEntity property;

    @Property
    private PropertyValueEntity oneValue;

    public List<PropertyValueEntity> getValues() {
        List<PropertyValueEntity> result = getServiceFacade().getPropertyManager().getAllValues(property);
        return getServiceFacade().getPropertyValueManager().getTranslated(result);
    }

    public boolean isPropertyDict() {
        return property.getType().equals(PropertyEntity.Type.DICTIONARY);
    }

    public UserEntity getCreator() {
        return (UserEntity) oneValue.getCreator();
    }

}