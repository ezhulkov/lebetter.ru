package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.room13.mallcore.spring.service.ObjectExistsException;

import java.security.AccessControlException;


public class PropertyValueAdd extends AbstractBaseComponent {

    @Property
    @Parameter(name = "property", required = true, allowNull = false)
    private PropertyEntity property;

    @Property
    @Persist
    private PropertyValueEntity newPropertyValue;

    @Component(id = "propertyValue",
            parameters = {"value=newPropertyValue.name", "validate=required,maxlength=32"})
    private TextField propertyValueField;

    void beginRender() {
        newPropertyValue = new PropertyValueEntity();
    }

    @OnEvent(value = "submit", component = "newValueForm")
    public void onNewValueFormSubmit(Long propertyId) {

        property = getServiceFacade().getPropertyManager().get(propertyId);

        //Try to add new property value
        try {
            newPropertyValue.setProperty(property);
            getServiceFacade().getPropertyValueManager().create(newPropertyValue, null, getAuth().getUser());
        } catch (ObjectExistsException ex) {
            getRMLogger().errorObjectExists("Object already exists", newPropertyValue);
            getBase().addFlashToSession(getBase().getText("error.property.value.exists"),
                    FlashMessage.Type.FAILURE);
            return;
        } catch (AccessControlException ace) {
            getRMLogger().errorSecurityViolation("Access denied", newPropertyValue);
            getBase().addFlashToSession(getBase().getText("error.action.not.granted"),
                    FlashMessage.Type.FAILURE);
            return;
        }

    }

}