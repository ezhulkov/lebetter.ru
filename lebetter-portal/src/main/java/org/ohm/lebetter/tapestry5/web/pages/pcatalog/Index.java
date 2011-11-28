package org.ohm.lebetter.tapestry5.web.pages.pcatalog;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.SelectedObject;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBrowseBasePage;
import org.room13.mallcore.spring.service.DataManager;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Index extends AbstractBrowseBasePage {

    @Property
    private CatalogEntity selectedCatalog;

    @InjectComponent
    private SelectedObject selectedObject;

    public void onActivate(String idStr) throws Exception {
        selectedObject.setIdStr(idStr);
        selectedObject.setObjectManager(getServiceFacade().getCatalogManager());
        selectedCatalog = (CatalogEntity) selectedObject.findSelectedObject();
    }

    public Long onPassivate() {
        return selectedCatalog == null ? null : selectedCatalog.getRootId();
    }

    public String getImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(selectedCatalog,
                                                                  DataManager.FileNames.BIG_AVATAR_FILE);
    }

}