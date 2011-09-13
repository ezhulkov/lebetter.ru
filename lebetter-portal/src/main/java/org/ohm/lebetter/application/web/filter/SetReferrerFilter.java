package org.ohm.lebetter.application.web.filter;

import org.room13.mallcore.Constants;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SetReferrerFilter extends KinderAbstractFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if ("GET".equals(request.getMethod()) && needToPassToTapestry(request)) {
            String path = new UrlPathHelper().getRequestUri(request);
            if (!path.startsWith("/auth") &&
                !path.startsWith("/po/mainproxy")) {
                request.getSession(false).setAttribute(Constants.PAGE_REFERER, path);
            }
        }
        filterChain.doFilter(request, response);
    }

}