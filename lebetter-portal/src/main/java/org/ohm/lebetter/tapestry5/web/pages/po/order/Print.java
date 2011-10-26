package org.ohm.lebetter.tapestry5.web.pages.po.order;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;
import org.room13.mallcore.spring.service.DataManager;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Print extends AbstractBasePage {

    @Property
    private OrderEntity selectedOrder;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getOrderManager());
        selectedOrder = (OrderEntity) selectedObject.findSelectedObject();
    }

    public Long onPassivate() {
        return selectedOrder == null ? null : selectedOrder.getRootId();
    }

    public String getImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(selectedOrder.getDealer(),
                                                                  DataManager.FileNames.BIG_AVATAR_FILE);
    }

}