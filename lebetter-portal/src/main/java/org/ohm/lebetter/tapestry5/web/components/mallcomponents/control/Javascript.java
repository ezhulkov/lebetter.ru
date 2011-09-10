package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.Block;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractGrantAwareComponent;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 17:47:04
 * To change this template use File | Settings | File Templates.
 */
public class Javascript extends AbstractGrantAwareComponent {

    public static final String JS_QUEUE = "JS_QUEUE";

    public boolean beforeRenderBody() {
        return false;
    }

    @SuppressWarnings("unchecked")
    protected void setupRender() {
        List javascriptQueue = (List<Block>) getIOC().getHttpServletRequest().getAttribute(JS_QUEUE);
        if (javascriptQueue == null) {
            javascriptQueue = new LinkedList<Block>();
        }
        synchronized (javascriptQueue) {
            if (getIOC().getResources().hasBody()) {
                javascriptQueue.add(getIOC().getResources().getBody());
            }
            getIOC().getHttpServletRequest().setAttribute(JS_QUEUE, javascriptQueue);
        }
    }

}