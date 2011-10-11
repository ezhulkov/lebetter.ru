package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericSelectModel;

import java.util.LinkedList;
import java.util.List;

public class ProductNew extends AbstractEditComponent {

    @Property
    private CategoryEntity category;

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=maxlength=64,required"})
    private TextField propertyNameField;

    @Component(id = "category", parameters = {
            "model=categorySelectModel",
            "encoder=categorySelectModel",
            "value=category",
            "validate=required"})
    private Select categoryNameField;

    @Property
    private GenericSelectModel categorySelectModel = null;

    void onPrepare() throws Exception {
        List<CategoryEntity> readyCategories = new LinkedList<CategoryEntity>();
        List<CategoryEntity> cats = getServiceFacade().getCategoryManager().getAllReadyCategoriesForUI();

        for (CategoryEntity cat : cats) {
            readyCategories.add(cat);
            for (CategoryEntity child : cat.getChildren()) {
                child.setName("---------" + child.getName());
                readyCategories.add(child);
            }
        }

        categorySelectModel = new GenericSelectModel<CategoryEntity>(readyCategories,
                                                                     readyCategories,
                                                                     CategoryEntity.class,
                                                                     "name", "rootId",
                                                                     getIOC().getPropertyAccess());
    }

    public ProductEntity getSelectedObject() {
        if (getSelectedObjectInternal() == null) {
            ProductEntity selectedObject = getServiceFacade().getProductManager().getNewInstance();
            setSelectedObjectInternal(selectedObject);
        }
        return (ProductEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(ProductEntity object) {
        setSelectedObjectInternal(object);
    }

}