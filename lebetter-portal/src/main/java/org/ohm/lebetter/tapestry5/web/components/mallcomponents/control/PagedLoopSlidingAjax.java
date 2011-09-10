package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.room13.mallcore.tapestry5.web.components.mallcomponents.control.PagedSource;

import java.util.LinkedList;
import java.util.List;

public class PagedLoopSlidingAjax extends PagedLoopAjax {
    public static final String NEXT = "next";
    public static final String BACK = "back";
    public static final String PAGE = "page";

    @Parameter(required = false, allowNull = false, value = "1")
    private int slidingFrameSize;

    private int pagesOnDisplay = 1;

    private String moveDirection = NEXT;

    private boolean stateRestore = false;

    private int pageFrom = 0;

    private int pageTo = -1;

    public Block onActionFromBackAjax() {
        // show block with "next" button
        return null;
    }

    public Block onActionFromNextAjax(int page, String pageKey, int offset) {
        onActionFromSetPageAjax(page, pageKey);
        moveDirection = NEXT;
        this.pagesOnDisplay = ++offset > slidingFrameSize ? slidingFrameSize : offset;
        // это текущая страница на след. шаге
        pageFrom = page - pagesOnDisplay + 1;
        pageTo = page;
        return getContents();
    }

    public Block onActionFromBackAjax(int page, String pageKey, int offset) {
        onActionFromSetPageAjax(page, pageKey);
        moveDirection = BACK;
        this.pagesOnDisplay = ++offset > slidingFrameSize ? slidingFrameSize : offset;
        pageFrom = page - pagesOnDisplay + 1;
        pageTo = page;
        return getContents();
    }

    public Object onActionFromSelectPageAjaxTop(int page, String pageKey, int offset) {
        return onActionFromSelectPageAjax(page, pageKey, offset);
    }

    public Object onActionFromSelectPageAjaxBottom(int page, String pageKey, int offset) {
        return onActionFromSelectPageAjax(page, pageKey, offset);
    }

    public Object onActionFromSelectPageAjax(int page, String pageKey, int offset) {
        moveDirection = PAGE;
        this.pagesOnDisplay = 1;
        pageFrom = page;
        pageTo = page;
        return super.onActionFromSetPageAjax(page, pageKey);
    }

    public boolean isBackPressed() {
        return BACK.equals(moveDirection) && !stateRestore;
    }

    public boolean isNextPressed() {
        return NEXT.equals(moveDirection) && !stateRestore;
    }

    public boolean isPagePressed() {
        return PAGE.equals(moveDirection) && !stateRestore;
    }

    public boolean isNextPressedAuto() {
        return NEXT.equals(moveDirection) && stateRestore;
    }

    public boolean isPagePressedAuto() {
        return PAGE.equals(moveDirection) && stateRestore;
    }


    public boolean isHasNext() {
        return getPage() < getPagesCount() - 1;
    }

    public boolean isHasPrev() {
        return getPage() >= pagesOnDisplay;
    }


    public Object getNextCtx() {
        return new Object[]{
                getPage() + 1,
                getPageKey(),
                pagesOnDisplay == 0 ? 1 : pagesOnDisplay
        };
    }


    public Object getBackCtx() {
        return new Object[]{
                pagesOnDisplay == slidingFrameSize ? getPage() - 1 : getPage(),
                getPageKey(),
                pagesOnDisplay == 0 ? 1 : pagesOnDisplay
        };
    }

    @Override
    public Object getCtx() {
        return new Object[]{
                getOnePage() == null ? 0 : getOnePage().idx,
                getPageKey(),
                pagesOnDisplay
        };
    }

    public int getSlidingFrameSize() {
        return slidingFrameSize;
    }

    public List<PageDto> getPrevPages() {
        LinkedList<PageDto> result = new LinkedList<PageDto>();
        for (int i = 0; i < getPage(); i++) {
            result.add(new PageDto(i, true));
        }
        return result;
    }


    public PagedSource<?> getSrc() {
        int rowCount = getRowCount();
        Integer rowsPerPage = getRowsPerPage();
        int page = getPagePosition();

        int pagesCount = rowCount / rowsPerPage +
                (rowCount % rowsPerPage > 0 ? 1 : 0);
        int startIdx = page * rowsPerPage >= rowCount ?
                pagesCount - 1 : page * rowsPerPage;
        int endIdx = (page + 1) * rowsPerPage > rowCount ?
                rowCount - 1 : (page + 1) * rowsPerPage - 1;

        getSource().prepare(startIdx, endIdx);
        return getSource();

    }

    public int getPagePosition() {
        int page = getPage();
        if (BACK.equals(moveDirection)) {
            int pagesOnDisplay = this.pagesOnDisplay < 0 ? slidingFrameSize : this.pagesOnDisplay;
            page = page - pagesOnDisplay + 1;
        }
        if (page < 0) {
            page = 0;
        }
        return page;
    }

    public boolean isHasPager() {
        return getPagesCount() > 1;
    }

    @Override
    protected void addGoToBeginPageLink(List<PageDto> pages) {
        int startPage = getPage() - getPagesInPager() / 2;
        if (startPage > 0) {
            pages.add(new PageDto(0, true));
        }
    }

    @Override
    protected void addGoToEndPageLink(int pagesCount, List<PageDto> pages) {
        int endPage = getEndPage();
        if (endPage < getPagesCount()) {
            pages.add(new PageDto(pagesCount - 1, true));
        }
    }

    protected int getEndPage() {
        Integer pagesInPager = getPagesInPager();
        return getPage() + pagesInPager / 2 +
                (pagesInPager % 2 == 0 ? 0 : 1);
    }

    @Override
    protected void addRightSpacePagedLink(int pagesCount, List<PageDto> pages) {
        int endPage = getEndPage();
        if (endPage < getPagesCount() - 1) {
            pages.add(new PageDto(0, "...", false));
        }
    }

    @Override
    protected void addLeftSpacePageLink(List<PageDto> pages) {
        int startPage = getPage() - getPagesInPager() / 2;
        if (startPage > 1) {
            pages.add(new PageDto(0, "...", true));
        }
    }

    public Object onLoadInitialAjaxAction(int pageFrom, int pageTo,
                                          String pageKey, int pagesOnDisplay, String direction) {
        this.pageFrom = pageFrom + 1;
        this.pageTo = pageTo;
        moveDirection = null;
        this.moveDirection = direction;
        this.pagesOnDisplay = pagesOnDisplay;
        stateRestore = true;
        return onActionFromSetPageAjax(pageFrom, pageKey);
    }

    public String getLoadLink() {
        return getIOC().getResources().createEventLink("loadInitialAjaxAction",
                new Object[]{"PARAM1", "PARAM2", "PARAM3", "PARAM4", "PARAM5"}).toURI();
    }

    public int getPagesOnDisplay() {
        return pagesOnDisplay;
    }

    public String getMoveDirection() {
        return moveDirection;
    }

    public int getPageFrom() {
        return pageFrom;
    }

    public int getPageTo() {
        return pageTo;
    }

    public boolean isStateRestore() {
        return stateRestore;
    }
}