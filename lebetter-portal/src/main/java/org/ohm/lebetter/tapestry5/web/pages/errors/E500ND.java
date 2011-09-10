package org.ohm.lebetter.tapestry5.web.pages.errors;

import org.apache.tapestry5.services.ExceptionReporter;

public class E500ND implements ExceptionReporter {

    @Override
    public void reportException(Throwable exception) {
        // all work already made in org.room13.mallcore.tapestry5.web.util.RMRequestExceptionHandler
    }

    //    @Override
    public void setPageReferer() {
        // do nothing
    }
}