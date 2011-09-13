package org.ohm.lebetter.tapestry5.web.pages.base;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.services.Session;
import org.ohm.lebetter.tapestry5.web.base.WebAppAuthFacade;
import org.ohm.lebetter.tapestry5.web.base.WebAppBaseFacade;
import org.ohm.lebetter.tapestry5.web.base.WebAppFacade;
import org.ohm.lebetter.tapestry5.web.base.WebAppIOCFacade;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.layout.Layout;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.ohm.lebetter.tapestry5.web.mixins.BaseComponent;
import org.ohm.lebetter.tapestry5.web.services.ServiceFacade;
import org.room13.mallcore.Constants;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.util.WebUtil;
import org.springframework.security.ui.rememberme.TokenBasedRememberMeServices;

import java.text.Format;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 14:17:53
 * To change this template use File | Settings | File Templates.
 */
@Import(
        stack = {"jquery"},
        stylesheet = {"proxy:/styles/style.css"},
        library = {"proxy:/scripts/jquery-ui-1.8.9.custom.min.js",
                   "proxy:/scripts/jquery.fancybox-1.3.3.js",
                   "proxy:/scripts/jquery.cookie.js",
                   "proxy:/scripts/jquery.metadata.js"}
)
public abstract class AbstractBasePage implements WebAppFacade {

    @Mixin
    private BaseComponent baseComponent;

    @Persist(PersistenceConstants.FLASH)
    private FlashMessage flashMessage;

    @InjectComponent
    private Layout layout;

    @Override
    public WebAppAuthFacade getAuth() {
        return baseComponent;
    }

    @Override
    public WebAppIOCFacade getIOC() {
        return baseComponent;
    }

    @Override
    public WebAppBaseFacade getBase() {
        return baseComponent;
    }

    @Override
    public RMLogger getRMLogger() {
        return baseComponent.getRMLogger();
    }

    @Override
    public ServiceFacade getServiceFacade() {
        return baseComponent.getServiceFacade();
    }

    public WebAppIOCFacade getBaseComponent() {
        return baseComponent;
    }

    public FlashMessage getFlashMessage() {
        FlashMessage res = null;

        Session session = getIOC().getRequest().getSession(false);
        if (session != null && !session.isInvalidated()) {
            if (session.getAttribute(Constants.FLASH) != null) {
                res = (FlashMessage) session.getAttribute(Constants.FLASH);
                session.setAttribute(Constants.FLASH, null);
            } else {
                res = flashMessage;
                flashMessage = null;
            }

            if (res != null && getRMLogger().isTraceEnabled()) {
                getRMLogger().trace("Flash message: " + res.getMessage());
            }
        }

        return res;
    }

    public void setFlashMessage(FlashMessage flashMessage) {
        this.flashMessage = flashMessage;
    }

    public void addFlashByKey(String key, FlashMessage.Type type, Object... args) {
        setFlashMessage(new FlashMessage(getBase().getText(key, args), type));
    }

    public String getKeywords() {
        return getIOC().getMessages().get("title.index.keywords");
    }

    public String getDescription() {
        return getIOC().getMessages().get("title.index.description");
    }

    public String getTitle() {
        return "";
    }

    public Format getDateFormat() {
        return getBase().getThreadSafeDateFormat();
    }

    public String getStaticLink() {
        return "http://static.kindershopping.ru/" + getAuth().getUser().getRootId() + "/";
    }

    protected void clearCookie() {
        WebUtil.deleteCookie(getIOC().getHttpServletResponse(),
                             TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
    }

    public String getSocialDescription() {
        return null;
    }

    public String getSocialImage() {
        return "http://kindershopping.ru/images/lb-front/logo.gif";
    }


    public String getLt() {
        return "<";
    }

    public String getGt() {
        return "<";
    }


}