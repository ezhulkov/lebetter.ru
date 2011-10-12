package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericStatusSelectModel;

public class ProductEdit extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name",
                                          "validate=maxlength=64,required"})
    private TextField propertyNameField;

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

    public ProductEntity getSelectedObject() {
        return (ProductEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(ProductEntity object) {
        setSelectedObjectInternal(object);
    }

}