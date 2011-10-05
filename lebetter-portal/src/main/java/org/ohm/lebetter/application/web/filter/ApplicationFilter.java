package org.ohm.lebetter.application.web.filter;

import javax.servlet.ServletException;

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
    }

}
