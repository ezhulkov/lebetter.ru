package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.ProfileAware;
import org.room13.mallcore.model.impl.entities.ProfileParameterEntity;

import java.util.List;

@SuppressWarnings("unchecked")
public class ProfileParameterList extends AbstractBaseComponent {

    @Property
    private ProfileParameterEntity oneParam;

    @Parameter(name = "profileAwareObject", allowNull = false, required = false)
    private ProfileAware profileAwareObject;

    public List<ProfileParameterEntity> getProfileParameter() {
        return profileAwareObject.getProfileParameters();
    }

}