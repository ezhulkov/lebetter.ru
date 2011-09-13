package org.ohm.lebetter.tapestry5.web.pages;

import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;

import javax.servlet.http.HttpServletResponse;

public class Index2 extends AbstractBasePage {

    public void onActivate(Object... args) throws Exception {
        if (args.length != 0) {
            getIOC().getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,
                                             "Page not found " + getIOC().getRequest().getPath());
            getRMLogger().error("Page not found " + getIOC().getRequest().getPath());
        }
    }

}