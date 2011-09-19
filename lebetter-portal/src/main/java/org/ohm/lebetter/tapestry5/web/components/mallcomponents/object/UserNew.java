package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericSelectModel;
import org.room13.mallcore.model.impl.entities.RoleEntity;

import java.util.List;

public class UserNew extends AbstractEditComponent {

    @Property
    private String pwd1;

    @Property
    private String pwd2;

    @Component(id = "name", parameters = {"value=selectedObject.name",
                                          "validate=required,maxlength=64,email"})
    private TextField propertyNameField;

    @Component(id = "fName", parameters = {"value=selectedObject.firstName",
                                           "validate=maxlength=64"})
    private TextField propertyFNameField;

    @Component(id = "lName", parameters = {"value=selectedObject.lastName",
                                           "validate=maxlength=64"})
    private TextField propertyLNameField;

    @Component(id = "role", parameters = {"model=roleModel",
                                          "encoder=roleModel",
                                          "value=selectedObject.role"})
    private Select roleField;

    @Component(id = "pwd1", parameters = {"value=pwd1"})
    private PasswordField pwd1Field;

    @Component(id = "pwd2", parameters = {"value=pwd2"})
    private PasswordField pwd2Field;

    @Property
    private ValueEncoder<RoleEntity> roleModel = null;

    void onPrepare() throws Exception {
        List<RoleEntity> roles = getServiceFacade().getRoleManager().getAllNonVirtual();

        if (roleModel == null) {
            roleModel = new GenericSelectModel<RoleEntity>(roles, roles,
                                                           RoleEntity.class, "name", "rootId",
                                                           getIOC().getPropertyAccess());
        }
    }

    public UserEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            UserEntity selectedObject = getServiceFacade().getUserManager().getNewInstance();
            setSelectedObjectInternal(selectedObject);
        }
        return (UserEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(UserEntity object) {
        setSelectedObjectInternal(object);
    }

    public UserEntity getSelectedObjectRoot() {
        return (UserEntity) getSelectedRootObjectInternal();
    }

}