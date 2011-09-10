package org.ohm.lebetter.application.web.filter;

import org.room13.mallcore.util.StringUtil;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 21.04.2011
 * Time: 16:58:17
 * To change this template use File | Settings | File Templates.
 */
public class AvoidTapestryFilter extends KinderAbstractFilter {

    private final Collection<String> include = new TreeSet<String>();

    @Override
    public void initFilterBean() throws ServletException {
        super.initFilterBean();
        include.add("/principalgetter");
        include.add("/newsFeed");
        include.add("/rest");
        include.add("/2ra");
        include.add("/extAuth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        String path = urlPathHelper.getPathWithinApplication(request);
        String pathToCompare = path;

        if (!StringUtil.isEmpty(pathToCompare)) {
            int pos;
            if ((pos = pathToCompare.indexOf("/", 2)) != -1) {
                pathToCompare = pathToCompare.substring(0, pos);
            }
        }

        if (!super.needToPassToTapestry(request) && include.contains(pathToCompare)) {
            synchronized (log) {
                log.setRequest(request);
                log.debug("Passing request to servlet " + path);
            }
            RequestDispatcher rd = getServletContext().getRequestDispatcher(path);
            rd.forward(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

}
