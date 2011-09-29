package org.ohm.lebetter.tapestry5.web.pages.po.system.property;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
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
    private PropertyEntity selectedProperty;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getPropertyManager());
        selectedProperty = (PropertyEntity) selectedObject.findSelectedObject();
    }

    public Long onPassivate() {
        return selectedProperty == null ? null : selectedProperty.getRootId();
    }

}