package org.ohm.lebetter.tapestry5.web.pages.po.system.property;

import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Create extends AdminBasePage {

    @Property
    private PropertyEntity parentCategory;

    public void onActivate(Long pid) {
        parentCategory = getServiceFacade().getPropertyManager().get(pid);
    }

    public Long onPassivate() {
        return parentCategory == null ? null : parentCategory.getRootId();
    }

}
