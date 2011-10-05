package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class PropertyNew extends AbstractEditComponent {

    @Property
    @Parameter(required = false, allowNull = true)
    private PropertyEntity parent;

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=required,maxlength=64"})
    private TextField propertyNameField;

    @Component(id = "dictionary", parameters = {"value=selectedObject.dictionary", "validate=maxlength=255"})
    private TextField propertyDictionaryField;

    @Component(id = "type", parameters = {"value=selectedObject.type", "validate=required"})
    private Select propertyTypeField;

    public PropertyEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            PropertyEntity selectedObject = getServiceFacade().getPropertyManager().getNewInstance();
            selectedObject.setType(PropertyEntity.Type.VALUE);
            selectedObject.setParent(parent);
            setSelectedObjectInternal(selectedObject);
        }
        return (PropertyEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(PropertyEntity object) {
        setSelectedObjectInternal(object);
    }

    public PropertyEntity getSelectedObjectRoot() {
        return (PropertyEntity) getSelectedRootObjectInternal();
    }

}