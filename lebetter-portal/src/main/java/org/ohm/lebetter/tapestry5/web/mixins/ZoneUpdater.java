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
@Import(library = "classpath:/org/room13/kinder/tapestry5/web/mixins/ZoneUpdater.js")
public class ZoneUpdater {

    public static final String PLACEHOLDER = "XXX";
    @Inject
    private ComponentResources resources;

    @Environmental
    private RenderSupport renderSupport;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String clientEvent;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private String event;

    @InjectContainer
    private ClientElement element;

    @Parameter
    private Object[] context;

    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    // To enable popups to fire events on this document, enter "document" here.
    private String listeningElement;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
    private String zone;

    protected Link createLink(Object[] context) {
        if (context == null) {
            context = new Object[1];
        }
        context = ArrayUtils.add(context, PLACEHOLDER); // To be replaced by javascript
        return resources.createEventLink(event, context);
    }

    void afterRender() {
        String link = createLink(context).toURI();
        String elementId = element.getClientId();
        if (clientEvent == null) {
            clientEvent = event;
        }
        if (listeningElement == null) {
            listeningElement = "$('" + elementId + "')";
        }
        renderSupport.addScript("new ZoneUpdater('%s', %s, '%s', '%s', '%s', '%s')",
                                elementId, listeningElement, clientEvent, link, zone, PLACEHOLDER);
    }
}