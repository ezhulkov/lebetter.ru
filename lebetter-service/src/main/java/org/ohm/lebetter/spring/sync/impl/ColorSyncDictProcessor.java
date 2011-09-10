package org.ohm.lebetter.spring.sync.impl;

import org.ohm.lebetter.model.impl.entities.ColorEntity;
import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.ohm.lebetter.spring.sync.SyncDictProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 09.12.2009
 * Time: 11:00:13
 * To change this template use File | Settings | File Templates.
 */
public class ColorSyncDictProcessor implements SyncDictProcessor<ColorEntity> {

    @Override
    public void processDictValue(ColorEntity object, PropertyValueEntity value) {

        value.getRelation().setObjectType(object.getEntityCode());
        value.getRelation().setObjectId(object.getId());
        value.setName(object.getName());
        value.setObjectStatus(object.getObjectStatus());
        value.setAdditionalDictInfo(object.getColorCode());
        value.setAdditionalDictInfo2(object.getCode());

    }
}