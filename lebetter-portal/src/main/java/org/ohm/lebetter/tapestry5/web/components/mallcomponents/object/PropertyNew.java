package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class PropertyNew extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=required,maxlength=64"})
    private TextField propertyNameField;

    @Component(id = "dictionary", parameters = {"value=selectedObject.dictionary", "validate=maxlength=255"})
    private TextField propertyDictionaryField;

    @Component(id = "type", parameters = {"value=selectedObject.type", "validate=required"})
    private Select propertyTypeField;

    @Cached
    public boolean isSelectedPropertyIsGroup() {
        return getSelectedObject() != null &&
               getSelectedObject().getType().equals(PropertyEntity.Type.GROUP);
    }

    @Cached
    public boolean isSelectedPropertyIsList() {
        return getSelectedObject() != null &&
               getSelectedObject().getType().equals(PropertyEntity.Type.LIST);
    }

    @Cached
    public boolean isSelectedPropertyIsDict() {
        return getSelectedObject() != null &&
               getSelectedObject().getType().equals(PropertyEntity.Type.DICTIONARY);
    }

    public PropertyEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            PropertyEntity selectedObject = getServiceFacade().getPropertyManager().getNewInstance();
            selectedObject.setType(PropertyEntity.Type.VALUE);
            setSelectedObjectInternal(selectedObject);
        }
        return (PropertyEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(PropertyEntity object) {
        setSelectedObjectInternal(object);
    }

    public PropertyEntity getSelectedObjectRoot() {
        return (PropertyEntity) getSelectedRootObjectInternal();
    }

}