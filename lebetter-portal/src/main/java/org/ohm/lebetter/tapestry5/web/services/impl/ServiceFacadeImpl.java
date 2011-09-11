package org.ohm.lebetter.tapestry5.web.services.impl;

import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.Context;
import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.spring.service.MailManager;
import org.ohm.lebetter.spring.service.ProductManager;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.spring.service.PropertyValueManager;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.ohm.lebetter.spring.service.UserKinderManager;
import org.ohm.lebetter.tapestry5.web.services.ServiceFacade;
import org.room13.mallcore.news.service.BlogManager;
import org.room13.mallcore.spring.service.ActionGroupManager;
import org.room13.mallcore.spring.service.ActionManager;
import org.room13.mallcore.spring.service.DataManager;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.LanguageManager;
import org.room13.mallcore.spring.service.ProfileParameterManager;
import org.room13.mallcore.spring.service.RoleManager;
import org.room13.mallcore.spring.service.SystemParameterManager;
import org.room13.mallcore.spring.service.UploadTicketService;
import org.slf4j.Logger;
import org.springframework.security.providers.encoding.PasswordEncoder;

import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 11.03.2009
 * Time: 17:53:52
 * To change this template use File | Settings | File Templates.
 */
public class ServiceFacadeImpl implements ServiceFacade {

    private ServiceManager serviceManager;
    private Context context;
    private ThreadLocale threadLocale;

    public ServiceFacadeImpl(final Logger log,
                             final Context context,
                             final ThreadLocale threadLocale,
                             final ServiceManager serviceManager) {
        this.context = context;
        this.threadLocale = threadLocale;
        this.serviceManager = serviceManager;
    }

    @Override
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public MailManager getMailManager() {
        return getServiceManager().getMailManager();
    }

    @Override
    public RoleManager getRoleManager() {
        return getServiceManager().getRoleManager();
    }

    @Override
    public UserKinderManager getUserManager() {
        return getServiceManager().getUserManager();
    }

    @Override
    public ActionManager getActionManager() {
        return getServiceManager().getActionManager();
    }

    @Override
    public DataManager getDataManager() {
        return getServiceManager().getDataManager();
    }

    @Override
    public Map<String, GenericManager> getGenericManagerMap() {
        return getServiceManager().getGenericManagerMap();
    }

    @Override
    public LanguageManager getLanguageManager() {
        return getServiceManager().getLanguageManager();
    }

    @Override
    public SystemParameterManager getSystemParameterManager() {
        return getServiceManager().getSystemParameterManager();
    }

    @Override
    public UploadTicketService getUploadTicketService() {
        return getServiceManager().getUploadTicketService();
    }

    @Override
    public BlogManager getBlogService() {
        return serviceManager.getBlogService();
    }

    @Override
    public ActionGroupManager getActionGroupManager() {
        return getServiceManager().getActionGroupManager();
    }

    @Override
    public ProfileParameterManager getProfileParameterManager() {
        return getServiceManager().getProfileManager();
    }

    @Override
    public Properties getProperties() {
        return getServiceManager().getProperties();
    }

    @Override
    public ProductManager getProductManager() {
        return getServiceManager().getProductManager();
    }

    @Override
    public CategoryManager getCategoryManager() {
        return serviceManager.getCategoryManager();
    }

    @Override
    public PropertyManager getPropertyManager() {
        return serviceManager.getPropertyManager();
    }

    @Override
    public PropertyValueManager getPropertyValueManager() {
        return serviceManager.getPropertyValueManager();
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return serviceManager.getPasswordEncoder();
    }
}