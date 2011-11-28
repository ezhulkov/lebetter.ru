package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.catalog;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class CatalogNew extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=maxlength=64,required"})
    private TextField propertyNameField;

    public CatalogEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            CatalogEntity selectedObject = getServiceFacade().getCatalogManager().getNewInstance();
            setSelectedObjectInternal(selectedObject);
        }
        return (CatalogEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(CatalogEntity object) {
        setSelectedObjectInternal(object);
    }

}