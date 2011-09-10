package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ajax.MultiZoneUpdate;
import org.apache.tapestry5.annotations.Parameter;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.components.base.EditObjectCallback;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.GenericManager;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 12:19:49
 * To change this template use File | Settings | File Templates.
 */
public class AbstractEditObject extends AbstractBaseComponent {

    @Parameter(name = "key", allowNull = true, required = false, value = "literal:main")
    private String key;

    @Parameter(name = "editAreaBlock", allowNull = false, required = false)
    private Block editAreaBlock;

    @Parameter(name = "multiZoneUpdate", allowNull = true, required = false)
    private MultiZoneUpdate multiZoneUpdate;

    @Parameter(name = "roles", allowNull = true, required = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String roles;

    @Parameter(name = "editObjectShow", allowNull = false, required = false)
    private EditObjectShow editObjectShow;

    @Parameter(name = "addAction", allowNull = true, required = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String addAction = null;

    @Parameter(name = "delAction", allowNull = true, required = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String delAction = null;

    @Parameter(name = "editAction", allowNull = true, required = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String editAction = null;

    @Parameter(name = "selectedObject", allowNull = false, required = true)
    private ObjectBaseEntity selectedObject = null;

    @Parameter(name = "selectedSecurityObject", allowNull = false, required = false)
    private ObjectBaseEntity selectedSecurityObject = null;

    @Parameter(name = "objectManager", allowNull = false, required = true)
    private GenericManager objectManager;

    @Parameter(name = "listPage", allowNull = false, required = false)
    private String listPage;

    @Parameter(name = "objectPage", allowNull = true, required = false)
    private String objectPage;

    @Parameter(name = "objectPagePermanent", allowNull = true, required = false, value = "literal:false")
    private boolean objectPagePermanent;

    @Parameter(name = "newObject", allowNull = false, required = false, value = "literal:false")
    private boolean newObject;

    @Parameter(name = "drawDelete", allowNull = false, required = false, value = "literal:true")
    private boolean drawDelete;

    @Parameter(name = "drawLang", allowNull = false, required = false, value = "literal:true")
    private boolean drawLang;

    @Parameter(name = "drawLock", allowNull = false, required = false, value = "literal:true")
    private boolean drawLock;

    @Parameter(name = "submitCallback", allowNull = true, required = false)
    private EditObjectCallback submitCallback = null;

    @Parameter(name = "parentObject", allowNull = true, required = false)
    private ObjectBaseEntity parentObject = null;

    @Parameter(name = "objectExistsLabel", allowNull = false, required = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String objectExistsLabel = null;

    @Parameter(name = "objectAddedLabel", allowNull = false, required = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String objectAddedLabel = null;

    @Parameter(name = "objectRemovedLabel", allowNull = false, required = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String objectRemovedLabel = null;

    @Parameter(name = "objectSavedLabel", allowNull = false, required = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String objectSavedLabel = null;

    public String getObjectAddedLabel() {
        return objectAddedLabel;
    }

    public void setObjectAddedLabel(String objectAddedLabel) {
        this.objectAddedLabel = objectAddedLabel;
    }

    public String getObjectSavedLabel() {
        return objectSavedLabel;
    }

    public void setObjectSavedLabel(String objectSavedLabel) {
        this.objectSavedLabel = objectSavedLabel;
    }

    public String getObjectExistsLabel() {
        return objectExistsLabel;
    }

    public void setObjectExistsLabel(String objectExistsLabel) {
        this.objectExistsLabel = objectExistsLabel;
    }

    public ObjectBaseEntity getParentObject() {
        return parentObject;
    }

    public void setParentObject(ObjectBaseEntity parentObject) {
        this.parentObject = parentObject;
    }

    public String getAddAction() {
        return addAction;
    }

    public void setAddAction(String addAction) {
        this.addAction = addAction;
    }

    public String getDelAction() {
        return delAction;
    }

    public void setDelAction(String delAction) {
        this.delAction = delAction;
    }

    public String getEditAction() {
        return editAction;
    }

    public void setEditAction(String editAction) {
        this.editAction = editAction;
    }

    public ObjectBaseEntity getSelectedObject() {
        return selectedObject;
    }

    public void setSelectedObject(ObjectBaseEntity selectedObject) {
        this.selectedObject = selectedObject;
    }

    public ObjectBaseEntity getSelectedObjectRoot() {
        return getObjectManager().getRootDuplicate(getSelectedObject().getId());
    }

    public ObjectBaseEntity getSelectedSecurityObject() {
        if (selectedSecurityObject != null) {
            return selectedSecurityObject;
        } else {
            if (selectedObject.getId() == null) {
                return selectedObject;
            } else {
                return getObjectManager().getRootDuplicate(selectedObject.getId());
            }
        }
    }

    public void setSelectedSecurityObject(ObjectBaseEntity selectedSecurityObject) {
        this.selectedSecurityObject = selectedSecurityObject;
    }

    public GenericManager getObjectManager() {
        return objectManager;
    }

    public void setObjectManager(GenericManager objectManager) {
        this.objectManager = objectManager;
    }

    public String getListPage() {
        return listPage;
    }

    public void setListPage(String listPage) {
        this.listPage = listPage;
    }

    public String getObjectPage() {
        return objectPage;
    }

    public boolean isObjectPagePermanent() {
        return objectPagePermanent;
    }

    public void setObjectPagePermanent(boolean objectPagePermanent) {
        this.objectPagePermanent = objectPagePermanent;
    }

    public void setObjectPage(String objectPage) {
        this.objectPage = objectPage;
    }

    public String getObjectRemovedLabel() {
        return objectRemovedLabel;
    }

    public void setObjectRemovedLabel(String objectRemovedLabel) {
        this.objectRemovedLabel = objectRemovedLabel;
    }

    public boolean isNewObject() {
        return newObject;
    }

    public void setNewObject(boolean newObject) {
        this.newObject = newObject;
    }

    public Block getEditAreaBlock() {
        return editAreaBlock;
    }

    public MultiZoneUpdate getMultiZoneUpdate() {
        return multiZoneUpdate;
    }

    public void setMultiZoneUpdate(MultiZoneUpdate multiZoneUpdate) {
        this.multiZoneUpdate = multiZoneUpdate;
    }

    public void setEditAreaBlock(Block editAreaBlock) {
        this.editAreaBlock = editAreaBlock;
    }

    public EditObjectShow getEditObjectShow() {
        return editObjectShow;
    }

    public void setEditObjectShow(EditObjectShow editObjectShow) {
        this.editObjectShow = editObjectShow;
    }

    public EditObjectCallback getSubmitCallback() {
        return submitCallback;
    }

    public void setSubmitCallback(EditObjectCallback submitCallback) {
        this.submitCallback = submitCallback;
    }

    public boolean isDrawDelete() {
        return drawDelete;
    }

    public void setDrawDelete(boolean drawDelete) {
        this.drawDelete = drawDelete;
    }

    public boolean isDrawLang() {
        return drawLang;
    }

    public void setDrawLang(boolean drawLang) {
        this.drawLang = drawLang;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isDrawLock() {
        return drawLock;
    }

    public void setDrawLock(boolean drawLock) {
        this.drawLock = drawLock;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    private boolean isUserMatches() {
        if (getRoles() != null) {
            String[] rolesStr = getRoles().split(",");
            for (String role : rolesStr) {
                if (getServiceFacade().getRoleManager().isRoleAssigned(getAuth().getUser(), role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAddAllowed() {
        if (isUserMatches()) {
            return true;
        }
        return getServiceFacade().getRoleManager().isActionGranted(getAuth().getUser(),
                                                                   getAddAction(),
                                                                   getSelectedSecurityObject());
    }

    public boolean isDelAllowed() {
        if (isUserMatches()) {
            return true;
        }
        return getServiceFacade().getRoleManager().isActionGranted(getAuth().getUser(),
                                                                   getDelAction(),
                                                                   getSelectedSecurityObject());
    }

    public boolean isEditAllowed() {
        if (isUserMatches()) {
            return true;
        }
        return getServiceFacade().getRoleManager().isActionGranted(getAuth().getUser(),
                                                                   getEditAction(),
                                                                   getSelectedSecurityObject());
    }

}