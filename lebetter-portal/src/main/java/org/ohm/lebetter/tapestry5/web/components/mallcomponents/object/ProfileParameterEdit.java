package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.room13.mallcore.model.impl.entities.ProfileParameterEntity;

public class ProfileParameterEdit extends AbstractEditComponent {

    @Component(id = "name",
            parameters = {"value=selectedObject.name", "validate=required,maxlength=128"})
    private TextField nameField;

    @Component(id = "code",
            parameters = {"value=selectedObject.code", "validate=required,maxlength=32"})
    private TextField codeField;

    @Component(id = "value",
            parameters = {"value=selectedObject.value", "validate=maxlength=255"})
    private TextField valueField;

    public ProfileParameterEntity getSelectedObject() {
        return (ProfileParameterEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(ProfileParameterEntity object) {
        setSelectedObjectInternal(object);
    }

    public ProfileParameterEntity getSelectedObjectRoot() {
        return (ProfileParameterEntity) getSelectedRootObjectInternal();
    }

    public String getListPage() {

        if (getSelectedObject().getUser() != null) {
            return "/user/" + getSelectedObject().getUser().getRootId();
        } else {
            return "/po/admin/role/" + getSelectedObject().getRole().getRootId();
        }
    }

}