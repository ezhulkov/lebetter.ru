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
@SuppressWarnings("unchecked")
public class ManagerCreateAdvice extends AbstractManagerAdvice {

    @Override
    protected void processDictSync(DictSyncAware object, UserEntity caller) {

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Processing sync operation after CREATE method", object);
        }

        SyncDictProcessor processor = getSyncDictProcessor(object);

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("SyncDictProcessor: " + processor, object);
        }

        List<PropertyEntity> properties = getServiceManager().getDictPropertyHolder().
                getDictProperties(object.getEntityCode().toLowerCase());
        //For every registered dict categories
        if (properties == null) {
            return;
        }
        for (PropertyEntity property : properties) {
            List<PropertyValueEntity> catValues = getServiceManager().
                    getPropertyValueManager().getValuesByObject(object, property);
            PropertyValueEntity propertyValue = null;
            if (catValues != null && catValues.size() > 0) {
                propertyValue = catValues.get(0);
            }
            //If we already have such cat value - delete it
            if (propertyValue != null) {
                getServiceManager().getPropertyValueManager().
                        removeValueForInsiders(propertyValue, property, caller);
            }
            //Create cat value and save it
            propertyValue = new PropertyValueEntity();
            //Copy all necessary parameters
            processor.processDictValue(object, propertyValue);
            //Create cat value
            getServiceManager().getPropertyValueManager().
                    createValueForInsiders(propertyValue, property, caller);
        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Processing sync done", object);
        }

    }

}