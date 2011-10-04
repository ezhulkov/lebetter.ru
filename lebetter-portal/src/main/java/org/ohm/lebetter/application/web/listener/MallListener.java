package org.ohm.lebetter.application.web.listener;

import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.spring.service.DictPropertyHolder;
import org.ohm.lebetter.spring.service.PropertyManager;
import org.room13.mallcore.spring.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContextEvent;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.11.2010
 * Time: 17:38:53
 * To change this template use File | Settings | File Templates.
 */
public class MallListener
        extends org.room13.mallcore.application.web.listener.MallListener {

    private static ApplicationContext ctx = null;

    public static Object getBean(String name) {
        return ctx == null ? null : ctx.getBean(name);
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {

        ctx = ApplicationContextProvider.getApplicationContext();
        Object ctmBean = getBean("propertyManager");

        if (ctmBean != null && ctmBean instanceof PropertyManager) {

            PropertyManager propertyManager = (PropertyManager) ctmBean;

            DictPropertyHolder holder = (DictPropertyHolder) getBean("dictPropertyHolder");

            //Find all DICTIONARY categories
            List<PropertyEntity> props = propertyManager.getAllRootProperties();
            if (props != null) {
                for (PropertyEntity prop : props) {
                    if (prop.getType().equals(PropertyEntity.Type.DICTIONARY)) {
                        holder.addNewDictCategory(prop);
                    }
                }
            }
        }

    }
}
