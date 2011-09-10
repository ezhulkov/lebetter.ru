package org.ohm.lebetter.tapestry5.web.pages.po.staff;

import org.apache.tapestry5.annotations.InjectComponent;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 17:55:42
 * To change this template use File | Settings | File Templates.
 */
public class Property extends AdminBasePage {

    @org.apache.tapestry5.annotations.Property
    private PropertyEntity selectedProperty;

    @InjectComponent
    private SelectedObject selectedObject;


    public void onActivate(String idStr) throws Exception {

        //Get duplicate object
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getPropertyManager());
        selectedProperty = (PropertyEntity) selectedObject.findSelectedObject();

    }

    public Long onPassivate() {
        return selectedProperty == null ? null : selectedProperty.getRootId();
    }

}