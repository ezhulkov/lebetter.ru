package org.ohm.lebetter.tapestry5.web.pages.pcatalog;

import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBrowseBasePage;
import org.room13.mallcore.spring.service.DataManager.FileNames;

public class List extends AbstractBrowseBasePage {

    @Property
    private CatalogEntity oneCatalog;

    @Property
    private String oneCity;

    public java.util.List<CatalogEntity> getCatalogs() {
        return getServiceFacade().getCatalogManager().getAllReady();
    }

    public String getImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(oneCatalog,
                                                                  FileNames.MEDIUM_AVATAR_FILE);
    }

}