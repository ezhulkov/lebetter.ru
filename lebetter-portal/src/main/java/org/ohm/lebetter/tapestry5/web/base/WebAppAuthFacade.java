package org.ohm.lebetter.tapestry5.web.base;

import org.ohm.lebetter.model.impl.entities.UserEntity;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 14:27:01
 * To change this template use File | Settings | File Templates.
 */
public interface WebAppAuthFacade {

    public UserEntity getUser();

    public String getSpringSecurityUrl();

    public String getSessionId();

    public boolean isAuthenticated();

    public boolean isAnonymous();

    public boolean isAdminRole();

    public boolean isStaffRole();

    public boolean isDealerRole();

}
