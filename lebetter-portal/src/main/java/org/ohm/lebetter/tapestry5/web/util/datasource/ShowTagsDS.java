package org.ohm.lebetter.tapestry5.web.util.datasource;

import org.ohm.lebetter.tapestry5.web.services.ServiceFacade;
import org.room13.mallcore.model.ObjectBaseEntity;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 19.10.2010
 * Time: 14:50:19
 * To change this template use File | Settings | File Templates.
 */
public class ShowTagsDS<T extends ObjectBaseEntity>
        extends GenericAbstractEntityDS<T, String> {

    public ShowTagsDS(Map<Long, String> map, ServiceFacade serviceFacade) {
        super(map, serviceFacade);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected T getValue(String entityType, Long entityKey) {
        return (T) serviceFacade.getProductManager().get(entityKey);
    }
}
