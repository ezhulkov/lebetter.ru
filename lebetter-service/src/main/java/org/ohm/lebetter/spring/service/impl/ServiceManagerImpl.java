package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.spring.service.DictPropertyHolder;
import org.ohm.lebetter.spring.service.MailManager;
import org.ohm.lebetter.spring.service.ProductManager;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.spring.service.PropertyValueManager;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.ohm.lebetter.spring.service.UserKinderManager;
import org.ohm.lebetter.spring.sync.SyncDictProcessor;
import org.room13.mallcore.spring.service.impl.I18nBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.providers.encoding.PasswordEncoder;

import java.util.Map;

public class ServiceManagerImpl
        extends org.room13.mallcore.spring.service.impl.ServiceManagerImpl
        implements ServiceManager {

    private CategoryManager categoryManager;
    private PropertyManager propertyManager;
    private PropertyValueManager propertyValueManager;
    private ProductManager productManager;
    private DictPropertyHolder dictPropertyHolder;
    private Map<String, SyncDictProcessor> syncDictProcessors;
    private I18nBean i18n;
    private PasswordEncoder passwordEncoder;

    @Override
    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    @Required
    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    @Override
    public PropertyManager getPropertyManager() {
        return propertyManager;
    }

    @Required
    public void setPropertyManager(PropertyManager propertyManager) {
        this.propertyManager = propertyManager;
    }

    @Override
    public ProductManager getProductManager() {
        return productManager;
    }

    @Override
    public PropertyValueManager getPropertyValueManager() {
        return propertyValueManager;
    }

    @Required
    public void setPropertyValueManager(PropertyValueManager propertyValueManager) {
        this.propertyValueManager = propertyValueManager;
    }

    @Override
    public DictPropertyHolder getDictPropertyHolder() {
        return dictPropertyHolder;
    }

    @Required
    public void setDictPropertyHolder(DictPropertyHolder dictPropertyHolder) {
        this.dictPropertyHolder = dictPropertyHolder;
    }

    @Override
    public Map<String, SyncDictProcessor> getDictSyncProcessors() {
        return syncDictProcessors;
    }

    @Required
    public void setSyncDictProcessors(Map<String, SyncDictProcessor> syncDictProcessors) {
        this.syncDictProcessors = syncDictProcessors;
    }

    @Override
    public MailManager getMailManager() {
        return getMailEngine() instanceof MailManager ?
               (MailManager) getMailEngine() : null;
    }

    @Override
    public UserKinderManager getUserManager() {
        return (UserKinderManager) super.getUserManager();
    }

    @Override
    public I18nBean getI18n() {
        return i18n;
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Required
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Required
    public void setI18n(I18nBean i18n) {
        this.i18n = i18n;
    }

    @SuppressWarnings("PMD")
    public void setUserManager(UserKinderManager userManager) {
        super.setUserManager(userManager);
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }
}