package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.ColorEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;

import java.security.AccessControlException;
import java.util.List;


public class ColorList extends AbstractBaseComponent {

    @Property
    private ColorEntity oneColor;

    @Cached
    public List<ColorEntity> getAllColors() {
        List<Long> ids = getServiceFacade().getColorManager().getAllIds(null, "name");
        return getServiceFacade().getColorManager().getTranslatedByIds(ids);
    }

    public ColorEntity getOneColorRoot() {
        return getServiceFacade().getColorManager().getRoot(oneColor.getId());
    }

    public void onActionFromButtonDelete(Long colorId) {

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Entered del color method: " + colorId);
        }

        //Get product type
        ColorEntity color = getServiceFacade().getColorManager().get(colorId);

        if (getRMLogger().isTraceEnabled()) {
            getRMLogger().trace("Color to delete", color);
        }

        try {
            getServiceFacade().getColorManager().remove(color, getAuth().getUser());
        } catch (AccessControlException ace) {
            getRMLogger().errorSecurityViolation("Access denied", color);
            getBase().addFlashToSession(getBase().getText("error.action.not.granted"),
                                        FlashMessage.Type.FAILURE);
            return;
        } catch (Exception ex) {
            getRMLogger().error("Error deleting object." + color, ex);
            getBase().addFlashToSession(getBase().getText("error.remove"), FlashMessage.Type.FAILURE);
            return;
        }

        getBase().addFlashToSession(getBase().getText("color.removed"), FlashMessage.Type.SUCCESS);

    }

}