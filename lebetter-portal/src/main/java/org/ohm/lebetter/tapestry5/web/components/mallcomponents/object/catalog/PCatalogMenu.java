package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.catalog;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

import java.util.List;

public class PCatalogMenu extends AbstractBaseComponent {

    @Property
    private CatalogEntity oneCatalog;

    @Cached
    public List<CatalogEntity> getCatalogs() {
        return getServiceFacade().getCatalogManager().getAllReady();
    }

}