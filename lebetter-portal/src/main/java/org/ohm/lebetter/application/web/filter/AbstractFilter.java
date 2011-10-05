package org.ohm.lebetter.application.web.filter;

import org.room13.mallcore.application.web.filter.MallAbstractFilter;

import javax.servlet.ServletException;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 05.04.2011
 * Time: 15:02:02
 * To change this template use File | Settings | File Templates.
 */
public class AbstractFilter extends MallAbstractFilter {

    @Override
    public void initFilterBean() throws ServletException {
        super.initFilterBean();
        addFilter("/lb-fh");
        addFilter("/2ra");
        addFilter("/multipow");
        addFilter("/redactor");
    }
}
