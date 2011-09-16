package org.ohm.lebetter.tapestry5.web.pages.po.system;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;
import org.room13.mallcore.model.impl.entities.RoleEntity;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 17:55:42
 * To change this template use File | Settings | File Templates.
 */
public class Role extends AdminBasePage {

    @Persist
    private String curBlockStr;

    @Property
    private RoleEntity selectedRole;

    @Inject
    @Property
    private Block actsBlock;

    @Inject
    @Property
    private Block profBlock;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {

        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getRoleManager());
        selectedRole = (RoleEntity) selectedObject.findSelectedObject();

    }

    public Long onPassivate() {

        return selectedRole == null ? null : selectedRole.getRootId();

    }

    public Block onActionFromRoleActionsAjax() {
        curBlockStr = "actsBlock";
        return actsBlock;
    }

    public Block onActionFromRoleProfileAjax() {
        curBlockStr = "profBlock";
        return profBlock;
    }

    @Cached
    public Block getCurBlock() {
        if ("profBlock".equals(curBlockStr)) {
            return profBlock;
        } else {
            return actsBlock;
        }
    }

}
