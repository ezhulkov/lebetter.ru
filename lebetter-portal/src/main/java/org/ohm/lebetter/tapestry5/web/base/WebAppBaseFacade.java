package org.ohm.lebetter.tapestry5.web.base;

import org.ohm.lebetter.tapestry5.web.data.FlashMessage;
import org.ohm.lebetter.tapestry5.web.data.FlashMessage.Type;

import java.text.DateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 14:43:53
 * To change this template use File | Settings | File Templates.
 */
public interface WebAppBaseFacade {
    public static final String DF = "dd/MM/yyyy";

    public String formatSystemError(String error);

    public String getText(String key, Object... args);

    public void redirectToReferer();

    public void addFlashToSession(String msg, Type type);

    public boolean getFlashMessage2();

    public FlashMessage getFlashMessage();

    public boolean containsFlashMessage(FlashMessage.Type type);

    public DateFormat getThreadSafeDateFormat();

    public boolean isBots();

}
