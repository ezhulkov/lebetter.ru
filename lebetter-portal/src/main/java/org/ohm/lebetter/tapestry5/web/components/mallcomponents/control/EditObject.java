package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 12:19:49
 * To change this template use File | Settings | File Templates.
 */
public class EditObject extends AbstractEditObject {

    @Property
    @Inject
    private Block editAreaBlock;

    @Property
    @InjectComponent
    private EditObjectShow editObjectShow;

}