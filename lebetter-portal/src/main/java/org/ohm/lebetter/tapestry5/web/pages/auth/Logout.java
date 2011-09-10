package org.ohm.lebetter.tapestry5.web.pages.auth;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.ohm.lebetter.tapestry5.web.pages.Index;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;
import org.room13.mallcore.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 18.03.2009
 * Time: 15:42:16
 * To change this template use File | Settings | File Templates.
 */


public class Logout extends AbstractBasePage {

    @Inject
    private Cookies cookies;

    public Object onActivate() throws Exception {

        if (getIOC().getRequest().getSession(false) == null) {
            return Index.class;
        }
        String referer = (String) getIOC().getRequest().getSession(false).
                getAttribute(Constants.PAGE_REFERER);
        clearCookie();

        getRMLogger().debug("Cookies cleared");

        getIOC().getRequest().getSession(false).invalidate();

        getRMLogger().debug("Session invalidated");

        getServiceFacade().getUserManager().
                afterSuccesfullLogout(getAuth().getUser(),
                                      getIOC().getRequestGlobals().getHTTPServletRequest());

        getRMLogger().debug("Logout done");

        if (!StringUtils.isBlank(referer)) {
            getRMLogger().debug("Redirecting to referer: " + referer, getAuth().getUser());
            getIOC().getResponse().sendRedirect(referer);
            return null;
        }

        return Index.class;

    }

}