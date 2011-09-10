package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;


public class Flash extends AbstractBaseComponent {

    @Parameter(required = true, allowNull = true)
    private FlashMessage object;

    @Inject
    private Messages messages;

    public boolean isFailer() {
        return isFlashSet() && object.getType().equals(FlashMessage.Type.FAILURE);
    }

    public boolean isSuccess() {
        return isFlashSet() && object.getType().equals(FlashMessage.Type.SUCCESS);
    }

    public boolean isFlashSet() {
        return object != null && !"".equals(object.getMessage());
    }

    public String getMessage() {
        if (object == null) {
            return "";
        }
        return object.getMessage();
    }

}
