package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractGrantAwareComponent;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 17:47:04
 * To change this template use File | Settings | File Templates.
 */
public class JavascriptDestination extends AbstractGrantAwareComponent {

    @Property
    private Block oneBlock;

    @SuppressWarnings("unchecked")
    public List<Block> getBlocks() {
        List<Block> res = (List<Block>) getIOC().getHttpServletRequest().getAttribute(Javascript.JS_QUEUE);
        if (res != null) {
            getIOC().getHttpServletRequest().setAttribute(Javascript.JS_QUEUE, null);
            return res;
        }
        return null;
    }

}