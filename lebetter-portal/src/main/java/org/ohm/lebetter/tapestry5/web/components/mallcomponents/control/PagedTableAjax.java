package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

public class PagedTableAjax extends PagedLoopAjax {
    @Parameter(name = "header", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private Block headerBlock;
}