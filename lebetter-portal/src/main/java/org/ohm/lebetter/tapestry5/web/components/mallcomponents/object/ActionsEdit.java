package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.impl.entities.ActionEntity;
import org.room13.mallcore.model.impl.entities.RoleEntity;
import org.room13.mallcore.model.impl.entities.RoleToActionEntity;
import org.room13.mallcore.spring.service.RoleManager;

import java.util.List;

@SuppressWarnings("unchecked")
public class ActionsEdit extends AbstractBaseComponent {

    @Property
    @Parameter(name = "role", allowNull = false, required = true)
    private RoleEntity selectedRole;

    @Property
    @Inject
    private Block actionsBlock;

    @Property
    private ActionEntity oneAction;

    @Cached

    public List<ActionEntity> getActions() {
        return getServiceFacade().getActionManager().getAllActionsForUI();
    }

    public String getRowClass() {
        if (!StringUtils.isBlank(oneAction.getActionGroupName())) {
            return "actgroup";
        } else {
            return null;
        }
    }

    @OnEvent(value = "submit", component = "actionsForm")
    public Block onActionsFormSubmit(Long roleId) {

        List<String> names = getIOC().getRequest().getParameterNames();
        RoleEntity role = getServiceFacade().getRoleManager().getRootDuplicate(roleId);
        for (String name : names) {
            if (name.startsWith("2RM-")) {
                String value = getIOC().getRequest().getParameter(name);
                String[] parts = name.split("-");
                String actionIdStr = parts[2];
                Long actionId = Long.parseLong(actionIdStr);
                ActionEntity action = getServiceFacade().getActionManager().getRootDuplicate(actionId);
                RoleManager roleManager = getServiceFacade().getRoleManager();
                if (value.equals("0")) {
                    roleManager.assignActionToRole(role,
                            action,
                            RoleToActionEntity.AccessFlag.DENY,
                            getAuth().getUser());
                } else if (value.equals("2")) {
                    roleManager.assignActionToRole(role,
                            action,
                            RoleToActionEntity.AccessFlag.ALLOW,
                            getAuth().getUser());
                } else {
                    roleManager.revokeActionFromRole(role,
                            action,
                            getAuth().getUser());
                }

            }
        }

        return actionsBlock;
    }

}