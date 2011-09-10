package org.ohm.lebetter.tapestry5.web.pages.po.staff;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 17:55:42
 * To change this template use File | Settings | File Templates.
 */
public class Category extends AdminBasePage {

    @Property
    private CategoryEntity selectedCategory;

    @InjectComponent
    private SelectedObject selectedObject;


    public void onActivate(String idStr) throws Exception {

        //Get duplicate object
        selectedObject.setIdStr(idStr);
        CategoryManager categoryManager = getServiceFacade().getCategoryManager();
        selectedObject.setObjectManager(categoryManager);
        selectedCategory = (CategoryEntity) selectedObject.findSelectedObject();

    }

    public Long onPassivate() {
        return selectedCategory == null ? null : selectedCategory.getRootId();
    }


    public boolean isLeaf() {
        return getServiceFacade().getCategoryManager().
                getAllCategoriesCount(selectedCategory) == 0;
    }

    public boolean isCategoryIsProduct(){
        return true;
    }

}