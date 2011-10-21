package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 21.10.11
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public enum PagerPosition {

    /**
     * Position the pager above the paged content.
     */
    TOP(true, false),

    /**
     * Position the pager below the paged content (this is the default).
     */
    BOTTOM(false, true),

    /**
     * Show the pager above and below the paged content.
     */
    BOTH(true, true),

    /**
     * Don't show a pager (the application will need to supply its own
     * navigation mechanism).
     */
    NONE(false, false);

    private final boolean _matchTop;

    private final boolean _matchBottom;

    private PagerPosition(boolean matchTop, boolean matchBottom) {
        _matchTop = matchTop;
        _matchBottom = matchBottom;
    }

    public boolean isMatchBottom() {
        return _matchBottom;
    }

    public boolean isMatchTop() {
        return _matchTop;
    }

}