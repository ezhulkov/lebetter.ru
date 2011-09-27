package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.ohm.lebetter.tapestry5.web.pages.po.system.property.List;
import org.room13.mallcore.spring.service.ObjectExistsException;

import java.security.AccessControlException;

public class PropertySubTypeAdd extends AbstractBaseComponent {

    @Property
    @Parameter(name = "parent", required = true, allowNull = false)
    private PropertyEntity parentProperty;

    @Property
    private PropertyEntity selectedProperty = null;

    @Component(id = "propertyName",
            parameters = {"value=selectedProperty.name", "validate=required,maxlength=32"})
    private TextField propertyNameField;

    @Component(id = "propertyDescription",
            parameters = {"value=selectedProperty.description", "validate=maxlength=255"})
    private TextArea propertyDescriptionField;

    public void onPrepare() throws Exception {
        selectedProperty = new PropertyEntity();
    }

    @OnEvent(value = "cancelButton", component = "button")
    Object onCancel() {
        return List.class;
    }

    @OnEvent(value = "submit", component = "propertyForm")
    public void onSavePropertySubmit(Long parentPropertyId) {

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Entered add property subtype method. Parent cat: " + parentPropertyId);
        }

        PropertyManager propertyManager = getServiceFacade().getPropertyManager();
        parentProperty = propertyManager.get(parentPropertyId);

        selectedProperty.setType(PropertyEntity.Type.VALUE);
        selectedProperty.setMultiple(false);
        selectedProperty.setObjectStatus(PropertyEntity.Status.READY);
        selectedProperty.setParent(parentProperty);

        //try to create selected ptype
        try {
            propertyManager.create(selectedProperty, parentProperty, getAuth().getUser());
        } catch (ObjectExistsException ex) {
            getRMLogger().errorObjectExists("Object already exists", selectedProperty);
            getBase().addFlashToSession(getBase().getText("error.property.type.exists"),
                    FlashMessage.Type.FAILURE);
            return;
        } catch (AccessControlException ace) {
            getRMLogger().errorSecurityViolation("Access denied", selectedProperty);
            getBase().addFlashToSession(getBase().getText("error.action.not.granted"),
                    FlashMessage.Type.FAILURE);
            return;
        }

        getBase().addFlashToSession(getBase().getText("property.type.added"), FlashMessage.Type.SUCCESS);
    }


    @OnEvent(value = "delButton", component = "buttondel")
    public Object onDelSubmit() {

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Entered del property subtype method", selectedProperty);
        }

        try {
            getServiceFacade().getPropertyManager().remove(selectedProperty, getAuth().getUser());
        } catch (AccessControlException acex) {
            getRMLogger().errorSecurityViolation("Access denied", selectedProperty);
            getBase().addFlashToSession(getBase().getText("error.access.denied"), FlashMessage.Type.FAILURE);
        }

        getBase().addFlashToSession(getBase().getText("property.type.removed"), FlashMessage.Type.SUCCESS);

        return List.class;
    }

}