package org.ohm.lebetter.application.web.filter;


import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.util.StringUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TapestryURLFilter extends KinderAbstractFilter {

    private static final RMLogger log = new RMLogger(TapestryURLFilter.class);

    @Override
    public void doFilterInternal(final HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain) throws ServletException, IOException {

        if (needToPassToTapestry(request)) {
            HttpServletRequest requestWrapper = new HttpServletRequestWrapper(request) {
                @Override
                public String getServletPath() {
                    return filterPath(request);
                }
            };

            filterChain.doFilter(requestWrapper, response);
            return;
        }

        filterChain.doFilter(request, response);

    }

    private String filterPath(HttpServletRequest request) {
        String filterePath = (String) request.getAttribute("FILTERED_PATH");
        if (filterePath == null) {
            filterePath = request.getServletPath();
            if (!StringUtil.isEmpty(filterePath)) {
                if (filterePath.indexOf("&%23036%3B") != -1) {
                    filterePath = StringUtil.replace(filterePath, "&%23036%3B", "$");
                }
                if (filterePath.indexOf("&#036;") != -1) {
                    filterePath = StringUtil.replace(filterePath, "&#036;", "$");
                }
                if (filterePath.indexOf("&") != -1) {
                    filterePath = filterePath.substring(0, filterePath.indexOf("&"));
                }
                if (filterePath.indexOf("%3B") != -1) {
                    filterePath = filterePath.substring(0, filterePath.indexOf("%3B"));
                }
                if (filterePath.indexOf(";") != -1) {
                    filterePath = filterePath.substring(0, filterePath.indexOf(";"));
                }
                if (filterePath.indexOf(":/") != -1) {
                    filterePath = filterePath.substring(0, filterePath.indexOf(":/"));
                }
                if (filterePath.indexOf("%20") != -1) {
                    filterePath = StringUtil.replace(filterePath, "%20", " ");
                }
                if (filterePath.indexOf("%24") != -1) {
                    filterePath = StringUtil.replace(filterePath, "%24", "$");
                }
                if (filterePath.indexOf("/$n") != -1) {
                    filterePath = StringUtil.replace(filterePath, "/$n", "/$N");
                }
                if (filterePath.indexOf("/$b") != -1) {
                    filterePath = StringUtil.replace(filterePath, "/$b", "/$B");
                }
                if (filterePath.indexOf("' + thislocation + '") != -1) {
                    filterePath = StringUtil.replace(filterePath, "' + thislocation + '", "");
                }
            }
            request.setAttribute("FILTERED_PATH", filterePath);
        } 
        return filterePath;
    }

}
