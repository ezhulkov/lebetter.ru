package org.ohm.lebetter.tapestry5.web.pages.pcatalog;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CatalogEntity;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
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

    @Cached
    public CategoryEntity getSelectedCategoryParent() {
        java.util.List<CategoryEntity> rootCats = getServiceFacade().getCategoryManager().getAllReadyCategories(null);
        if (rootCats.size() != 0) {
            return rootCats.get(0);
        }
        return null;
    }

}