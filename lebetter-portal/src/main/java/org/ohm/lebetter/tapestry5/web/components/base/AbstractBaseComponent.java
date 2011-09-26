package org.ohm.lebetter.tapestry5.web.components.base;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.ohm.lebetter.tapestry5.web.base.WebAppAuthFacade;
import org.ohm.lebetter.tapestry5.web.base.WebAppBaseFacade;
import org.ohm.lebetter.tapestry5.web.base.WebAppFacade;
import org.ohm.lebetter.tapestry5.web.base.WebAppIOCFacade;
import org.ohm.lebetter.tapestry5.web.mixins.BaseComponent;
import org.ohm.lebetter.tapestry5.web.services.ServiceFacade;
import org.room13.mallcore.log.RMLogger;

import javax.servlet.http.HttpSession;
import java.text.Format;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 13:55:23
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractBaseComponent implements WebAppFacade {

    private static final RMLogger log = new RMLogger(AbstractBaseComponent.class);

    @Mixin
    private BaseComponent baseComponent;

    @Parameter(name = "ajaxBlock", allowNull = true, required = false)
    private Block ajaxBlock = null;

    @Parameter(name = "targetZone", allowNull = false,
            required = false, value = "editArea",
            defaultPrefix = BindingConstants.LITERAL)
    private String targetZone;

    private String pageKey = "default_page_store";

    public String getTargetZone() {
        return targetZone;
    }

    public void setTargetZone(String targetZone) {
        this.targetZone = targetZone;
    }

    public Block getAjaxBlock() {
        return ajaxBlock;
    }

    public void setAjaxBlock(Block ajaxBlock) {
        this.ajaxBlock = ajaxBlock;
    }

    @Override
    public WebAppAuthFacade getAuth() {
        return baseComponent;
    }

    @Override
    public WebAppBaseFacade getBase() {
        return baseComponent;
    }

    @Override
    public WebAppIOCFacade getIOC() {
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

    public Format getDateFormat() {
        return getBase().getThreadSafeDateFormat();
    }

    /**
     * Stores data in the session with unique key and returns this key
     *
     * @param o object to save
     * @return key
     */
    protected String storeBetweenRequests(Object o) {
        HttpSession session = getIOC().getHttpServletRequest().getSession(false);
        if (session == null) {
            return null;
        }
        String key = new AtomicLongIdGenerator().generate(session);
        session.setAttribute(key, o);
        return key;
    }

    protected Object getAndRemove(String key) {
        HttpSession session = getIOC().getHttpServletRequest().getSession(false);
        if (session == null || key == null) {
            return null;
        }
        Object result = session.getAttribute(key);
        session.setAttribute(key, null);
        return result;
    }


    protected void storeBetweenAjaxRequests(Object o) {
        HttpSession session = getIOC().getHttpServletRequest().getSession(false);
        if (session == null) {
            return;
        }
        session.setAttribute(getPageKey(), o);
    }

    protected Object getObjectDuringAjaxRequest() {
        HttpSession session = getIOC().getHttpServletRequest().getSession(false);
        if (session == null) {
            if (log.isDebugEnabled()) {
                log.debug("Session is null. So, we can not get object from session");
            }
            return null;
        }
        return session.getAttribute(getPageKey());
    }

    protected void removeObject() {
        HttpSession session = getIOC().getHttpServletRequest().getSession(false);
        if (session != null) {
            session.removeAttribute(getPageKey());
        }
    }

    public String getPageKey() {
        return pageKey;
    }

    public void setPageKey(String pageKey) {
        this.pageKey = pageKey;
    }

    public static interface IdGenerator {
        public String generate(HttpSession session);
    }

    public static class AtomicLongIdGenerator implements IdGenerator {
        private static final RMLogger log = new RMLogger(AtomicLongIdGenerator.class);

        public static final String ID_GEN_KEY = "id_generator";
        public static final String STORE_PREFFIX = "atomic_long_id_gen_";

        @Override
        public String generate(HttpSession session) {
            try {
                AtomicLong gen = (AtomicLong) session.getAttribute(ID_GEN_KEY);
                if (gen == null) {
                    gen = synchronizedCreateCounter(session, gen);
                }
                return new StringBuilder().append(STORE_PREFFIX).append(gen.getAndIncrement()).toString();
            } catch (Exception e) {
                log.error("", e);
                return null;
            }
        }

        private AtomicLong synchronizedCreateCounter(HttpSession session, AtomicLong gen)
                throws InterruptedException {
            synchronized (this) {
                gen = (AtomicLong) session.getAttribute(ID_GEN_KEY);
                if (gen == null) {
                    gen = new AtomicLong(1);
                    session.setAttribute(ID_GEN_KEY, gen);
                }
            }
            return gen;
        }
    }

    public String getStaticLink() {
        return "http://static.lebetter.ru/" + getAuth().getUser().getRootId() + "/";
    }


    public String getLt() {
        return "<";
    }

    public String getGt() {
        return "<";
    }

}