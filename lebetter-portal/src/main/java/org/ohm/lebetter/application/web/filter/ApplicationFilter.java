package org.ohm.lebetter.application.web.filter;

import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 20.02.2011
 * Time: 18:16:22
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationFilter extends org.room13.mallcore.application.web.filter.ApplicationFilter {

    public void initFilterBean() throws ServletException {
        super.initFilterBean();
        addFilter("/redactor");
        addFilter("/pageflip");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LocaleContextHolder.setLocale(new Locale("ru"));
        super.doFilterInternal(request, response, filterChain);
    }
}
