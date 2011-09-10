package org.ohm.lebetter.tapestry5.web.pages.auth;

import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.03.2009
 * Time: 16:11:47
 * To change this template use File | Settings | File Templates.
 */
public class Login extends AbstractBasePage {

    @Property
    private boolean error;

    @Property
    private boolean dup;

    void onActivate(String errcode) {
        if ("edup".equals(errcode)) {
            dup = true;
        } else {
            error = true;
        }
    }

}