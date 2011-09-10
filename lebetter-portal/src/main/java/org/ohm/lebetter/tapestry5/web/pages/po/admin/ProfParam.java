package org.ohm.lebetter.tapestry5.web.pages.po.admin;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;
import org.room13.mallcore.model.impl.entities.ProfileParameterEntity;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 17:55:42
 * To change this template use File | Settings | File Templates.
 */
public class ProfParam extends AdminBasePage {

    @Persist
    private String curBlockStr;

    @Property
    private ProfileParameterEntity selectedParam;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {

        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getProfileParameterManager());
        selectedParam = (ProfileParameterEntity) selectedObject.findSelectedObject();

    }

    public Long onPassivate() {

        return selectedParam == null ? null : selectedParam.getRootId();

    }

}