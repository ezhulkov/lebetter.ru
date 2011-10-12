package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.components.base.EditObjectCallback;

import java.util.List;

public class ProductNew extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=maxlength=64,required"})
    private TextField propertyNameField;

    @Property
    private CategoryEntity oneCategory;

    @Property
    private CategoryEntity oneSubCategory;

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

    @Cached
    public List<CategoryEntity> getCategories() {
        return getServiceFacade().getCategoryManager().getAllReadyCategoriesForUI();
    }

    public EditObjectCallback getCallback() {
        return new EditObjectCallback<ProductEntity>() {
            @Override
            public boolean onFormSubmit(ProductEntity object) throws Exception {
                Long cid = Long.parseLong(getIOC().getRequest().getParameter("LB-SEL-CAT"));
                CategoryEntity category = getServiceFacade().getCategoryManager().get(cid);
                object.getCategories().add(category);
                return true;
            }

            @Override
            public boolean onPostFormSubmit(ProductEntity object) throws Exception {
                return true;
            }
        };
    }

}