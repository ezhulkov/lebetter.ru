package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.system;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextArea;
import org.apache.tapestry5.corelib.components.TextField;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;
import org.ohm.lebetter.tapestry5.web.components.base.EditObjectCallback;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.SelectMultiple;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericMultiValueEncoder;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericSelectModel;
import org.ohm.lebetter.tapestry5.web.services.impl.GenericStatusSelectModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CategoryEdit extends AbstractEditComponent {

    @Parameter(name = "parent", required = false, allowNull = false)
    private CategoryEntity parent;

    @Property
    private PropertyEntity oneProperty;

    @Component(id = "name", parameters = {"value=selectedObject.name", "validate=required,maxlength=64"})
    private TextField nameField;

    @Component(id = "tomainname", parameters = {"value=selectedObject.tomainname", "validate=maxlength=32"})
    private TextField tomainnameField;

    @Component(id = "tomain", parameters = {"value=selectedObject.tomain"})
    private Checkbox tomainField;

    @Component(id = "hidemain", parameters = {"value=selectedObject.hidemain"})
    private Checkbox hidemainField;

    @Component(id = "code", parameters = {"value=selectedObject.code", "validate=required,maxlength=64"})
    private TextField codeField;

    @Component(id = "description", parameters = {"value=selectedObject.description", "validate=maxlength=256"})
    private TextArea descriptionField;

    @Component(id = "position", parameters = {"value=selectedObject.position"})
    private TextField positionField;

    @Property
    private GenericStatusSelectModel statusSelectModel = null;

    private List<PropertyEntity> categoryProperties;

    @Component(id = "status", parameters = {
            "model=statusSelectModel",
            "encoder=statusSelectModel",
            "value=selectedObject.objectStatus",
            "validate=required"})
    private Select statusField;

    @Component(id = "properties", parameters = {
            "model=propertyModel",
            "encoder=multiValueEncoder",
            "values=categoryProperties",
            "validate=required"})
    private SelectMultiple categoryPropertiesField;

    public void setCategoryProperties(List<PropertyEntity> categories) {
        categoryProperties = categories;
    }

    public List<PropertyEntity> getCategoryProperties() {
        return getServiceFacade().getCategoryManager().getAllProperties(getSelectedObjectRoot());
    }

    @Property
    private GenericSelectModel<PropertyEntity> propertyModel = null;

    @Property
    private GenericSelectModel<PropertyEntity> categoryPropertyModel = null;

    @Property
    private GenericMultiValueEncoder multiValueEncoder = null;

    public void onPrepare() throws Exception {

        statusSelectModel = new GenericStatusSelectModel(getAuth().getUser(),
                                                         getServiceFacade().getRoleManager(),
                                                         getSelectedObjectRoot(),
                                                         getIOC().getPropertyAccess());

        PropertyManager propertyManager = getServiceFacade().getPropertyManager();
        CategoryManager categoryManager = getServiceFacade().getCategoryManager();
        List<PropertyEntity> allCategories = propertyManager.getAllReadyRootProperties();
        List<PropertyEntity> allCategoriesL = propertyManager.getTranslated(allCategories);
        List<PropertyEntity> categoryProperties = categoryManager.getAllProperties(getSelectedObjectRoot());
        List<PropertyEntity> categoryPropertiesL = propertyManager.getTranslated(categoryProperties);

        if (propertyModel == null) {
            propertyModel = new GenericSelectModel<PropertyEntity>(allCategories, allCategoriesL,
                                                                   PropertyEntity.class,
                                                                   "name",
                                                                   "rootId",
                                                                   getIOC().getPropertyAccess());
        }

        if (categoryPropertyModel == null) {
            categoryPropertyModel = new GenericSelectModel<PropertyEntity>(categoryProperties,
                                                                           categoryPropertiesL,
                                                                           PropertyEntity.class,
                                                                           "name",
                                                                           "rootId",
                                                                           getIOC().getPropertyAccess());
        }

        if (multiValueEncoder == null) {
            multiValueEncoder = new GenericMultiValueEncoder<PropertyEntity>(allCategories,
                                                                             allCategoriesL, "rootId");
        }

    }

    public EditObjectCallback getCallback() {
        return new Callback();
    }

    public List<PropertyEntity> getSearchOrder() {
        List<PropertyEntity> result;
        result = getServiceFacade().getCategoryManager().getAllProperties(getSelectedObjectRoot());
        return getServiceFacade().getPropertyManager().getTranslated(result);
    }

    public class Callback implements EditObjectCallback<CategoryEntity> {

        @Override
        public boolean onPostFormSubmit(CategoryEntity object) throws Exception {
            return true;
        }

        @Override
        public boolean onFormSubmit(CategoryEntity object) throws Exception {

            if (getRMLogger().isDebugEnabled()) {
                getRMLogger().debug("Entered hook for category create method", object);
            }

            //Get selected categories
            if (categoryProperties != null && categoryProperties.size() != 0) {
                List<Long> selectedCatIds = new ArrayList<Long>(categoryProperties.size());
                for (PropertyEntity categoryProperty : categoryProperties) {
                    selectedCatIds.add(categoryProperty.getRootId());
                }

                //Get desired order of categories
                String order = getIOC().getRequest().getParameter("2RM-SORDER");
                String[] entries = order.split("-");
                List<Long> newOrderList = new LinkedList<Long>();
                if (entries != null) {
                    for (String entry : entries) {
                        try {
                            if (!StringUtils.isEmpty(entry)) {
                                Long cid = Long.parseLong(entry);
                                newOrderList.add(cid);
                            }
                        } catch (Exception ex) {
                            getRMLogger().error("Error whine processing search order.",
                                                getSelectedObjectRoot(), ex);
                            return false;
                        }
                    }
                }

                try {
                    CategoryManager categoryManager = getServiceFacade().getCategoryManager();
                    categoryManager.setNewSearchChain(getSelectedObjectRoot(),
                                                      selectedCatIds,
                                                      newOrderList,
                                                      getAuth().getUser());
                } catch (Exception ex) {
                    getRMLogger().error("Error setting search order chain", getSelectedObjectRoot(), ex);
                    getBase().addFlashToSession(getBase().getText("error.generic"),
                                                FlashMessage.Type.FAILURE);
                    return false;
                }
            }

            return true;
        }

    }

    public CategoryEntity getSelectedObject() {
        return (CategoryEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(CategoryEntity object) {
        setSelectedObjectInternal(object);
    }

    public CategoryEntity getSelectedObjectRoot() {
        return (CategoryEntity) getSelectedRootObjectInternal();
    }

    public boolean isLeaf() {
        return getServiceFacade().getCategoryManager().getAllCategoriesCount(getSelectedObject()) == 0;
    }

}