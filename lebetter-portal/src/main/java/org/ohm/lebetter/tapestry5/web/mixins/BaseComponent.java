package org.ohm.lebetter.tapestry5.web.mixins;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.ohm.lebetter.Constants;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.base.WebAppAuthFacade;
import org.ohm.lebetter.tapestry5.web.base.WebAppBaseFacade;
import org.ohm.lebetter.tapestry5.web.base.WebAppIOCFacade;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage.Type;
import org.ohm.lebetter.tapestry5.web.services.ServiceFacade;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.impl.embedded.AddressSocial;
import org.room13.mallcore.model.impl.entities.RoleEntity;
import org.room13.mallcore.spring.service.RoleManager;
import org.room13.mallcore.util.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 13:58:22
 * To change this template use File | Settings | File Templates.
 */
public final class BaseComponent implements WebAppIOCFacade, WebAppAuthFacade, WebAppBaseFacade {

    private static final String SECURITY_URL = "/j_security_check";

    private static final RMLogger log = new RMLogger(BaseComponent.class);

    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DF);
        }
    };

    @Override
    public RMLogger getRMLogger() {
        log.setRequest(requestGlobals.getHTTPServletRequest());
        return log;
    }

    @Inject
    private PropertyAccess access;

    @Inject
    private ServiceFacade serviceFacade;

    @Inject
    private Properties properties;

    @Inject
    private Request request;

    @Inject
    private RequestGlobals requestGlobals;

    @Inject
    private Response response;

    @Inject
    private ComponentResources resources;

    @Inject
    private Context context;

    @Inject
    private ApplicationGlobals globals;

    @Environmental
    private RenderSupport renderSupport;

    @Inject
    private Messages messages;

    public Messages getMessages() {
        return messages;
    }

    public Context getContext() {
        return context;
    }

    public RenderSupport getRenderSupport() {
        return renderSupport;
    }

    public RequestGlobals getRequestGlobals() {
        return requestGlobals;
    }

    public ComponentResources getResources() {
        return resources;
    }

    public ServiceFacade getServiceFacade() {
        return serviceFacade;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }

    public HttpServletRequest getHttpServletRequest() {
        return requestGlobals.getHTTPServletRequest();
    }

    public HttpServletResponse getHttpServletResponse() {
        return requestGlobals.getHTTPServletResponse();
    }

    @Override
    public PropertyAccess getPropertyAccess() {
        return access;
    }

    @Override
    public String getSpringSecurityUrl() {
        return String.format("%s%s", getRequest().getContextPath(), SECURITY_URL);
    }

    @Override
    public String getSessionId() {
        return requestGlobals.getHTTPServletRequest().getSession(false).getId();
    }

    private UserEntity getAsAnonym() {
        UserEntity user = new UserEntity();
        user.setAnonymous(true);
        RoleManager roleManager = getServiceFacade().getRoleManager();
        RoleEntity anonRole = roleManager.getRoleByCode(Constants.Roles.ROLE_ANONYMOUS);
        List<RoleEntity> roles = new ArrayList<RoleEntity>();
        roles.add(anonRole);
        user.setRoles(roles);
        return user;
    }

    @SuppressWarnings("unchecked")
    public UserEntity getUser() {
        UserEntity user = (UserEntity) serviceFacade.getUserManager().getAuthenticatedPrincipal();
        if (user != null) {
            //Get user from session
            user = getServiceFacade().getUserManager().getDuplicate(user.getRootId());
        } else {
            //If not authenticated - create user with role ANONYMOUS
            user = getAsAnonym();
        }
        if (user != null && user.getAddress() == null) {
            user.setAddress(new AddressSocial());
        }
        return user;
    }

    @Override
    public boolean isAuthenticated() {

        if (getRequest().getSession(false) == null) {
            return false;
        }

        UserEntity user = (UserEntity) serviceFacade.
                getUserManager().getAuthenticatedPrincipal();
        return user != null && !user.isDummy();

    }

    @Override
    public boolean isAdminRole() {
        return getServiceFacade().getRoleManager().isRoleAssigned(getUser(), Constants.Roles.ROLE_ADMIN);
    }

    @Override
    public boolean isStaffRole() {
        return getServiceFacade().getRoleManager().isRoleAssigned(getUser(), Constants.Roles.ROLE_STAFF);
    }

    @Override
    public boolean isDealerRole() {
        return getServiceFacade().getRoleManager().isRoleAssigned(getUser(), Constants.Roles.ROLE_DEALER);
    }

    @Override
    @Cached
    public boolean isAnonymous() {
        return getServiceFacade().getUserManager().isAnonymousRole(getUser());
    }

    public String getText(String key, Object... args) {
        return StringUtils.replace(MessageUtil.format(getMessages().get(key), args), "\"", "'");
    }

    public void onPrepare() {
        globals.getServletContext().setAttribute(Constants.T5MESSAGES, messages);
    }

    public void redirectToReferer() {
        String referer = getRequest().getHeader("referer");
        if (referer == null) {
            referer = "index";
        }

        try {
            getResponse().sendRedirect(getResources().createPageLink(referer, false));
        } catch (Exception ex) {
            getRMLogger().error("Error while redirecting to referer " + referer);
        }
    }

    public void addFlashToSession(String msg, Type type) {
        getRequest().getSession(false).setAttribute(Constants.FLASH, new FlashMessage(msg, type));
    }

    public FlashMessage getFlashMessage() {

        FlashMessage res = null;
        if (getRequest().getSession(false) != null &&
            getRequest().getSession(false).getAttribute(Constants.FLASH) != null) {
            res = (FlashMessage) getRequest().getSession(false).getAttribute(Constants.FLASH);
            getRequest().getSession(false).setAttribute(Constants.FLASH, null);
        }
        return res;
    }

    public boolean hasFlashMessage(FlashMessage.Type type) {
        if (getRequest().getSession(false).getAttribute(Constants.FLASH) != null) {
            FlashMessage res = (FlashMessage) getRequest().getSession(false).
                    getAttribute(Constants.FLASH);
            return type == null || res.getType() == type;
        }
        return false;
    }

    @Override
    public String formatSystemError(String error) {
        if (StringUtils.isBlank(error)) {
            error = "error.generic";
        } else {
            String[] parts = error.toLowerCase().split(" ");
            StringBuffer result = new StringBuffer("error.");
            for (int i = 0; i < parts.length; i++) {
                result.append(parts[i]);
                if (i != parts.length - 1) {
                    result.append(".");
                }
            }
            error = result.toString();
        }
        return error;
    }


    @Override
    public Properties getProperties() {
        return properties;
    }

    @Override
    public boolean isBots() {
        String userAgent = getRequest().getHeader("User-Agent");
        return userAgent == null ||
               userAgent.toLowerCase().indexOf("bot") != -1 ||
               userAgent.toLowerCase().indexOf("wget") != -1;
    }

    public DateFormat getThreadSafeDateFormat() {
        return DATE_FORMAT.get();
    }

}