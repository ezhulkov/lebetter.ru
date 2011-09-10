package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.corelib.base.AbstractTextField;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 18.03.2009
 * Time: 17:28:18
 * To change this template use File | Settings | File Templates.
 */
public class HashedPasswordField extends AbstractTextField {

    @Override
    protected final void writeFieldTag(MarkupWriter writer, String value) {
        writer.element("input",
                       "type", "password",
                       "name", getControlName(),
                       "id", getClientId(),
                       "value", value,
                       "size", getWidth());
    }

    final void afterRender(MarkupWriter writer) {
        writer.end(); // input
    }
}
