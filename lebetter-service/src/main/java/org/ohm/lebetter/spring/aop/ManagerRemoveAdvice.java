package org.ohm.lebetter.spring.aop;


import org.ohm.lebetter.model.DictSyncAware;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.sync.SyncDictProcessor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 01.09.2009
 * Time: 12:05:05
 * To change this template use File | Settings | File Templates.
 */
public class ManagerRemoveAdvice extends AbstractManagerAdvice {

    @Override
    protected void processDictSync(DictSyncAware object, UserEntity caller) {

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Processing sync operation after REMOVE methos", object);
        }

        SyncDictProcessor processor = getSyncDictProcessor(object);
        boolean rootObject = object.isRootObject();

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("SyncDictProcessor: " + processor + ", rootObject: " + rootObject, object);
        }

        List<PropertyEntity> categories = getServiceManager().getDictPropertyHolder().
                getDictProperties(object.getEntityCode().toLowerCase());
        if (categories == null) {
            return;
        }
        //For every registered dict categories
        for (PropertyEntity category : categories) {
            PropertyValueEntity catValue = getServiceManager().
                    getPropertyValueManager().
                    getValueByName(category, object.getName());
            //If we have such cat value - delete it
            if (catValue != null) {
                //Create cat value
                getServiceManager().getPropertyValueManager().
                        removeValueForInsiders(catValue, category, caller);
            }
        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Processing sync done", object);
        }

    }

}