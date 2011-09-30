package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class CategoryNew extends AbstractEditComponent {

    @Property
    @Parameter(name = "parent", required = false, allowNull = true)
    private CategoryEntity parent;

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=required,maxlength=64"})
    private TextField nameField;

    @Component(id = "code", parameters = {"value=selectedObject.code", "validate=required,maxlength=64"})
    private TextField codeField;

    public CategoryEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            CategoryEntity selectedObject = getServiceFacade().getCategoryManager().getNewInstance();
            if (parent != null) {
                selectedObject.setParent(parent);
            }
            setSelectedObjectInternal(selectedObject);
        }
        return (CategoryEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(CategoryEntity object) {
        setSelectedObjectInternal(object);
    }

    public CategoryEntity getSelectedObjectRoot() {
        return (CategoryEntity) getSelectedRootObjectInternal();
    }

}