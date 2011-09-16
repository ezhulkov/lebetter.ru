package org.ohm.lebetter.tapestry5.web.pages;

public class Index {

    public Class onActivate(Object... args) throws Exception {
        return Index2.class;
    }


    //    public void onActivate(Object... args) throws Exception {
    //        if (args.length != 0) {
    //            getIOC().getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,
    //                                             "Page not found " + getIOC().getRequest().getPath());
    //            getRMLogger().error("Page not found " + getIOC().getRequest().getPath());
    //        }
    //    }

}