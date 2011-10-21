package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.product;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.impl.Ticket;

import java.util.Date;

public class ProductPhoto extends AbstractBaseComponent {

    @Property
    @Parameter(name = "object", required = false, allowNull = false)
    private ProductEntity selectedObject;

    public String getTicket() {
        return getServiceFacade().getUploadTicketService().lease(new Ticket(
                getAuth().getUser().getRootId(),
                selectedObject.getRootId(),
                selectedObject.getEntityCode(),
                "PRODUCT", new Date()
        ));
    }

}