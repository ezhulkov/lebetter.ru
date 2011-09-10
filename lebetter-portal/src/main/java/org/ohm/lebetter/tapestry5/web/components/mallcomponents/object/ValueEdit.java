package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class ValueEdit extends AbstractEditComponent {

    @Component(id = "name",
               parameters = {"value=selectedObject.name", "validate=required,maxlength=128"})
    private TextField nameField;

    @Component(id = "code",
               parameters = {"value=selectedObject.code", "validate=maxlength=8"})
    private TextField propertyCodeField;

    @Component(id = "alias",
               parameters = {"value=selectedObject.alias", "validate=maxlength=128"})
    private TextField aliasField;

    @Component(id = "description",
               parameters = {"value=selectedObject.description", "validate=maxlength=1000"})
    private TextArea descriptionField;

    @Component(id = "attention", parameters = {"value=selectedObject.uiTagAttention"})
    private Checkbox attentionField;

    @Component(id = "mandatory", parameters = {"value=selectedObject.uiTagMandatory"})
    private Checkbox mandatoryField;

    public PropertyValueEntity getSelectedObject() {
        return (PropertyValueEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(PropertyValueEntity object) {
        setSelectedObjectInternal(object);
    }

    public PropertyValueEntity getSelectedObjectRoot() {
        return (PropertyValueEntity) getSelectedRootObjectInternal();
    }

    public boolean getShowControls() {
        return !(getSelectedObject().getProperty() != null &&
                 getSelectedObject().getProperty().getType().equals(PropertyEntity.Type.DICTIONARY));
    }

}