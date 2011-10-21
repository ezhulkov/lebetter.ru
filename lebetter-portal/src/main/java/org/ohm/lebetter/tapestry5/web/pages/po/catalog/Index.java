package org.ohm.lebetter.tapestry5.web.pages.po.catalog;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Index extends AdminBasePage {

    @Property
    @Inject
    private Block descBlock;

    @Property
    @Inject
    private Block picsBlock;

    @Property
    @Inject
    private Block adminBlock;

    @Property
    private ProductEntity selectedProduct;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getProductManager());
        selectedProduct = (ProductEntity) selectedObject.findSelectedObject();
    }

    public Long onPassivate() {
        return selectedProduct == null ? null : selectedProduct.getRootId();
    }

    public Block onActionFromPicsTab() {
        return picsBlock;
    }

    public Block onActionFromDescTab() {
        return descBlock;
    }

    public Block onActionFromAdminTab() {
        return adminBlock;
    }

    @Cached
    public String[] getProductContext() {
        selectedProduct = getServiceFacade().getProductManager().get(selectedProduct.getId());
        CategoryEntity cat = getSelectedCategory();
        return new String[]{cat == null ? "" : cat.getAltId(), selectedProduct.getAltId()};
    }

    @Cached
    public CategoryEntity getSelectedCategory() {
        if (selectedProduct != null &&
            selectedProduct.getCategories() != null &&
            selectedProduct.getCategories().size() != 0) {
            return selectedProduct.getCategories().get(0);
        }
        return null;
    }

}