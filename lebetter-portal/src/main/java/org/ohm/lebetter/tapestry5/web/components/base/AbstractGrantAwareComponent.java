package org.ohm.lebetter.tapestry5.web.components.base;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.model.impl.entities.ActionEntity;
import org.room13.mallcore.spring.service.RoleManager;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 17.04.2009
 * Time: 11:04:04
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGrantAwareComponent extends AbstractBaseComponent {

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private boolean autoCompleteAction = false;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String roles = null;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String action = null;

    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String actionPrefix = null;

    @Parameter(required = false)
    private ObjectBaseEntity object = null;

    @Parameter(required = false, allowNull = true, value = "prop:object",
               defaultPrefix = BindingConstants.PROP)
    private ObjectBaseEntity securityObject = null;

    private boolean actionGranted() {

        if (autoCompleteAction && securityObject != null) {
            String entityCode = securityObject.getEntityCode();
            if (entityCode == null) {
                String[] cName = getClass().getName().split("\\.");
                entityCode = cName[cName.length - 1];
            }
            String newAction = actionPrefix.toUpperCase() + entityCode.toUpperCase();
            if (getRMLogger().isTraceEnabled()) {
                getRMLogger().trace("Autocompleting action: " + newAction, securityObject);
            }
            setAction(newAction);
        }

        //Check action for misprint error
        ActionEntity ac = getServiceFacade().getActionManager().getActionByCode(action);
        if (ac == null) {
            getRMLogger().error("Configuration error in menulink component. " +
                                "There is no such action - " + action);
            return false;
        }

        if (getRMLogger().isTraceEnabled()) {
            getRMLogger().trace("Checking if action granted: " + getAction(), securityObject);
        }

        //Check if action granted to user's role set
        RoleManager roleManager = getServiceFacade().getRoleManager();
        boolean res = roleManager.isActionGranted(getAuth().getUser(), getAction(), securityObject);

        if (getRMLogger().isTraceEnabled()) {
            getRMLogger().trace("Result: " + res, securityObject);
        }

        return res;
    }

    private boolean roleAssigned() {

        if (getRMLogger().isTraceEnabled()) {
            getRMLogger().trace("Checking if role assigned: " + roles, securityObject);
        }

        //Check if user has one of the listed roles 
        String[] rolesArray = roles.split(",");
        if (rolesArray != null) {

            for (String desiredRole : rolesArray) {
                if (desiredRole.equals("ALL")) {
                    getRMLogger().trace("Result: true");
                    return true;
                }
                if (getServiceFacade().getRoleManager().isRoleAssigned(getAuth().getUser(),
                                                                       desiredRole,
                                                                       securityObject)) {
                    return true;
                }
            }
        }

        getRMLogger().trace("Result: false");
        return false;
    }

    protected boolean authReqsSatisfied() {

        if (getRMLogger().isTraceEnabled()) {
            getRMLogger().trace("Checking grants to access inner TML components", securityObject);
        }

        //do not allow to edit admin role!
        if (!"ALL".equals(roles) && securityObject != null && securityObject instanceof UserEntity) {
            UserEntity objectUser = (UserEntity) securityObject;
            if (!getServiceFacade().getUserManager().isAdminRole(getAuth().getUser()) &&
                getServiceFacade().getUserManager().isAdminRole(objectUser)) {
                return false;
            }
        }

        if (roles != null) {
            return roleAssigned();
        } else if (action != null || actionPrefix != null) {
            return actionGranted();
        }
        return false;
    }

    public boolean getAutoCompleteAction() {
        return autoCompleteAction;
    }

    public void setAutoCompleteAction(boolean autoCompleteAction) {
        this.autoCompleteAction = autoCompleteAction;
    }

    public String getActionPrefix() {
        return actionPrefix;
    }

    public void setActionPrefix(String actionPrefix) {
        this.actionPrefix = actionPrefix;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public ObjectBaseEntity getObject() {
        return this.object;
    }

    public void setObject(ObjectBaseEntity object) {
        this.object = object;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public ObjectBaseEntity getSecurityObject() {
        return securityObject;
    }

    public void setSecurityObject(ObjectBaseEntity securityObject) {
        this.securityObject = securityObject;
    }
}
