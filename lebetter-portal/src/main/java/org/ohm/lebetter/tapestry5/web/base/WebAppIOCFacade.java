package org.ohm.lebetter.tapestry5.web.base;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.ohm.lebetter.tapestry5.web.services.ServiceFacade;
import org.room13.mallcore.log.RMLogger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 13:59:46
 * To change this template use File | Settings | File Templates.
 */
public interface WebAppIOCFacade {

    public Messages getMessages();

    public Context getContext();

    public RMLogger getRMLogger();

    public RequestGlobals getRequestGlobals();

    public ComponentResources getResources();

    public ServiceFacade getServiceFacade();

    public RenderSupport getRenderSupport();

    public Request getRequest();

    public Response getResponse();

    public HttpServletRequest getHttpServletRequest();

    public HttpServletResponse getHttpServletResponse();

    public PropertyAccess getPropertyAccess();

    public Properties getProperties();

}
