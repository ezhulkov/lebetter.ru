package org.ohm.lebetter.tapestry5.web.pages.dealer;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBrowseBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
@Import(
        library = {"proxy:http://maps.googleapis.com/maps/api/js?sensor=false"}
)
public class Index extends AbstractBrowseBasePage {

    @Property
    private DealerEntity selectedDealer;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getDealerManager());
        selectedDealer = (DealerEntity) selectedObject.findSelectedObject();
    }

    public Long onPassivate() {
        return selectedDealer == null ? null : selectedDealer.getRootId();
    }

}