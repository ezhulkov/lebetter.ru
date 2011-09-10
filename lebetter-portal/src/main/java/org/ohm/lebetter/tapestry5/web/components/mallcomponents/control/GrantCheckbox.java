package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.impl.entities.ActionEntity;
import org.room13.mallcore.model.impl.entities.RoleEntity;
import org.room13.mallcore.model.impl.entities.RoleToActionEntity;
import org.room13.mallcore.model.impl.entities.RoleToActionEntity.AccessFlag;

public class GrantCheckbox extends AbstractBaseComponent {

    @Property
    @Parameter(name = "action", required = true, allowNull = false)
    private ActionEntity action;

    @Property
    @Parameter(name = "role", required = true, allowNull = false)
    private RoleEntity role;

    @Property
    private AccessFlag flag = AccessFlag.NOT_SET;

    public void beginRender() {
        RoleToActionEntity link = getServiceFacade().getRoleManager().getActionForRole(role, action);
        if (link != null) {
            flag = link.getFlag();
        } else {
            flag = RoleToActionEntity.AccessFlag.NOT_SET;
        }
    }

}
