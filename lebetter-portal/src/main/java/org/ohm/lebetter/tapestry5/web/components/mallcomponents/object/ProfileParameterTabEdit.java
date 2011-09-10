package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.ProfileAware;

@SuppressWarnings("unchecked")
public class ProfileParameterTabEdit extends AbstractBaseComponent {

    @Property
    @Parameter(name = "profileAwareObject", allowNull = false, required = false)
    private ProfileAware profileAwareObject;

}