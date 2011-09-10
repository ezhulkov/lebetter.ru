package org.ohm.lebetter.spring.sync;

import org.ohm.lebetter.model.impl.entities.PropertyValueEntity;
import org.room13.mallcore.model.ObjectBaseEntity;


/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 09.12.2009
 * Time: 10:58:51
 * To change this template use File | Settings | File Templates.
 */
public interface SyncDictProcessor<T extends ObjectBaseEntity> {

    public void processDictValue(T object, PropertyValueEntity value);

}
