package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.ColorEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class ColorEdit extends AbstractEditComponent {

    @Property
    @Parameter(name = "create", required = false, allowNull = false)
    private boolean newObject = false;

    @Component(id = "name",
               parameters = {"value=selectedObject.name", "validate=required,maxlength=64"})
    private TextField nameField;

    @Component(id = "code",
               parameters = {"value=selectedObject.code", "validate=maxlength=16"})
    private TextField codeField;

    @Component(id = "ccode",
               parameters = {"value=selectedObject.colorCode", "validate=required,maxlength=7,regexp"})
    private TextField ccodeField;

    public void onPrepare() throws Exception {
    }

    public ColorEntity getSelectedObject() {
        if (newObject && getSelectedObjectInternal() == null) {
            setSelectedObjectInternal(getServiceFacade().getColorManager().getNewInstance());
        }
        return (ColorEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(ColorEntity object) {
        setSelectedObjectInternal(object);
    }

    public ColorEntity getSelectedObjectRoot() {
        return (ColorEntity) getSelectedRootObjectInternal();
    }


}