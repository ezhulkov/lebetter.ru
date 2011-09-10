package org.ohm.lebetter.tapestry5.web.pages.auth;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 20.03.2009
 * Time: 17:24:04
 * To change this template use File | Settings | File Templates.
 */
public class PasswordForgot extends AbstractBasePage {

    @Property
    private String emailStr;

    @Component(id = "email", parameters = {"value=emailStr",
                                           "validate=maxlength=128,email,required"})
    private TextField emailField;

    @Property
    private boolean badEmail;

    public void onActivate(String error) {
        if (error.equals("be")) {
            badEmail = true;
        }
    }

    public String onPassivate() {

        if (badEmail) {
            return "be";
        } else {
            return null;
        }

    }

    public Object onSuccess() throws IOException {

        UserEntity user = (UserEntity) getServiceFacade().getUserManager().getUserByUsername(emailStr);
        getRMLogger().info("Entered password forgot method", user);

        if (user != null) {

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Generating new password", user);
            }

            //Generate and create new password
            getServiceFacade().getUserManager().generateNewPassword(user, null);
            
            getRMLogger().info("Password changed", user);

            return PasswordForgotRes.class;

        } else {
            badEmail = true;
        }

        return PasswordForgot.class;
    }

}