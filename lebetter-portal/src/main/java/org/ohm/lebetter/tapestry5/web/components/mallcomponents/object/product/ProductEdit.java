package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.product;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.layout.OfficeLayout;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericStatusSelectModel;

public class ProductEdit extends AbstractEditComponent {

    @Component(id = "name", parameters = {"value=selectedObject.name",
                                          "validate=maxlength=64,required"})
    private TextField propertyNameField;

    @Component(id = "articul", parameters = {"value=selectedObject.articul", "validate=maxlength=32,required"})
    private TextField propertyArtField;

    @Component(id = "stockStatus", parameters = {"value=selectedObject.stockStatus"})
    private Select stockStatusField;

    @Component(id = "price", parameters = {"value=selectedObject.price"})
    private TextField priceField;

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

    @Parameter
    private OfficeLayout office;


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

    public Block onActionFromAddProduct(Long pid) {
        OrderEntity order = getServiceFacade().getOrderManager().getCurrentOrder(getAuth().getUser(), true);
        getServiceFacade().getOrderManager().addOrderToProductLink(pid, order, getAuth().getUser());
        return office.getBasketBlock();
    }

}