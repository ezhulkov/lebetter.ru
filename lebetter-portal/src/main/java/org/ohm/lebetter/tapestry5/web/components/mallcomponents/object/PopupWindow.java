package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;


public class PopupWindow extends AbstractBaseComponent {

    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = true)
    private String popupClass;

    @Property
    @Parameter(name = "popupTitle", required = false, allowNull = true,
               defaultPrefix = BindingConstants.LITERAL)
    private String popupTitle;

}