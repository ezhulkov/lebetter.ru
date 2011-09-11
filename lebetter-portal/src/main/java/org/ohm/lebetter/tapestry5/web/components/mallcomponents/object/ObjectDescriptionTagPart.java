package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.Constants;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.TagToValueEntity;
import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.room13.mallcore.model.ObjectBaseEntity;

import java.security.AccessControlException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.Long.parseLong;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 19.10.2010
 * Time: 11:49:15
 * To change this template use File | Settings | File Templates.
 */
public class ObjectDescriptionTagPart extends AbstractBaseComponent {

    public static final String SELECT_INPUT = "SELECT";
    public static final String VALUE_INPUT = "VALUE";
    public static final String GVALUE_INPUT = "GVALUE";

    @Property
    private PropertyEntity oneProperty;

    @Property
    private TagToValueEntity oneTagLink;

    @Property
    @Parameter(name = "object", required = true, allowNull = true)
    private ProductEntity selectedObject;

    @Parameter(name = "securityObject", required = false, allowNull = true)
    private ObjectBaseEntity selectedSecurityObject;

    public ObjectBaseEntity getSelectedSecurityObject() {
        return selectedSecurityObject == null ? selectedObject : selectedSecurityObject;
    }

    public List<TagToValueEntity> getTagsMultiple() {

        //Get all multiple tags for property
        List<TagToValueEntity> result = new LinkedList<TagToValueEntity>();
        result.addAll(getServiceFacade().getPropertyManager().getTagsForProductByMultiple(selectedObject,
                                                                                          oneProperty,
                                                                                          true));
        //Add unassigned properties to result list
        if (result.size() == 0) {
            TagToValueEntity stabLink = new TagToValueEntity();
            PropertyValueEntity stabPropVal = new PropertyValueEntity();
            stabPropVal.setProperty(oneProperty);
            stabLink.setPropertyValue(stabPropVal);
            result.add(stabLink);
        }

        Collections.sort(result, CategoryManager.TAG_ENTITY_COMPARATOR);

        return result;
    }

    public List<TagToValueEntity> getTagsSingle() {

        //Get all product single tags
        List<TagToValueEntity> res = getServiceFacade().getPropertyManager().
                getTagsForProductByMultiple(selectedObject, false);
        List<TagToValueEntity> resDup = new LinkedList<TagToValueEntity>();
        Map<PropertyValueEntity, StringBuffer> tagLinksForGroupCats =
                new HashMap<PropertyValueEntity, StringBuffer>();

        for (TagToValueEntity re : res) {
            PropertyEntity parentCatType = re.getPropertyValue().getProperty().getParent();
            if (parentCatType != null && parentCatType.getType().equals(PropertyEntity.Type.GROUP)) {
                String reVal = re.getValue();
                PropertyValueEntity stubValue = parentCatType.getValues().get(0);
                StringBuffer totalVal = tagLinksForGroupCats.get(stubValue);
                if (totalVal == null) {
                    totalVal = new StringBuffer(reVal);
                } else {
                    totalVal.append("-@-").append(reVal);
                }
                tagLinksForGroupCats.put(stubValue, totalVal);
            } else {
                resDup.add(re);
            }
        }

        if (!tagLinksForGroupCats.isEmpty()) {
            for (PropertyValueEntity aValue : tagLinksForGroupCats.keySet()) {
                String val = tagLinksForGroupCats.get(aValue).toString();
                TagToValueEntity newLink = new TagToValueEntity();
                newLink.setTempValue(val);
                newLink.setPropertyValue(aValue);
                resDup.add(newLink);
            }
        }

        //Get all properties for product and delete assigned ones
        List<PropertyEntity> props = getPropertiesSingle();
        for (TagToValueEntity tagToValueEntity : resDup) {
            if (props.contains(tagToValueEntity.getPropertyValue().getProperty())) {
                props.remove(tagToValueEntity.getPropertyValue().getProperty());
            }
        }
        //Add unassigned properties to result list
        for (PropertyEntity prop : props) {
            TagToValueEntity stabLink = new TagToValueEntity();
            PropertyValueEntity stabPropVal = new PropertyValueEntity();
            stabPropVal.setProperty(prop);
            stabLink.setPropertyValue(stabPropVal);
            resDup.add(stabLink);
        }
        //Sort properties by name
        Collections.sort(resDup, CategoryManager.TAG_ENTITY_COMPARATOR);

        return resDup;
    }

    public List<PropertyEntity> getProperties() {
        Set<PropertyEntity> set = new HashSet<PropertyEntity>();
        for (CategoryEntity category : selectedObject.getCategories()) {
            set.addAll(getServiceFacade().getCategoryManager().getAllReadyProperties(category));
        }
        List<PropertyEntity> result = new LinkedList<PropertyEntity>(set);
        Collections.sort(result, CategoryManager.PROPERTY_ENTITY_COMPARATOR);
        return result;
    }

    public List<PropertyEntity> getPropertiesMultiple() {
        Set<PropertyEntity> set = new HashSet<PropertyEntity>();
        for (CategoryEntity category : selectedObject.getCategories()) {
            set.addAll(getServiceFacade().getCategoryManager().getPropertiesByMultiple(category, true));
        }
        List<PropertyEntity> result = new LinkedList<PropertyEntity>(set);
        return result;
    }

    public List<PropertyEntity> getPropertiesSingle() {
        Set<PropertyEntity> set = new HashSet<PropertyEntity>();
        for (CategoryEntity category : selectedObject.getCategories()) {
            set.addAll(getServiceFacade().getCategoryManager().getPropertiesByMultiple(category, false));
        }
        List<PropertyEntity> result = new LinkedList<PropertyEntity>(set);
        Collections.sort(result, CategoryManager.PROPERTY_ENTITY_COMPARATOR);
        return result;
    }

