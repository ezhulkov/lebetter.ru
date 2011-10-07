package org.ohm.lebetter.tapestry5.web.pages.catalog;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBrowseBasePage;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 13.09.11
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class Index extends AbstractBrowseBasePage {

    @Property
    private ProductEntity selectedProduct;

    @Property
    private CategoryEntity selectedCategory = null;

    @Property
    private CategoryEntity oneSubCategory;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate() {
        onActivate(null);
    }

    public void onActivate(String cid, String pid) {
        selectedProduct = (ProductEntity) getServiceFacade().getProductManager().getByAltId(pid);
        selectedCategory = (CategoryEntity) getServiceFacade().getCategoryManager().getByAltId(cid);
        selectedObject.setObject(selectedCategory);
    }

    public void onActivate(String cid) {
        if (selectedCategory == null) {
            if (cid == null) {
                List<CategoryEntity> rootCats = getServiceFacade().getCategoryManager().getAllReadyCategories(null);
                if (rootCats.size() != 0) {
                    selectedCategory = rootCats.get(0);
                    selectedObject.setObject(selectedCategory);
                }
            } else {
                selectedObject.setIdStr(cid);
                selectedObject.setObjectManager(getServiceFacade().getCategoryManager());
                selectedCategory = (CategoryEntity) selectedObject.findSelectedObject();
            }
        }
    }

    public String onPassivate() {
        return selectedCategory == null ? null : selectedCategory.getAltId();
    }

    public CategoryEntity getSelectedCategoryParent() {
        return selectedCategory.getParent() == null ? selectedCategory : selectedCategory.getParent();
    }

}