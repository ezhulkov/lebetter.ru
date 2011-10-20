package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.system;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.room13.mallcore.model.ObjectBaseEntity.Status;

public class ValueNew extends AbstractEditComponent {

    @Parameter(allowNull = false, required = true)
    private PropertyEntity parent;

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=required,maxlength=64"})
    private TextField propertyNameField;

     @Component(id = "code", parameters = {"value=selectedObject.code", "validate=maxlength=32"})
    private TextField propertyCodeField;

    public PropertyValueEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            PropertyValueEntity selectedObject = getServiceFacade().getPropertyValueManager().getNewInstance();
            selectedObject.setProperty(parent);
            selectedObject.setObjectStatus(Status.READY);
            setSelectedObjectInternal(selectedObject);
        }
        return (PropertyValueEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(PropertyValueEntity object) {
        setSelectedObjectInternal(object);
    }

    public PropertyValueEntity getSelectedObjectRoot() {
        return (PropertyValueEntity) getSelectedRootObjectInternal();
    }

}