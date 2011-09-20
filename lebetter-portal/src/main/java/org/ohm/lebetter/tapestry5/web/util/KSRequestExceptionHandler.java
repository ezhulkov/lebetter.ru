package org.ohm.lebetter.tapestry5.web.util;

import org.apache.tapestry5.services.RequestExceptionHandler;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.ResponseRenderer;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.log.UIException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class KSRequestExceptionHandler implements RequestExceptionHandler {

    private static final RMLogger log = new RMLogger(KSRequestExceptionHandler.class);
    private static final String WAS_E500 = "WAS_E500";

    private RequestGlobals requestGlobals;
    private ResponseRenderer renderer;

    public KSRequestExceptionHandler(RequestGlobals requestGlobals,
                                     ResponseRenderer renderer) {
        this.requestGlobals = requestGlobals;
        this.renderer = renderer;
    }

    @Override
    public void handleRequestException(Throwable exception) throws IOException {

        if (exception == null ||
                exception instanceof IllegalStateException) {
            return;
        }

        // неправильный URL = 404
        if (exception instanceof IllegalArgumentException &&
                exception.getStackTrace() != null &&
                exception.getStackTrace().length > 0 &&
                "org.apache.tapestry5.internal.services.URLEncoderImpl".
                        equals(exception.getStackTrace()[0].getClassName())) {
            log.debug("Page not found ", exception);
            requestGlobals.getResponse().sendError(404, "Page not found");
            return;
        }

        HttpServletRequest request = requestGlobals.getHTTPServletRequest();
        if (request != null) {
            HttpSession session = request.getSession(false);
            String msg = new StringBuffer().append(request.getServletPath()).append(" [").
                    append(request.getHeader("User-Agent")).append("]").toString();
            log.error("Internal server error occurred!", new UIException(msg, exception));
            if (session.getAttribute(WAS_E500) != null) {
                renderer.renderPageMarkupResponse("errors/E500ND");
            } else {
                session.setAttribute(WAS_E500, WAS_E500);
                renderer.renderPageMarkupResponse("errors/E500");
            }
        }
    }

}
