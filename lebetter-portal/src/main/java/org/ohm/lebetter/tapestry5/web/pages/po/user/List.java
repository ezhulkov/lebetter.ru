package org.ohm.lebetter.tapestry5.web.pages.po.user;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;
import org.ohm.lebetter.tapestry5.web.util.datasource.GenericEntityGridDS;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class List extends AbstractBasePage {

    @Property
    private UserEntity oneUser;

    @Cached
    public GridDataSource getUserList() {
        java.util.List<Long> ids = getServiceFacade().getUserManager().getAllIds(null, "name", null, null);
        return new GenericEntityGridDS<UserEntity>(ids, getServiceFacade().getUserManager(), UserEntity.class);
    }

}
