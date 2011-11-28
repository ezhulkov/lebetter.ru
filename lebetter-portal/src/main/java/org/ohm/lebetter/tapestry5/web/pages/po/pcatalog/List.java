package org.ohm.lebetter.tapestry5.web.pages.po.pcatalog;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.spring.service.Constants.FileNames;
import org.ohm.lebetter.tapestry5.web.pages.base.AdminBasePage;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class List extends AdminBasePage {

    @Property
    private CatalogEntity oneCatalog;

    @Cached
    public java.util.List<CatalogEntity> getCatalogs() {
        return getServiceFacade().getCatalogManager().getAll("name");
    }

    public String getImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(oneCatalog, FileNames.SMALL_PHOTO.toString());
    }

    public String getOneCatalogObjectStatus() {
        return getBase().getText(oneCatalog.getObjectStatus().toString());
    }

}
