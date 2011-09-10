package org.ohm.lebetter.tapestry5.web.pages.errors;

import org.apache.tapestry5.services.ExceptionReporter;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;

public class E500 extends AbstractBasePage implements ExceptionReporter {

    @Override
    public void reportException(Throwable exception) {
        // all work already made in org.room13.mallcore.tapestry5.web.util.RMRequestExceptionHandler
    }

}
