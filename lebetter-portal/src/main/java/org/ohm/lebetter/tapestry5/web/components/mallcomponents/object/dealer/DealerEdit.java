package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.dealer;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericStatusSelectModel;

public class DealerEdit extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name",
                                          "validate=maxlength=64,required"})
    private TextField propertyNameField;

    @Component(id = "city", parameters = {"value=selectedObject.city",
                                          "validate=maxlength=64,required"})
    private TextField propertyCityField;

    @Component(id = "addressLine", parameters = {"value=selectedObject.addressLine",
                                             "validate=maxlength=128"})
    private TextField propertyAddressField;

    @Component(id = "site", parameters = {"value=selectedObject.site",
                                          "validate=maxlength=64"})
    private TextField propertySiteField;

    @Component(id = "telephone", parameters = {"value=selectedObject.telephone",
                                               "validate=maxlength=64"})
    private TextField propertyTelField;

    @Component(id = "email", parameters = {"value=selectedObject.email",
                                           "validate=maxlength=64,regexp"})
    private TextField propertyeMailField;

    @Component(id = "discount", parameters = {"value=selectedObject.discount", "validate=maxlength=64"})
    private TextField propertyFNameField;

    @Component(id = "description", parameters = {"value=selectedObject.description", "validate=maxlength=4000"})
    private TextArea descField;

    @Component(id = "status", parameters = {
            "model=statusSelectModel",
            "encoder=statusSelectModel",
            "value=selectedObject.objectStatus",
            "validate=required"})
    private Select statusField;

    @Property
    private GenericStatusSelectModel statusSelectModel = null;

    void onPrepare() throws Exception {
        statusSelectModel = new GenericStatusSelectModel(getAuth().getUser(),
                                                         getServiceFacade().getRoleManager(),
                                                         getSelectedObject(),
                                                         getIOC().getPropertyAccess());
    }

    public DealerEntity getSelectedObject() {
        return (DealerEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(DealerEntity object) {
        setSelectedObjectInternal(object);
    }

}