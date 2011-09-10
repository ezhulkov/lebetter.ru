package org.ohm.lebetter.tapestry5.web.pages.po.admin;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;
import org.room13.mallcore.model.impl.entities.RoleEntity;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 12:48:21
 * To change this template use File | Settings | File Templates.
 */
public class Roles extends AdminBasePage {

    @Property
    private RoleEntity oneRole;

    @Cached
    public List<RoleEntity> getAllRoles() {
        return getServiceFacade().getRoleManager().getAllReadyRolesForUI();
    }

    public void onActivate() throws Exception {

    }

    public String getPadding() {
        return oneRole.getOffset() * 30 + "px;";
    }

}