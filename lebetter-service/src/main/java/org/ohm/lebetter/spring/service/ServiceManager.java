package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.spring.sync.SyncDictProcessor;
import org.room13.mallcore.spring.service.impl.I18nBean;
import org.springframework.security.providers.encoding.PasswordEncoder;

import java.util.Map;

public interface ServiceManager
        extends org.room13.mallcore.spring.service.ServiceManager {

    public CategoryManager getCategoryManager();

    public OrderManager getOrderManager();

    public PropertyManager getPropertyManager();

    public PropertyValueManager getPropertyValueManager();

    public ProductManager getProductManager();

    public DictPropertyHolder getDictPropertyHolder();

    public Map<String, SyncDictProcessor> getDictSyncProcessors();

    public MailManager getMailManager();

    public DealerManager getDealerManager();

    @Override
    public UserLBManager getUserManager();

    public I18nBean getI18n();

    public PasswordEncoder getPasswordEncoder();

    public ProductPhotoManager getProductPhotoManager();

}