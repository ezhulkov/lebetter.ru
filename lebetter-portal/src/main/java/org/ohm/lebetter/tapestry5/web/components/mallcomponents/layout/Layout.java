package org.ohm.lebetter.tapestry5.web.components.mallcomponents.layout;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.runtime.Component;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;
import org.room13.mallcore.util.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 18.03.2009
 * Time: 11:37:54
 * To change this template use File | Settings | File Templates.
 */

public class Layout extends AbstractBaseComponent {

    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    @Parameter(name = "flash", required = false, allowNull = true)
    private FlashMessage flashMessage;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String bodyId;

    @Cached
    public String getKeywords() {
        Component page = getIOC().getResources().getPage();
        return page instanceof AbstractBasePage ?
                ((AbstractBasePage) page).getKeywords() :
                getIOC().getMessages().get("default.keywords");
    }

    @Cached
    public String getDescription() {
        Component page = getIOC().getResources().getPage();
        return page instanceof AbstractBasePage ?
                ((AbstractBasePage) page).getDescription() :
                getIOC().getMessages().get("default.keywords");
    }

    public String getSocialDescription() {
        Component page = getIOC().getResources().getPage();
        return page instanceof AbstractBasePage ?
                ((AbstractBasePage) page).getSocialDescription() : null;
    }

    public String getSocialImage() {
        Component page = getIOC().getResources().getPage();
        return page instanceof AbstractBasePage ?
                ((AbstractBasePage) page).getSocialImage() : null;
    }

    public String getTitle() {
        Component page = getIOC().getResources().getPage();
        if (page instanceof AbstractBasePage) {
            AbstractBasePage abPage = (AbstractBasePage) page;
            if (!StringUtil.isBlank(abPage.getTitle())) {
                return abPage.getTitle();
            }
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getUseAnalytics() {
        return Boolean.parseBoolean(getServiceFacade().getProperties().getProperty("analytics.enabled",
                "false"));
    }

}

