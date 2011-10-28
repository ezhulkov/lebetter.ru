package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.model.impl.entities.LanguageEntity;
import org.room13.mallcore.spring.service.LanguageManager;
import org.room13.mallcore.spring.service.ObjectExistsException;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;

import java.security.AccessControlException;
import java.util.List;

import static org.room13.mallcore.spring.service.DataManager.FileNames.SMALL_AVATAR_FILE;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 12:19:49
 * To change this template use File | Settings | File Templates.
 */
public class EditObjectShow extends AbstractEditObject {

    private LanguageEntity editLanguage;

    @Property
    private LanguageEntity oneLang;

    public String getLangPicClass() {
        return getEditLanguage().getCode().equals(oneLang.getCode()) ? "sel" : "";
    }

    public String getRootLangPicClass() {
        return getSelectedRootObject().getLanguage().getCode().equals(oneLang.getCode()) ? "selroot" : "";
    }

    public String getLangAvatar() {
        return getServiceFacade().getDataManager().getDataFullURL(oneLang, SMALL_AVATAR_FILE);
    }

    public List<LanguageEntity> getAllReadyLangs() {
        return getServiceFacade().getLanguageManager().getAllReady();
    }

    private ObjectBaseEntity getLocalizedSelectedObject(LanguageEntity lang) {

        ObjectBaseEntity obe;
        if (!getObjectManager().exists(getSelectedObject().getRootId(), lang.getCode())) {
            //Create new localized instance of object
            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Creating new translated object for lang " + lang.getCode(),
                                    getSelectedObject());
            }
            obe = getObjectManager().createL(getSelectedObject(), lang.getCode(), getAuth().getUser());
        } else {
            //Get existent localized object
            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Getting existent translated object for lang " + lang.getCode(),
                                    getSelectedObject());
            }
            obe = getObjectManager().getL(getSelectedObject().getRootId(), lang.getCode());
        }
        return obe;
    }

    public Block onActionFromSetEditLangAjax(Long lid) {

        if (lid == null) {
            return getEditAreaBlock();
        }

        LanguageEntity lang = getServiceFacade().getLanguageManager().get(lid);
        ObjectBaseEntity obe = getLocalizedSelectedObject(lang);

        setSelectedObject(obe);
        getEditObjectShow().setEditLanguage(lang);
        getEditObjectShow().setSelectedObject(obe);

        return getEditAreaBlock();
    }

    @OnEvent(value = "submit", component = "editObjectForm")
    @SuppressWarnings("unchecked")
    public Object onSaveSubmit(Long lid) throws Exception {

        getRMLogger().info("Entered save method for object", getSelectedObject());

        FlashMessage msg = null;
        ObjectBaseEntity obe = null;
        try {

            //Get language of editing
            LanguageEntity lang = getServiceFacade().getLanguageManager().get(lid);

            if (!getSelectedObject().getLanguage().getId().equals(lang.getId())) {
                //Get localized object
                obe = getLocalizedSelectedObject(lang);
                //Copy fields to localized object
                BeanUtils.copyProperties(getSelectedObject(), obe, getObjectManager().getTranslatableClass());
            } else {
                obe = getSelectedObject();
            }

            //Call custom callback function
            if (getSubmitCallback() != null && !getSubmitCallback().onFormSubmit(obe)) {
                return getEditAreaBlock();
            }

            if (getBase().containsFlashMessage(FlashMessage.Type.FAILURE)) {
                return getEditAreaBlock();
            }


            getObjectManager().save(obe, getAuth().getUser());
            getRMLogger().info("Object saved.", obe);

            if (getSubmitCallback() != null && !getSubmitCallback().onPostFormSubmit(obe)) {
                return getEditAreaBlock();
            }

            //Set parameters to component to draw form correctly after AJAX submit
            getEditObjectShow().setEditLanguage(lang);
            getEditObjectShow().setSelectedObject(obe);

        } catch (ObjectExistsException ex) {
            getRMLogger().errorObjectExists("Error adding object. Exists", obe);
            msg = new FlashMessage(getBase().getText(getObjectExistsLabel()), FlashMessage.Type.FAILURE);
        } catch (DataIntegrityViolationException cve) {
            getRMLogger().errorObjectExists("Error saving object 1", obe);
            msg = new FlashMessage(getBase().getText(getObjectExistsLabel()), FlashMessage.Type.FAILURE);
        } catch (AccessControlException acex) {
            getRMLogger().errorSecurityViolation("Error saving object 2", obe);
            msg = new FlashMessage(getBase().getText("error.access.denied"), FlashMessage.Type.FAILURE);
        } catch (Exception ex2) {
            getRMLogger().error("Error saving object 3", obe, ex2);
            msg = new FlashMessage(getBase().getText("error.generic"), FlashMessage.Type.FAILURE);
        }

        if (msg != null) {
            getBase().addFlashToSession(msg.getMessage(), msg.getType());
            return getEditAreaBlock();
        }

        getBase().addFlashToSession(getBase().getText(getObjectSavedLabel()), FlashMessage.Type.SUCCESS);

        if (getMultiZoneUpdate() != null) {
            return getMultiZoneUpdate().add("editAreaZone", getEditAreaBlock());
        } else {
            return getEditAreaBlock();
        }

    }

    @OnEvent(value = "submit", component = "addObjectFormAjax")
    @SuppressWarnings("unchecked")
    public Object onAddSubmitAjax() throws Exception {
        return onAddSubmit();
    }

    @OnEvent(value = "submit", component = "addObjectForm")
    @SuppressWarnings("unchecked")
    public Object onAddSubmit() throws Exception {

        getRMLogger().info("Entered add method for object", getSelectedObject());

        FlashMessage msg = null;
        try {

            if (getSubmitCallback() != null && !getSubmitCallback().onFormSubmit(getSelectedObject())) {
                return getAjaxBlock() == null ? null : getEditAreaBlock();
            }

            //Create object
            getObjectManager().create(getSelectedObject(), getParentObject(), getAuth().getUser());
            getRMLogger().info("Object created.", getSelectedObject());

            if (getSubmitCallback() != null && !getSubmitCallback().onPostFormSubmit(getSelectedObject())) {
                return getEditAreaBlock();
            }

        } catch (ObjectExistsException ex) {
            getRMLogger().errorObjectExists("Error adding object. Exists", getSelectedObject());
            msg = new FlashMessage(getBase().getText(getObjectExistsLabel()), FlashMessage.Type.FAILURE);
        } catch (DataIntegrityViolationException cve) {
            getRMLogger().errorObjectExists("Error adding object. Exists", getSelectedObject());
            msg = new FlashMessage(getBase().getText(getObjectExistsLabel()), FlashMessage.Type.FAILURE);
        } catch (AccessControlException acex) {
            getRMLogger().errorSecurityViolation("Error adding object. Access denied", getSelectedObject());
            msg = new FlashMessage(getBase().getText("error.access.denied"), FlashMessage.Type.FAILURE);
        } catch (Exception ex2) {
            getRMLogger().error("Error adding object.", getSelectedObject(), ex2);
            msg = new FlashMessage(getBase().getText("error.generic"), FlashMessage.Type.FAILURE);
        }

        if (msg != null) {
            getBase().addFlashToSession(msg.getMessage(), msg.getType());
            return null;
        }

        getBase().addFlashToSession(getBase().getText(getObjectAddedLabel()), FlashMessage.Type.SUCCESS);
        if (getAjaxBlock() == null) {
            if (getObjectPage() != null) {
                if (isObjectPagePermanent()) {
                    getIOC().getResponse().sendRedirect(getObjectPage());
                } else {
                    getIOC().getResponse().sendRedirect(getObjectPage() + "/" +
                                                        getSelectedObject().getRootId());
                }
            }
        } else {
            return getAjaxBlock();
        }

        return null;

    }

    @SuppressWarnings("unchecked")
    public void onActionFromDelObject(String listPageStr, Long obId) throws Exception {

        if (getSelectedObject() == null) {
            setSelectedObject(getObjectManager().getRootDuplicate(obId));
        } else if (!getSelectedObject().isRootObject()) {
            setSelectedObject(getObjectManager().getRootDuplicate(getSelectedObject().getId()));
        }

        String listPageLocal = getListPage() == null ? listPageStr : getListPage();

        getRMLogger().info("Entered del method for object", getSelectedObject());

        try {
            getObjectManager().remove(getSelectedObjectRoot(), getAuth().getUser());
        } catch (AccessControlException acex) {
            getBase().addFlashToSession(getBase().getText("error.access.denied"), FlashMessage.Type.FAILURE);
            return;
        } catch (Exception ex) {
            getRMLogger().error("Error deleting object.", getSelectedRootObject(), ex);
            getBase().addFlashToSession(getBase().getText("error.remove"), FlashMessage.Type.FAILURE);
            return;
        }

        getRMLogger().info("Object deleted", getSelectedObject());

        getIOC().getResponse().sendRedirect(listPageLocal);

    }

    @Cached
    public ObjectBaseEntity getSelectedRootObject() {
        if (getSelectedObject() == null ||
            getSelectedObject().getId() == null ||
            getSelectedObject().getId().equals(new Long(0))) {
            return getSelectedObject();
        }
        if (getSelectedObject().isRootObject()) {
            return getSelectedObject();
        }
        return getObjectManager().getRootDuplicate(getSelectedObject().getRootId());
    }

    public LanguageEntity getEditLanguage() {
        LanguageManager languageManager = getServiceFacade().getLanguageManager();

        if (getSelectedRootObject() == null) {
            return null;
        }

        if (editLanguage == null) {
            editLanguage = languageManager.getDuplicate(getSelectedRootObject().getLanguage().getRootId());
        } else {
            editLanguage = languageManager.getDuplicate(editLanguage.getRootId());
        }


        return editLanguage;
    }

    public void setEditLanguage(LanguageEntity editLanguage) {
        this.editLanguage = editLanguage;
    }

    public Long getSaveContext() {
        return getEditLanguage() == null ? null : getEditLanguage().getRootId();
    }

    public Object[] getDelContext() {
        return new Object[]{getListPage(),
                            getSelectedObject() == null ? null : getSelectedObject().getRootId()};
    }

    public String getBackDiv() {
        return "<div class=\"fancybox-overlay\"></div>";
    }

}