    @OnEvent(value = "submit", component = "form")
    @SuppressWarnings("unchecked")
    public Object onSetTagsSubmit() {

        // manually set tag processing
        if (getRMLogger().isDebugEnabled()) {
            getRMLogger().debug("Entered manual setting tags for object", selectedObject);
        }

        List<String> params = getIOC().getRequest().getParameterNames();
        List<TagToValueEntity> valueLinks = new LinkedList<TagToValueEntity>();
        for (String param : params) {
            if (param.startsWith("2RM")) {

                String value = getIOC().getRequest().getParameter(param);

                if (getRMLogger().isDebugEnabled()) {
                    getRMLogger().debug("Tag value " + param + ": " + value, selectedObject);
                }

                if (StringUtils.isEmpty(value)) {
                    value = "-";
                }
                String[] parsedParam = param.split("-");
                if (parsedParam.length >= 3) {
                    String propType = parsedParam[1];
                    String propertyIdStr = parsedParam[2];
                    String gPropertyIdStr = null;
                    Long propertyId;
                    Long gPropertyId = null;

                    if (GVALUE_INPUT.equals(propType) && parsedParam.length >= 4) {
                        gPropertyIdStr = parsedParam[3];
                    }

                    try {
                        propertyId = parseLong(propertyIdStr);
                        if (gPropertyIdStr != null) {
                            gPropertyId = parseLong(gPropertyIdStr);
                        }
                    } catch (Exception ex) {
                        getRMLogger().error("Bad id " + param, selectedObject, null);
                        getBase().addFlashToSession(getBase().getText("error.generic"),
                                                    FlashMessage.Type.FAILURE);
                        return getAjaxBlock();
                    }

                    if (GVALUE_INPUT.equals(propType)) {
                        if (getRMLogger().isDebugEnabled()) {
                            getRMLogger().debug("Processing gvalue tag. Property id: " +
                                                propertyId + ", child Property id: " + gPropertyId +
                                                ", value: " +
                                                value, selectedObject);
                        }
                        setValueTag(gPropertyId, value, valueLinks);
                    } else if (VALUE_INPUT.equals(propType)) {
                        if (getRMLogger().isDebugEnabled()) {
                            getRMLogger().debug("Processing value tag. Property id: " +
                                                propertyId + ", value: " + value, selectedObject);
                        }
                        setValueTag(propertyId, value, valueLinks);
                    } else if (SELECT_INPUT.equals(propType)) {
                        if (getRMLogger().isDebugEnabled()) {
                            getRMLogger().debug("Processing select tag. Property id: " +
                                                propertyId + ", value: " + value, selectedObject);
                        }
                        setSelectTag(propertyId, value, valueLinks);
                    }
                }
            }
        }

        try {


            getServiceFacade().getPropertyManager().clearTagsForProduct(selectedObject,
                                                                        getAuth().getUser());
            getServiceFacade().getPropertyManager().setManuallyTagsToProduct(valueLinks,
                                                                             selectedObject,
                                                                             getAuth().getUser());
            getBase().addFlashToSession(getBase().getText("tags.set.done"),
                                        FlashMessage.Type.SUCCESS);

        } catch (AccessControlException acex) {
            getRMLogger().errorSecurityViolation("Access denied", selectedObject);
            getBase().addFlashToSession(getBase().getText("error.access.denied"), FlashMessage.Type.FAILURE);
        } catch (Exception ex) {
            getRMLogger().error("Error manually setting tags.", selectedObject, ex);
            getBase().addFlashToSession(getBase().getText("error.generic"), FlashMessage.Type.FAILURE);
        }

        return getAjaxBlock();

    }

    private void setValueTag(Long propId, String value, List<TagToValueEntity> list) {

        PropertyEntity catType = getServiceFacade().getPropertyManager().get(propId);
        if (catType == null) {
            getRMLogger().error("Unknown category type with id: " + propId, selectedObject, null);
            return;
        }
        PropertyValueEntity catValue = getServiceFacade().
                getPropertyValueManager().getValueByName(catType, Constants.CAT_VALUE_STUB);
        if (catValue == null) {
            getRMLogger().error("Category does not contain STUB value", catType, null);
            return;
        }
        TagToValueEntity tagLink = new TagToValueEntity();
        tagLink.setValue(value);
        tagLink.setType(TagToValueEntity.Type.VALUE);
        tagLink.setPropertyValue(catValue);

        list.add(tagLink);

    }

    private void setSelectTag(Long propId, String value, List<TagToValueEntity> list) {

        if (StringUtils.isBlank(value) || value.equals("-")) {
            return;
        }

        PropertyEntity property = getServiceFacade().getPropertyManager().get(propId);
        if (property == null) {
            getRMLogger().error("Unknown property with id: " + propId, selectedObject, null);
            return;
        }
        PropertyValueEntity propertyValue = getServiceFacade().
                getPropertyValueManager().get(Long.parseLong(value));
        if (propertyValue == null) {
            getRMLogger().error("Property does not contain value " + value, property, null);
            return;
        }
        TagToValueEntity tagLink = new TagToValueEntity();
        tagLink.setType(TagToValueEntity.Type.LIST);
        tagLink.setPropertyValue(propertyValue);

        list.add(tagLink);
    }

    public Object onActionFromCancelAjax() {
        return getAjaxBlock();
    }

    public List<PropertyEntity> getMandatoryFields() {
        List<PropertyEntity> mf =
                getServiceFacade().getPropertyManager().getAllMandatoryFields(selectedObject);
        List<PropertyEntity> sf =
                getServiceFacade().getPropertyManager().getSetMandatoryFields(selectedObject);
        mf.removeAll(sf);
        return mf;
    }

}