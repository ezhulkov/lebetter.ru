package org.ohm.lebetter.tapestry5.web.components.base;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.corelib.components.Form;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 13:55:23
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractAjaxFormComponent extends AbstractBaseComponent {

    @InjectComponent
    private Form form;

    protected void linkAjaxForm(String zoneName, Form form) {
        if (getAjaxBlock() != null) {
            Link link = getIOC().getResources().createFormEventLink(EventConstants.ACTION);
            getIOC().getRenderSupport().addInit("linkZone",
                    form.getClientId(),
                    zoneName,
                    link.toString() + ".form");
        }
    }

    public void afterRender() {
        linkAjaxForm(getTargetZone(), form);
    }

}