package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.room13.mallcore.model.CreatorRepAware;
import org.room13.mallcore.model.impl.entities.ObjectOwnerEntity;
import org.room13.mallcore.spring.service.ObjectBadRoleException;
import org.room13.mallcore.spring.service.ObjectIsNullException;
import org.room13.mallcore.spring.service.ObjectNotFoundException;
import org.room13.mallcore.spring.service.OwnerAwareManager;

import java.security.AccessControlException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 26.09.11
 * Time: 13:03
 * To change this template use File | Settings | File Templates.
 */
public class ObjectOwnerShow extends AbstractBaseComponent {

    @Property
    @Parameter(name = "object", required = false, allowNull = false)
    private CreatorRepAware object = null;

    @Property
    @Parameter(name = "objectManager", required = false, allowNull = false)
    private OwnerAwareManager objectManager;

    @Property
    @Parameter(name = "role", required = false, allowNull = true, defaultPrefix = BindingConstants.PROP)
    private String role;

    @Component(id = "owner", parameters = {"value=ownerLogin", "validate=maxlength=64"})
    private TextField ownerField;

    @Component(id = "creatorrep", parameters = {"value=creatorLogin", "validate=maxlength=64"})
    private TextField creatorField;

    @Property
    private String ownerLogin = null;

    @Property
    private String creatorLogin = null;

    @Property
    private ObjectOwnerEntity ownerRef;

    public void beginRender() {
        object = (CreatorRepAware) getServiceFacade().getGenericManagerMap().
                get(object.getEntityCode()).get(object.getId());
    }

    @OnEvent(value = "submit", component = "form")
    @SuppressWarnings("unchecked")
    public Block onAssignSubmit() {

        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Entered set owner/creator for object method. Username: " +
                    ownerLogin, object);
        }

        //try to set owner/creator for object
        Long oid = object.getId();
        try {
            if (!StringUtils.isEmpty(ownerLogin)) {
                UserEntity user = getServiceFacade().getUserManager().getUserByUsername(ownerLogin);
                objectManager.addOwner(object, role, user);
            }
            if (!StringUtils.isEmpty(creatorLogin)) {
                objectManager.setCreatorRep(object, creatorLogin, getAuth().getUser());
                UserEntity user = getServiceFacade().getUserManager().getUserByUsername(creatorLogin);
                object.setCreatorRep(user);
            }
            getBase().addFlashToSession(getBase().getText("owner.assigned"), FlashMessage.Type.SUCCESS);
        } catch (AccessControlException acex) {
            getBase().addFlashToSession(getBase().getText("error.access.denied"), FlashMessage.Type.FAILURE);
        } catch (ObjectNotFoundException onfe) {
            getRMLogger().error("Error setting owner. Not found " + ownerLogin);
            getBase().addFlashToSession(getBase().getText("error.no.such.user"), FlashMessage.Type.FAILURE);
        } catch (ObjectIsNullException onee) {
            getRMLogger().error("Error setting owner. Empty name");
            getBase().addFlashToSession(getBase().getText("error.empty.owner"), FlashMessage.Type.FAILURE);
        } catch (ObjectBadRoleException obre) {
            getRMLogger().error("Error setting owner. Bad role");
            getBase().addFlashToSession(getBase().getText("error.not." + role), FlashMessage.Type.FAILURE);
        }

        object = (CreatorRepAware) objectManager.get(oid);
        getRMLogger().debug("Owner/creator set: " + object.getCreator());

        return getAjaxBlock();

    }

    public Block onActionFromRemoveOwnerAjax(Long uid) {
        objectManager.removeOwner(object, role,
                getServiceFacade().getUserManager().get(uid));
        return getAjaxBlock();
    }

    public List<ObjectOwnerEntity> getOwners() {
        return object.getOwners() != null && object.getOwners().size() > 0 ?
                object.getOwners() : null;
    }

}
