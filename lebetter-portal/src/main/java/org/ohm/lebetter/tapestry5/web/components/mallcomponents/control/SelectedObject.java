package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.spring.service.SitemapAwareManager;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.GenericManager;

public class SelectedObject extends GrantCheck {

    @Parameter(name = "notfound",
               defaultPrefix = BindingConstants.LITERAL,
               value = "block:notfounddefault")
    private Block notfound;

    @Parameter(name = "deleted",
               defaultPrefix = BindingConstants.LITERAL,
               value = "block:deleteddefault")
    private Block deleted;

    @Parameter
    @Property(write = false)
    private Boolean showControls = false;

    @Parameter
    private Boolean lockObject = false;

    @Parameter(allowNull = false, value = "false")
    @Property(write = false)
    private Boolean increaseViewCount = false;

    @Parameter(allowNull = false, value = "false")
    private Boolean addAdv = false;

    private String idStr;
    private GenericManager objectManager;

    public void setObjectManager(GenericManager objectManager) {
        this.objectManager = objectManager;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    @Cached
    public Object[] getAbsrtactObjectURL() {
        return new Object[]{getObject().getEntityCode(), getObject().getId()};
    }

    @Override
    protected Object setupRender() {
        Object result;

        //Object not found
        if (getObject() == null) {
            getRMLogger().info("Object not found. " + objectManager + " " + idStr);
            setSatisfied(false);
            result = notfound;
            return result;
        }

        //Object deleted
        if (getObject().isObjectDeleted()) {
            getRMLogger().info("Object deleted. " + objectManager + " " + idStr, getObject());
            setSatisfied(false);
            result = deleted;
            return result;
        }

        //Object translated - not root
        if (!getObject().isRootObject()) {
            getRMLogger().info("Object is not root. " + objectManager + " " + idStr, getObject());
            setSatisfied(false);
            result = notfound;
            return result;
        }

        //Set default value to Roles
        if (StringUtils.isEmpty(getRoles()) && StringUtils.isEmpty(getAction())) {
            super.setRoles("ALL");
        }

        return super.setupRender();
    }

    private ObjectBaseEntity getById(String idStr, GenericManager objectManager) {
        long id;
        try {
            id = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
            return null;
        }
        return objectManager.get(id);
    }

    @SuppressWarnings("unchecked")
    public ObjectBaseEntity findSelectedObject() {

        if (getObject() == null && !StringUtils.isBlank(idStr) && objectManager != null) {

            ObjectBaseEntity obe;

            if (objectManager instanceof SitemapAwareManager) {
                obe = ((SitemapAwareManager) objectManager).getByAltId(idStr);
            } else {
                obe = getById(idStr, objectManager);
            }

            if (obe == null) {
                return null;
            }

            obe = objectManager.getDuplicate(obe.getId());

            try {
                setObject(obe);
                getRMLogger().debug("selectedObject: " + getObject(), getObject());
                if (getObject() == null) {
                    getRMLogger().error("Error getting object with with id: " + idStr +
                                        ". Manager " + objectManager, getObject());
                    return null;
                }

            } catch (Exception e) {
                getRMLogger().error("Error getting object with with id: " + idStr +
                                    ". Manager " + objectManager, getObject(), e);
                return null;
            }
        }

        return getObject();
    }

}