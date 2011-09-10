package org.ohm.lebetter.spring.sync.impl;

import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.spring.sync.SyncDictProcessor;
import org.room13.mallcore.model.ObjectBaseEntity;


/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 09.12.2009
 * Time: 11:00:13
 * To change this template use File | Settings | File Templates.
 */
public class GenericSyncDictProcessor implements SyncDictProcessor<ObjectBaseEntity> {

    @Override
    public void processDictValue(ObjectBaseEntity object, PropertyValueEntity value) {

        value.getRelation().setObjectType(object.getEntityCode());
        value.getRelation().setObjectId(object.getId());
        value.setObjectStatus(object.getObjectStatus());
        value.setName(object.getName());

    }
}
