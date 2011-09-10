package org.ohm.lebetter.tapestry5.web.mixins;

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 20.01.2010
 * Time: 17:18:38
 * To change this template use File | Settings | File Templates.
 */
@Import(library = {"classpath:/org/room13/kinder/tapestry5/web/mixins/FireHTMLEvent.js"})
public class FireHTMLEvent {

    @Inject
    private ComponentResources resources;

    @Environmental
    private RenderSupport renderSupport;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "click")
    private String clientEvent;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private String event;

    @InjectContainer
    private ClientElement element;

    @Parameter
    private Object[] context;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String listeningElement;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private String zone;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private String selector;

    protected Link createLink(Object[] context) {
        if (context == null) {
            context = new Object[0];
        }
        context = ArrayUtils.add(context, "SELECTOR");
        return resources.createEventLink(event, context);
    }

    public void afterRender() {
        String link = createLink(context).toURI();
        String elementId = element.getClientId();
        if (clientEvent == null) {
            clientEvent = event;
        }
        if (listeningElement == null) {
            listeningElement = "$('" + elementId + "')";
        }
        renderSupport.addScript("new ZoneUpdater('%s', %s, '%s', '%s', '%s', '%s')",
                                elementId, listeningElement, clientEvent, link, zone, selector);
    }

}