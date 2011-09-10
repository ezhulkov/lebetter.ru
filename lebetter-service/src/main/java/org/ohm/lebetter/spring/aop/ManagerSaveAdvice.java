package org.ohm.lebetter.spring.aop;

import org.ohm.lebetter.model.DictSyncAware;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.service.DictPropertyHolder;
import org.ohm.lebetter.spring.sync.SyncDictProcessor;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 01.09.2009
 * Time: 12:05:05
 * To change this template use File | Settings | File Templates.
 */
public class ManagerSaveAdvice extends AbstractManagerAdvice {

    @Override
    protected void processDictSync(DictSyncAware object, UserEntity caller) {

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Processing sync operation after SAVE method", object);
        }

        SyncDictProcessor processor = getSyncDictProcessor(object);
        boolean rootObject = object.isRootObject();

        if (getLogger().isTraceEnabled()) {
            getLogger().trace("SyncDictProcessor: " + processor + ", rootObject: " + rootObject, object);
        }

        String entityCode = object.getEntityCode().toLowerCase();
        DictPropertyHolder dictPropertyHolder = getServiceManager().getDictPropertyHolder();
        List<PropertyEntity> categories = dictPropertyHolder.getDictProperties(entityCode);
        if (categories == null) {
            return;
        }
        //For every registered dict categories
        for (PropertyEntity category : categories) {
            List<PropertyValueEntity> catValues = getServiceManager().
                    getPropertyValueManager().getValuesByObject(object, category);
            //Try to find root value by object id and type
            PropertyValueEntity catValue = null;
            if (catValues != null && catValues.size() > 0) {
                catValue = catValues.get(0);
            }
            if (catValue == null) {
                //Create new cat value
                catValue = new PropertyValueEntity();
                //Copy all necessary parameters
                processor.processDictValue(object, catValue);
                //Create cat value
                getServiceManager().getPropertyValueManager().
                        createValueForInsiders(catValue, category, caller);
            }
            if (getLogger().isTraceEnabled()) {
                getLogger().trace("Root category value: " + catValue, object);
            }
            if (!rootObject) {
                //Try to find translated value for root value
                PropertyValueEntity catValueL = getServiceManager().getPropertyValueManager().
                        getStrictL(catValue.getRootId(), object.getLanguage().getId());
                if (catValueL == null) {
                    //Translated value does not exist - create it first
                    catValueL = new PropertyValueEntity();
                    catValueL = getServiceManager().getPropertyValueManager().
                            createL(catValue, object.getLanguage().getCode(), caller);
                }
                catValue = catValueL;
                if (getLogger().isTraceEnabled()) {
                    getLogger().trace("Translated category value: " + catValue, object);
                }
            }

            //Copy necessery parameters from object to value
            processor.processDictValue(object, catValue);

            if (getLogger().isTraceEnabled()) {
                getLogger().trace("Saving category value: " + catValue, object);
            }

            //Save category value
            getServiceManager().getPropertyValueManager().saveValueForInsiders(catValue, category, caller);

        }

        if (getLogger().isDebugEnabled()) {
            getLogger().debug("Processing sync done", object);
        }

    }

}