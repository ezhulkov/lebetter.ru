package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.OwnerAware;
import org.room13.mallcore.spring.service.OwnerAwareManager;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 26.09.11
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public class ObjectOwnerEdit extends AbstractBaseComponent {

    @Inject
    @Property
    private Block ownerBlock;

    @Property
    @Parameter(name = "object", required = false, allowNull = false)
    private OwnerAware object = null;

    @Property
    @Parameter(name = "objectManager", required = false, allowNull = false)
    private OwnerAwareManager objectManager;

    @Property
    @Parameter(name = "role", required = false, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String role;

    @Property
    @Parameter(name = "action", required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String action;

    @InjectComponent
    private ObjectOwnerShow objectOwnerShow;

}
