package org.ohm.lebetter.tapestry5.web.pages.po.staff;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 17:55:42
 * To change this template use File | Settings | File Templates.
 */
public class Value extends AdminBasePage {

    @Property
    private PropertyValueEntity selectedValue;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {

        //Get persist object
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getPropertyValueManager());
        selectedValue = (PropertyValueEntity) selectedObject.findSelectedObject();
    }

    public Long onPassivate() {
        return selectedValue == null ? null : selectedValue.getRootId();
    }

}