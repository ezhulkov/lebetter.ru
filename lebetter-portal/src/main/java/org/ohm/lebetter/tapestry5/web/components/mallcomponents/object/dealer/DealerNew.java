package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.dealer;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class DealerNew extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name",
                                          "validate=maxlength=64,required"})
    private TextField propertyNameField;

    @Component(id = "discount", parameters = {"value=selectedObject.discount", "validate=maxlength=64"})
    private TextField propertyFNameField;

    public DealerEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            DealerEntity selectedObject = getServiceFacade().getDealerManager().getNewInstance();
            setSelectedObjectInternal(selectedObject);
        }
        return (DealerEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(DealerEntity object) {
        setSelectedObjectInternal(object);
    }

}