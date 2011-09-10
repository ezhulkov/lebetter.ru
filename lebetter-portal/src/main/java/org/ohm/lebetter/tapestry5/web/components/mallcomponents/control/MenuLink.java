package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractGrantAwareComponent;
import org.room13.mallcore.model.CreatorAware;
import org.room13.mallcore.model.ObjectBaseEntity;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 17:47:04
 * To change this template use File | Settings | File Templates.
 */
public class MenuLink extends AbstractGrantAwareComponent {

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String link;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String context;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private boolean atag = false;

    @Parameter(required = false, allowNull = false,
               defaultPrefix = BindingConstants.LITERAL)
    private boolean ownerAlwaysAllowed = false;

    private boolean needToDraw = false;

    boolean beginRender(MarkupWriter writer) {
        needToDraw = authReqsSatisfied() ||
                     ownerAlwaysAllowed && isOwner(getObject());
        if (needToDraw && atag) {
            writer.element("a", "href", link + (context == null ? "" : ("/" + context)));
        }
        return needToDraw;
    }

    private boolean isOwner(ObjectBaseEntity object) {
        return object instanceof CreatorAware &&
               ((CreatorAware) object).getCreator() != null &&
               ((CreatorAware) object).getCreator().equals(getAuth().getUser());
    }

    void afterRender(MarkupWriter writer) {
        if (needToDraw && atag) {
            writer.end();
        }
    }

}
