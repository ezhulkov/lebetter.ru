package org.ohm.lebetter.tapestry5.web.components.base;

import org.apache.tapestry5.annotations.Parameter;
import org.ohm.lebetter.model.impl.entities.UserEntity;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 13:55:23
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractUserEditComponent extends AbstractBaseComponent {

    @Parameter(name = "object", required = false, allowNull = false)
    private UserEntity selectedObject;

    public UserEntity getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(UserEntity selectedObject) {
        this.selectedObject = selectedObject;
    }

}