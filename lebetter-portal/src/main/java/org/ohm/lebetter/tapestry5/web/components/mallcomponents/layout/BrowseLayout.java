package org.ohm.lebetter.tapestry5.web.components.mallcomponents.layout;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 18.03.2009
 * Time: 11:37:54
 * To change this template use File | Settings | File Templates.
 */

public class BrowseLayout extends AbstractBaseComponent {

    @Property
    @Parameter(required = true, allowNull = false)
    private Block leftMenuBlock;

    @Property
    @Parameter(required = true, allowNull = false)
    private Block breadCrumpsBlock;

}

