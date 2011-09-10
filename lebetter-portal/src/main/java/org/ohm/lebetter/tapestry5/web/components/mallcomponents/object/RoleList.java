package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.impl.entities.RoleEntity;

import java.util.List;

public class RoleList extends AbstractBaseComponent {

    @Property
    private RoleEntity oneRole;

    @Property
    @Parameter(name = "user", required = true, allowNull = false)
    private UserEntity selectedUser;

    public List<RoleEntity> getSelectedUserRoles() {
        return selectedUser.getRoles();
    }
}