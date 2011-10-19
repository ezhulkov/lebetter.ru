package org.ohm.lebetter.tapestry5.web.pages;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class Index extends AbstractBasePage {

    @Property
    private CategoryEntity oneCategory;

    public void onActivate(Object... args) throws Exception {
        if (args.length != 0) {
            getIOC().getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,
                                             "Page not found " + getIOC().getRequest().getPath());
            getRMLogger().error("Page not found " + getIOC().getRequest().getPath());
        }
    }

    @Cached
    public List<CategoryEntity> getMainCategories() {
        return getServiceFacade().getCategoryManager().getCategoriesToMainPage();
    }

}