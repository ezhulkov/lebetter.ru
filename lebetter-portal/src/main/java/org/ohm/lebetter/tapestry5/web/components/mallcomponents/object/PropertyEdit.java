package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericStatusSelectModel;

public class PropertyEdit extends AbstractEditComponent {

    @Property
    @Parameter(name = "parent", required = false, allowNull = true)
    private PropertyEntity parentObject;

    @Component(id = "name",
               parameters = {"value=selectedObject.name", "validate=required,maxlength=128"})
    private TextField propertyNameField;

    @Component(id = "multiple",
               parameters = {"value=selectedObject.multiple"})
    private Checkbox propertyMultipleField;

    @Property
    private GenericStatusSelectModel statusSelectModel = null;

    @Component(id = "status", parameters = {
            "model=statusSelectModel",
            "encoder=statusSelectModel",
            "value=selectedObject.objectStatus",
            "validate=required"})
    private Select statusField;

    void setupRender() {
        if (parentObject == null) {
            parentObject = getServiceFacade().getPropertyManager().getNewInstance();
            parentObject.setId(new Long(0));
        }
    }

    public void onPrepare() throws Exception {

        statusSelectModel = new GenericStatusSelectModel(getAuth().getUser(),
                                                         getServiceFacade().getRoleManager(),
                                                         getSelectedObjectRoot(),
                                                         getIOC().getPropertyAccess());
    }

    public String getjsfunctionname() {
        return "propertyType" + parentObject.getId();
    }

    public boolean isSelectedPropertyIsGroup() {
        return getSelectedObjectRoot() != null &&
               getSelectedObjectRoot().getType().equals(PropertyEntity.Type.GROUP);
    }

    public boolean isSelectedPropertyIsList() {
        return getSelectedObjectRoot() != null &&
               getSelectedObjectRoot().getType().equals(PropertyEntity.Type.LIST);
    }

    public boolean isSelectedPropertyIsListOrDict() {
        return isSelectedPropertyIsList() || isSelectedPropertyIsDict();
    }

    public boolean isSelectedPropertyIsDict() {
        return getSelectedObjectRoot() != null &&
               getSelectedObjectRoot().getType().equals(PropertyEntity.Type.DICTIONARY);
    }

    public PropertyEntity getSelectedObject() {
        return (PropertyEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(PropertyEntity object) {
        setSelectedObjectInternal(object);
    }

    public PropertyEntity getSelectedObjectRoot() {
        return (PropertyEntity) getSelectedRootObjectInternal();
    }

}