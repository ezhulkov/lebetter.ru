package org.ohm.lebetter.tapestry5.web.pages;

import org.apache.tapestry5.annotations.Import;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;

import javax.servlet.http.HttpServletResponse;

public class Index extends AbstractBasePage {

    public void onActivate(Object... args) throws Exception {
        if (args.length != 0) {
            getIOC().getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,
                                             "Page not found " + getIOC().getRequest().getPath());
            getRMLogger().error("Page not found " + getIOC().getRequest().getPath());
        }
    }

    @Override
    public String getTitle() {
        return getIOC().getMessages().get("title.index.text1");
    }

    @Override
    public String getKeywords() {
        return getIOC().getMessages().get("title.index.text2");
    }

    @Override
    public String getDescription() {
        return getIOC().getMessages().get("title.index.text3");
    }



}