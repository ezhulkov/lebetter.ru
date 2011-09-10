package org.ohm.lebetter.tapestry5.web.pages.po;

import org.apache.commons.lang.StringUtils;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.pages.Index;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;
import org.room13.mallcore.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 23.03.2009
 * Time: 17:22:45
 * To change this template use File | Settings | File Templates.
 */
public class MainProxy extends AdminBasePage {

    public Object onActivate() throws Exception {

        getIOC().getRequest().getSession(false).
                setAttribute(Constants.IS_AUTHENTICATED, Boolean.toString(true));

        UserEntity user = getAuth().getUser();

        if (user.isJustEntered()) {
            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("User has just entered", user);
            }
            getServiceFacade().getUserManager().
                    afterSuccesfullLogin(user,
                                         getIOC().getRequestGlobals().getHTTPServletRequest());
        }

        //Route user to referer page
        String referer = (String) getIOC().getRequest().getSession(false).
                getAttribute(Constants.PAGE_REFERER);

        if (!StringUtils.isBlank(referer)) {
            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Redirecting to referer: " + referer, user);
            }
            getIOC().getResponse().sendRedirect(referer);
            return null;
        }

        return Index.class;
    }
}