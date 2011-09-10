package org.ohm.lebetter.spring.aop;

import org.ohm.lebetter.model.DictSyncAware;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.ohm.lebetter.spring.sync.SyncDictProcessor;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.beans.factory.annotation.Required;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 01.09.2009
 * Time: 12:05:05
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractManagerAdvice implements AfterReturningAdvice {

    private final RMLogger log = new RMLogger(this.getClass());

    private ServiceManager serviceManager;

    public RMLogger getLogger() {
        return log;
    }

    @Required
    public void setServiceManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target)
            throws Throwable {
        if (args != null && args.length >= 2) {
            Object ob1 = args[0];
            Object ob2 = args[args.length - 1];
            if (ob1 instanceof DictSyncAware &&
                ob2 instanceof UserEntity) {
                processDictSync((DictSyncAware) ob1, (UserEntity) ob2);
            }
        }
    }

    protected void processProduct(ProductEntity product) {
        // normally do nothing
    }

    protected abstract void processDictSync(DictSyncAware object, UserEntity caller);

    protected SyncDictProcessor getSyncDictProcessor(ObjectBaseEntity object) {
        Map syncProcessors = getServiceManager().getDictSyncProcessors();
        return (SyncDictProcessor) (!syncProcessors.containsKey(object.getEntityCode()) ?
                                    syncProcessors.get("default") :
                                    syncProcessors.get(object.getEntityCode()));
    }


}