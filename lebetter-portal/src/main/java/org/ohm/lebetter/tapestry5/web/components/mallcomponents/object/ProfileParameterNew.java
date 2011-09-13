package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.room13.mallcore.model.ProfileAware;
import org.room13.mallcore.model.impl.entities.ProfileParameterEntity;
import org.room13.mallcore.model.impl.entities.RoleEntity;

public class ProfileParameterNew extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=required,maxlength=128"})
    private TextField nameField;

    @Component(id = "code", parameters = {"value=selectedObject.code", "validate=required,maxlength=32"})
    private TextField codeField;

    @Parameter(allowNull = false, required = true)
    private ProfileAware parent;

    public ProfileParameterEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            ProfileParameterEntity selectedObject =
                    getServiceFacade().getProfileParameterManager().getNewInstance();
            if (parent instanceof UserEntity) {
                selectedObject.setUser((UserEntity) parent);
            } else if (parent instanceof RoleEntity) {
                selectedObject.setRole((RoleEntity) parent);
            }
            setSelectedObjectInternal(selectedObject);
        }
        return (ProfileParameterEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(ProfileParameterEntity object) {
        setSelectedObjectInternal(object);
    }

    public ProfileParameterEntity getSelectedObjectRoot() {
        return (ProfileParameterEntity) getSelectedRootObjectInternal();
    }

    public String getListPage() {
        if (parent instanceof UserEntity) {
            return "/user/" + parent.getRootId();
        } else {
            return "/po/admin/role/" + parent.getRootId();
        }
    }

}