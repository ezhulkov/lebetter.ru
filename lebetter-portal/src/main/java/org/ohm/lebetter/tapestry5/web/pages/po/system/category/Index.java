package org.ohm.lebetter.tapestry5.web.pages.po.system.category;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
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
    private CategoryEntity selectedCategory;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getCategoryManager());
        selectedCategory = (CategoryEntity) selectedObject.findSelectedObject();
    }

    public Long onPassivate() {
        return selectedCategory == null ? null : selectedCategory.getRootId();
    }

    public Block onActionFromPicsTab() {
        return picsBlock;
    }

    public Block onActionFromDescTab() {
        return descBlock;
    }

    public CategoryEntity getSelectedCategoryRenew() {
        return getServiceFacade().getCategoryManager().get(selectedCategory.getRootId());
    }

}