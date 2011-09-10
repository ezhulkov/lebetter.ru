package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Loop;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.room13.mallcore.util.MessageUtil;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 02.07.2009
 * Time: 13:46:13
 * To change this template use File | Settings | File Templates.
 */
public class LoopMore implements ClientElement {

    @Environmental
    private RenderSupport renderSupport;

    private String _assignedClientId;

    @Parameter(value = "prop:componentResources.id", defaultPrefix = "literal")
    private String clientId;

    @Property
    @Parameter
    private int loopIndex;

    /**
     * The element to render. If not null, then the loop will render
     * the indicated element around its body (on each pass through the loop).
     * The default is derived from the component template.
     */
    @Parameter(value = "prop:componentResources.elementName", defaultPrefix = "literal")
    private String elementName;

    /**
     * Defines the collection of values for the loop to iterate over.
     */
    @SuppressWarnings("unused")
    @Parameter(required = true)
    private Iterable<?> source;

    /**
     * The current value, set before the component renders its body.
     */
    @SuppressWarnings("unused")
    @Parameter
    private Object _value;

    @SuppressWarnings("unused")
    @Component(parameters = {"source=inherit:source",
                             "element=prop:elementName", "value=inherit:value",
                             "volatile=literal:true", "encoder=inherit:encoder",
                             "index=inherit:index"})
    private Loop _loop;

    /**
     * Optional primary key converter; if provided and inside a form and not volatile, then each
     * iterated value is converted and stored into the form.
     */
    @SuppressWarnings("unused")
    @Parameter
    private PrimaryKeyEncoder<?, ?> _encoder;

    @Parameter(required = false, allowNull = false, value = "literal:3")
    private int showCount;

    @Property
    @Parameter(value = "block:morelink")
    private Block moreLink;

    @Property
    @Parameter(value = "block:empty")
    private Block empty;

    @Property
    @Parameter(value = "block:delimiter")
    private Block delimiter;

    @Inject
    private Messages messages;

    private int curCount;
    private int item;

    //    @Property
    //    @Parameter
    //    private boolean exceeds = false;

    /**
     * Returns a unique id for the element. This value will be unique for any given rendering of a
     * page. This value is intended for use as the id attribute of the client-side element, and will
     * be used with any DHTML/Ajax related JavaScript.
     */
    public String getClientId() {
        return _assignedClientId;
    }

    public Object setupRender() {
        curCount = showCount;
        item = 0;
        _assignedClientId = renderSupport.allocateClientId(clientId);
        Iterator it = source.iterator();
        if (!it.hasNext()) {
            return empty;
        }
        return null;
    }

    public boolean getCurCount() {
        loopIndex++;
        return --curCount >= 0;
    }

    public String getText(String key) {
        return MessageUtil.format(messages.get(key));
    }

    public String getElementName() {
        return elementName;
    }

    public boolean getDrawMore() {
        return curCount + 1 >= 0;
    }

    public boolean getDrawDelimiter() {
        if (source instanceof Collection) {
            Collection sourceCollection = (Collection) source;
            item++;
            if (item == sourceCollection.size() || (item == showCount && showCount != -1 && curCount > 0)) {
                return false;
            }
        }
        return true;
    }

}
