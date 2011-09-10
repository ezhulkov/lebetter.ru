package org.ohm.lebetter.tapestry5.web.pages.po.staff;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.ColorEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 17:55:42
 * To change this template use File | Settings | File Templates.
 */
public class Color extends AdminBasePage {

    @Property
    private ColorEntity selectedColor;

    @Property
    private ColorEntity selectedColorL;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {

        //Get duplicate object
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getColorManager());
        selectedColor = (ColorEntity) selectedObject.findSelectedObject();

        if (selectedColor == null) {
            return;
        }

        if (selectedColor != null) {
            selectedColorL = getServiceFacade().getColorManager().getL(selectedColor.getId());
        }

    }

    public Long onPassivate() {
        return selectedColor == null ? null : selectedColor.getRootId();
    }

}