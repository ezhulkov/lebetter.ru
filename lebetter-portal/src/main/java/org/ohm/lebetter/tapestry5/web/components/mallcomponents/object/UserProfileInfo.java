package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 13:04:15
 * To change this template use File | Settings | File Templates.
 */
public class UserProfileInfo extends AbstractBaseComponent {

    @Property
    @Parameter(required = true, allowNull = false)
    private UserEntity user;

    public boolean isSelfSelected() {
        return getAuth().getUser().equals(user);
    }

}