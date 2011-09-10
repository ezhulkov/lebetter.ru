package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractGrantAwareComponent;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 12:19:49
 * To change this template use File | Settings | File Templates.
 */
public class GrantCheck extends AbstractGrantAwareComponent {

    @Parameter(name = "notgranted", value = "block:notgranteddefault",
            defaultPrefix = BindingConstants.LITERAL)
    private Block notgranted;

    private boolean satisfied = false;

    public boolean isSatisfied() {
        return satisfied;
    }

    public void setSatisfied(boolean satisfied) {
        this.satisfied = satisfied;
    }

    public boolean beforeRenderBody() {
        return satisfied;
    }

    protected Object setupRender() {
        satisfied = super.authReqsSatisfied();
        if (!satisfied) {
            getRMLogger().error("GrantCheck returned false for: user(" + getAuth().getUser() +
                    "), action(" + getAction() +
                    "), object(" + getObject() +
                    "), roles(" + getRoles() + ")", getObject());
        }
        return satisfied ? null : notgranted;
    }
}