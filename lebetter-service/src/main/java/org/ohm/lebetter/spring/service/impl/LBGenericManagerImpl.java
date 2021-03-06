package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.room13.mallcore.model.ObjectBaseEntity;
import org.room13.mallcore.spring.service.impl.PortalGenericManagerImpl;

public class LBGenericManagerImpl<T extends ObjectBaseEntity, TU extends UserEntity>
        extends PortalGenericManagerImpl<T, TU> {

    public LBGenericManagerImpl() {
        super();
    }

    protected LBGenericManagerImpl(Class<T> persistentClass) {
        super(persistentClass);
    }

    @Override
    public ServiceManager getServiceManager() {
        return (ServiceManager) super.getServiceManager();
    }

}
