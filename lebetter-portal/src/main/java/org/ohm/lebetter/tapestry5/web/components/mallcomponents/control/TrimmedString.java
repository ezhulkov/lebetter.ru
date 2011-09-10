package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.util.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 17:47:04
 * To change this template use File | Settings | File Templates.
 */
public class TrimmedString extends AbstractBaseComponent {

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String value;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "8")
    private int length;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "...")
    private String suffix;

    void beginRender(MarkupWriter writer) {
        if (!StringUtil.isEmpty(value)) {
            if (value.length() > length) {
                writer.writeRaw(StringUtil.transformHTML(value.substring(0, length) + suffix).trim());
            } else {
                writer.writeRaw(StringUtil.transformHTML(value).trim());
            }
        }
    }


}