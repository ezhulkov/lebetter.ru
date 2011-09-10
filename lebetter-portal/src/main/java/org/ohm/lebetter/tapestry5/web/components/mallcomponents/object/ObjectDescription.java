package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.model.DescriptionAware;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.room13.mallcore.model.ObjectBaseEntity;

import java.security.AccessControlException;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 19.10.2010
 * Time: 11:27:48
 * To change this template use File | Settings | File Templates.
 */
@Import(library = {"proxy:/scripts/lb-categories.js"})
public class ObjectDescription extends AbstractBaseComponent {

    @Property
    @Inject
    private Block tagBlock;

    @Parameter(name = "object", required = true, allowNull = true)
    private DescriptionAware selectedObject;

    @Parameter(name = "securityObject", required = false, allowNull = true, value = "prop:selectedObject")
    private ObjectBaseEntity selectedSecurityObject;

    public DescriptionAware getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(DescriptionAware selectedObject) {
        this.selectedObject = selectedObject;
    }

    public ObjectBaseEntity getSelectedSecurityObject() {
        return selectedSecurityObject == null ? selectedObject : selectedSecurityObject;
    }

    @Cached
    public Object[] getAbsrtactObjectURL() {
        return new Object[]{selectedObject.getEntityCode(), selectedObject.getId()};
    }

    public Object onActionFromClearTagsAjax(String entityCode, Long id) {

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Entered clear tags for object method. Code: " + entityCode + ", id: " + id);
        }

        ProductEntity product = getServiceFacade().getProductManager().get(id);
        try {

            getServiceFacade().getPropertyManager().clearTagsForProduct(product, getAuth().getUser());
            getBase().addFlashToSession(getBase().getText("tags.cleared"), FlashMessage.Type.SUCCESS);

        } catch (AccessControlException acex) {
            getRMLogger().errorSecurityViolation("Access denied", product);
            getBase().addFlashToSession(getBase().getText("error.access.denied"), FlashMessage.Type.FAILURE);
        } catch (Exception ex2) {
            getRMLogger().error("Error clearing tags for object " + selectedObject, ex2);
            getBase().addFlashToSession(getBase().getText("error.generic"), FlashMessage.Type.FAILURE);
        }

        return tagBlock;

    }

}
