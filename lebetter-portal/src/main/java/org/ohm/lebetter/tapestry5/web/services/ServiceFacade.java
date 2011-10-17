package org.ohm.lebetter.tapestry5.web.services;

import org.ohm.lebetter.spring.service.CategoryManager;
import org.ohm.lebetter.spring.service.DealerManager;
import org.ohm.lebetter.spring.service.MailManager;
import org.ohm.lebetter.spring.service.OrderManager;
import org.ohm.lebetter.spring.service.ProductManager;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.ohm.lebetter.spring.service.PropertyValueManager;
import org.ohm.lebetter.spring.service.UserLBManager;
import org.room13.mallcore.spring.service.ActionGroupManager;
import org.room13.mallcore.spring.service.ActionManager;
import org.room13.mallcore.spring.service.DataManager;
import org.room13.mallcore.spring.service.GenericManager;
import org.room13.mallcore.spring.service.LanguageManager;
import org.room13.mallcore.spring.service.ProfileParameterManager;
import org.room13.mallcore.spring.service.RoleManager;
import org.room13.mallcore.spring.service.ServiceManager;
import org.room13.mallcore.spring.service.SystemParameterManager;
import org.room13.mallcore.spring.service.UploadTicketService;
import org.springframework.security.providers.encoding.PasswordEncoder;

import java.util.Map;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.03.2009
 * Time: 17:05:53
 * To change this template use File | Settings | File Templates.
 */
public interface ServiceFacade {

    public ServiceManager getServiceManager();

    public UserLBManager getUserManager();

    public RoleManager getRoleManager();

    public OrderManager getOrderManager();

    public ActionManager getActionManager();

    public ActionGroupManager getActionGroupManager();

    public MailManager getMailManager();

    public DataManager getDataManager();

    public Map<String, GenericManager> getGenericManagerMap();

    public LanguageManager getLanguageManager();

    public SystemParameterManager getSystemParameterManager();

    public UploadTicketService getUploadTicketService();

    public ProfileParameterManager getProfileParameterManager();

    public Properties getProperties();

    public ProductManager getProductManager();

    public CategoryManager getCategoryManager();

    public PropertyManager getPropertyManager();

    public PropertyValueManager getPropertyValueManager();

    public PasswordEncoder getPasswordEncoder();

    public DealerManager getDealerManager();

}
