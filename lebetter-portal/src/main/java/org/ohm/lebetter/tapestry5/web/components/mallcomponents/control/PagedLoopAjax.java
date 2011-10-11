package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.ohm.lebetter.tapestry5.web.util.datasource.GenericListBasedDS;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.log.UIException;
import org.room13.mallcore.tapestry5.web.components.mallcomponents.control.PagedSource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Import(
        library = {"proxy:/scripts/jquery.history.js"}
)
public class PagedLoopAjax extends AbstractBaseComponent {
    private static final RMLogger log = new RMLogger(PagedLoopAjax.class);

    @Parameter(required = true, allowNull = false, name = "source")
    private Iterable<?> sourceParam;

    private PagedSource<?> source;

    @Parameter
    @Property
    private Object value;

    @Parameter(value = "12")
    private Integer rowsPerPage;

    @Parameter(value = "12")
    private Integer pagesInPager;

    private PageDto onePage;

    @Inject
    private Block contents;

    @Inject
    private Block empty;

    @Parameter(required = true, allowNull = false)
    @Property
    private Zone targetZone;

    @Parameter(required = false, allowNull = false, name = "page")
    private int pageParam;

    private int page;

    @Parameter(name = "zoneId", allowNull = false,
               required = false, defaultPrefix = BindingConstants.LITERAL,
               value = "pagedLoopAjaxZone")
    @Property
    private String zoneId = "pagedLoopAjaxZone";


    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String reloadCallback;

