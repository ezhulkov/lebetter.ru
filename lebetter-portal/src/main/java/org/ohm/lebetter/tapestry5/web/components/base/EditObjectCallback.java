package org.ohm.lebetter.tapestry5.web.components.base;

import org.room13.mallcore.model.ObjectBaseEntity;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 25.06.2009
 * Time: 16:45:56
 * To change this template use File | Settings | File Templates.
 */
public interface EditObjectCallback<T extends ObjectBaseEntity> {

    public boolean onFormSubmit(T object) throws Exception;

    public boolean onPostFormSubmit(T object) throws Exception;

}
