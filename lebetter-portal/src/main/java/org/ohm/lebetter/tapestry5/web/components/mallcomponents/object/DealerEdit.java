package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class DealerEdit extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name",
                                          "validate=maxlength=64,required"})
    private TextField propertyNameField;

    @Component(id = "discount", parameters = {"value=selectedObject.discount", "validate=maxlength=64"})
    private TextField propertyFNameField;

    @Component(id = "description", parameters = {"value=selectedObject.description", "validate=maxlength=4000"})
    private TextArea descField;

    public DealerEntity getSelectedObject() {
        return (DealerEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(DealerEntity object) {
        setSelectedObjectInternal(object);
    }

}