package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.catalog;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.layout.OfficeLayout;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericStatusSelectModel;

public class CatalogEdit extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name",
                                          "validate=maxlength=64,required"})
    private TextField propertyNameField;

    @Component(id = "dataurl", parameters = {"value=selectedObject.dataurl", "validate=maxlength=32"})
    private TextField dataUrlField;

    @Component(id = "status", parameters = {
            "model=statusSelectModel",
            "encoder=statusSelectModel",
            "value=selectedObject.objectStatus",
            "validate=required"})
    private Select statusField;

    @Property
    private GenericStatusSelectModel statusSelectModel = null;

    @Parameter
    private OfficeLayout office;


    void onPrepare() throws Exception {
        statusSelectModel = new GenericStatusSelectModel(getAuth().getUser(),
                                                         getServiceFacade().getRoleManager(),
                                                         getSelectedObject(),
                                                         getIOC().getPropertyAccess());
    }

    public CatalogEntity getSelectedObject() {
        return (CatalogEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(CatalogEntity object) {
        setSelectedObjectInternal(object);
    }

}