    @Parameter(required = false, allowNull = false, value = "", defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String id;

    @Property(write = false)
    private boolean navigationUsed = false;

    public String getReloadCallback() {
        if (reloadCallback == null) {
            return null;
        }
        return reloadCallback.endsWith(")") ?
               reloadCallback : reloadCallback + "()";
    }

    public void setReloadCallback(String reloadCallback) {
        this.reloadCallback = reloadCallback;
    }

    @BeginRender
    public void beginRender() {
        if (sourceParam instanceof List) {
            source = new GenericListBasedDS((List) sourceParam);
        } else if (sourceParam instanceof Collection) {
            source = new GenericListBasedDS(new LinkedList((Collection) sourceParam));
        } else if (sourceParam instanceof PagedSource) {
            source = (PagedSource<?>) sourceParam;
        } else {
            throw new UIException(sourceParam.getClass().getName() + " is unknown source type");
        }
        this.page = this.pageParam;
        if (rowsPerPage == null) {
            rowsPerPage = 10;
        }
        if (pagesInPager == null) {
            pagesInPager = 5;
        }

        HttpSession session = getIOC().getHttpServletRequest().getSession(false);
        if (session != null) {
            setPageKey(new AtomicLongIdGenerator().generate(session));
        }
        storeBetweenAjaxRequests(getSource());
    }

    public Object onActionFromSetPageAjax(int page, String pageKey) {
        navigationUsed = true;
        setPageKey(pageKey);
        source = (PagedSource<?>) getObjectDuringAjaxRequest();

        int pagesCount = getPagesCount();
        if (page >= pagesCount) {
            page = pagesCount - 1;
        }
        this.page = page;

        if (source == null && log.isDebugEnabled()) {
            log.debug("Loop source is null. Page key is " + pageKey + "\n Reloading source page");
            return getIOC().getResources().getPage().getClass();
        }
        return isEmpty() ? empty : contents;
    }


    public Object onLoadInitialAjax(int page, String pageKey) {
        Object result = onActionFromSetPageAjax(page, pageKey);
        navigationUsed = false;
        return result;
    }


    public String getLoadInitialLink() {
        return getIOC().getResources().createEventLink("loadInitialAjax",
                                                       new Object[]{"PARAM1", "PARAM2"}).toURI();
    }

    protected void logSessionAttributes() {
        HttpSession session = getIOC().getHttpServletRequest().getSession(false);
        if (session != null) {
            Enumeration attributeNames = session.getAttributeNames();
            StringBuffer sb = new StringBuffer();
            while (attributeNames.hasMoreElements()) {
                String attribName = attributeNames.nextElement().toString();
                sb = sb.append(attribName).append(";");
            }
            log.debug("Available attributes are: " + sb.toString());

            sb = new StringBuffer();
            Cookie[] cookies = getIOC().getHttpServletRequest().getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    sb = sb.append(cookie.getName()).
                            append("=").
                            append(cookie.getValue()).
                            append(";");
                }
            }
            log.debug("Cookies are: " + sb.toString());

            sb = new StringBuffer();
            Map parameterMap = getIOC().getHttpServletRequest().getParameterMap();
            for (Object paramName : parameterMap.keySet()) {
                sb = sb.append(paramName).
                        append("=").
                        append(parameterMap.get(paramName)).
                        append(";");
            }
            log.debug("Request params are: ");
        }
    }

    public Object getCtx() {
        Iterable<?> src = source;
        return new Object[]{
                onePage.idx,
                getPageKey()
        };
    }

    public int getRowCount() {
        return source == null ? 0 : source.getTotalRowCount();
    }

    public boolean isEmpty() {
        return source.getTotalRowCount() == 0;
    }

    public PagedSource<?> getSrc() {
        int rowCount = getRowCount();
        int pagesCount = rowCount / rowsPerPage +
                         (rowCount % rowsPerPage > 0 ? 1 : 0);
        int startIdx = page * rowsPerPage >= rowCount ?
                       pagesCount - 1 : page * rowsPerPage;
        int endIdx = (page + 1) * rowsPerPage > rowCount ?
                     rowCount - 1 : (page + 1) * rowsPerPage - 1;

        source.prepare(startIdx, endIdx);
        return source;

    }

    public List<PageDto> getPages() {
        int rowCount = getRowCount();
        int pagesCount = rowCount / rowsPerPage +
                         (rowCount % rowsPerPage > 0 ? 1 : 0);

        if (pagesCount < 2) {
            return Collections.emptyList();
        }

        List<PageDto> pages = new LinkedList<PageDto>();
        if (pagesInPager == null ||
            pagesInPager < 0 ||
            pagesCount <= pagesInPager + 4) {
            for (int i = 0; i < pagesCount; i++) {
                pages.add(new PageDto(i, true));
            }
        } else {
            int startPage = page - pagesInPager / 2;

            // goto begin
            addGoToBeginPageLink(pages);

            // prev
            addLeftSpacePageLink(pages);

            // pages
            int k = Math.max(0, -startPage);
            int l = (startPage > pagesCount - pagesInPager) ? startPage - pagesCount + pagesInPager : 0;

            for (int i = 0; i < pagesInPager; i++) {
                int idx = i + startPage + k - l;
                pages.add(new PageDto(idx, true));
            }

            // next
            addRightSpacePagedLink(pagesCount, pages);

            // goto end
            addGoToEndPageLink(pagesCount, pages);
        }
        return pages;
    }

    protected void addGoToBeginPageLink(List<PageDto> pages) {
        int startPage = page - pagesInPager / 2;
        if (startPage > 0) {
            pages.add(new PageDto(0, getIOC().getMessages().get("page.first"), true));
        }
    }

    protected void addGoToEndPageLink(int pagesCount, List<PageDto> pages) {
        if (page <= pagesCount - pagesInPager) {
            pages.add(new PageDto(pagesCount - 1, getIOC().getMessages().get("page.last"), true));
        }
    }

    protected void addRightSpacePagedLink(int pagesCount, List<PageDto> pages) {
        if (page < pagesCount - 2) {
            pages.add(new PageDto(page + 1, getIOC().getMessages().get("page.forward"), true));
        }
    }

    protected void addLeftSpacePageLink(List<PageDto> pages) {
        if (page > 1) {
            pages.add(new PageDto(page - 1, getIOC().getMessages().get("page.backward"), true));
        }
    }


    public int getPagesCount() {
        int rowCount = getRowCount();
        return rowCount / rowsPerPage +
               (rowCount % rowsPerPage > 0 ? 1 : 0);
    }

    public boolean isPageSelected() {
        return onePage.getIdx() == page;
    }

    public int getCurrentPage() {
        return page;
    }

    public int getPagePosition() {
        return page;
    }

    public Integer getPagesInPager() {
        return pagesInPager;
    }

    public void setPagesInPager(Integer pagesInPager) {
        this.pagesInPager = pagesInPager;
    }

    public PagedSource<?> getSource() {
        return source;
    }

    public Integer getRowsPerPage() {
        return rowsPerPage;
    }

    public int getPage() {
        return page;
    }

    public Block getContents() {
        return contents;
    }

    public Block getEmpty() {
        return empty;
    }

    public PageDto getOnePage() {
        return onePage;
    }

    public void setOnePage(PageDto onePage) {
        this.onePage = onePage;
    }

    public static class PageDto implements Serializable {
        protected int idx;
        protected String hint;
        protected boolean isLink;

        public PageDto(int idx, boolean link) {
            this.idx = idx;
            isLink = link;
            hint = String.valueOf(idx + 1);
        }

        public PageDto(int idx, String hint, boolean link) {
            this.idx = idx;
            this.hint = hint;
            isLink = link;
        }

        public int getIdx() {
            return idx;
        }

        public String getHint() {
            return hint;
        }

        public boolean isLink() {
            return isLink;
        }
    }
}