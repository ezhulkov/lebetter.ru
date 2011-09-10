package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractGrantAwareComponent;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 17:47:04
 * To change this template use File | Settings | File Templates.
 */
public class ExtLink extends AbstractGrantAwareComponent {

    @Parameter(allowNull = false, required = true, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String id;

    @Parameter(allowNull = false, required = true, defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String href;

}