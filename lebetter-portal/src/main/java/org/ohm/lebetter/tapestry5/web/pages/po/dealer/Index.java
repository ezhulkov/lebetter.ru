package org.ohm.lebetter.tapestry5.web.pages.po.dealer;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

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
public class Index extends AdminBasePage {

    @Property
    @Inject
    private Block descBlock;

    @Property
    @Inject
    private Block picsBlock;

    @Property
    @Inject
    private Block mapBlock;

    @Property
    @Inject
    private Block adminBlock;

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

    public Block onActionFromPicsTab() {
        return picsBlock;
    }

    public Block onActionFromDescTab() {
        return descBlock;
    }

    public Block onActionFromMapTab() {
        return mapBlock;
    }

    public Block onActionFromAdminTab() {
        return adminBlock;
    }

    @Cached
    public DealerEntity getSelectedDealerRenew() {
        return getServiceFacade().getDealerManager().get(selectedDealer.getRootId());
    }

    public void setSelectedDealerRenew(DealerEntity dealer) {
        selectedDealer = dealer;
    }

}