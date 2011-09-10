package org.ohm.lebetter.tapestry5.web.mixins;

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
@Import(library = {"classpath:/org/room13/kinder/tapestry5/web/mixins/OnEvent.js"})
public class OnEvent {
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String event;
    @Parameter
    private Object[] context;
    @Environmental
    private RenderSupport renderSupport;
    @InjectContainer
    private ClientElement container;
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String callback;
    @Inject
    private ComponentResources componentResources;

    void afterRender() {
        Link link = componentResources.createEventLink(event, context);
        String script = "new OnEvent('%s', '%s', '%s', '%s')";
        renderSupport.addScript(script, container.getClientId(), event, link.toRedirectURI(), callback);
    }
}