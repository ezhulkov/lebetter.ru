package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.components.base.EditObjectCallback;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage.Type;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericSelectModel;
import org.room13.mallcore.model.impl.entities.RoleEntity;
import org.room13.mallcore.util.StringUtil;

import java.util.List;

public class UserEdit extends AbstractEditComponent {

    @Property
    private RoleEntity role;

    @Component(id = "email", parameters = {"value=selectedObject.address.email",
                                           "validate=maxlength=64,regexp,required"})
    private TextField propertyNameField;

    @Component(id = "fName", parameters = {"value=selectedObject.firstName",
                                           "validate=maxlength=64"})
    private TextField propertyFNameField;

    @Component(id = "lName", parameters = {"value=selectedObject.lastName",
                                           "validate=maxlength=64"})
    private TextField propertyLNameField;

    @Component(id = "role", parameters = {"model=roleModel",
                                          "encoder=roleModel",
                                          "value=role",
                                          "validate=required"})
    private Select roleField;

    @Component(id = "pwd1", parameters = {"value=selectedObject.confirmPassword1"})
    private PasswordField pwd1Field;

    @Component(id = "pwd2", parameters = {"value=selectedObject.confirmPassword2"})
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
        role = getSelectedObject().getRoles().get(0);
    }

    public UserEntity getSelectedObject() {
        return (UserEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(UserEntity object) {
        setSelectedObjectInternal(object);
    }

    public EditObjectCallback getCallback() {
        return new EditObjectCallback<UserEntity>() {
            @Override
            public boolean onFormSubmit(UserEntity object) throws Exception {
                if (!StringUtil.isEmpty(object.getConfirmPassword1())) {
                    if (!object.getConfirmPassword1().equals(object.getConfirmPassword2())) {
                        getBase().addFlashToSession(getBase().getText("error.pwd"), Type.FAILURE);
                        return false;
                    }
                }
                if (role != null) {
                    object.getRoles().clear();
                    object.getRoles().add(role);
                }
                return true;
            }

            @Override
            public boolean onPostFormSubmit(UserEntity object) throws Exception {
                if (!StringUtil.isEmpty(object.getConfirmPassword1())) {
                    getServiceFacade().getUserManager().setPassword(object, object);
                }
                return true;
            }
        };
    }

}