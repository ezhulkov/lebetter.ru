package org.ohm.lebetter.tapestry5.web.pages.po.staff;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 12:48:21
 * To change this template use File | Settings | File Templates.
 */
public class Users extends AdminBasePage {

    @Property
    private String filter = null;

    @Cached
    public int getOnlineUserCount() {
        return getServiceFacade().getUserManager().getOnlineCount();
    }

    @Cached
    public int getUserSize() {
        return getServiceFacade().getUserManager().getCount();
    }

    public void onActivate(String filter) {
        this.filter = filter;
    }

    public Object[] onPassivate() {
        return filter == null ?
                null : new String[]{filter};
    }

}