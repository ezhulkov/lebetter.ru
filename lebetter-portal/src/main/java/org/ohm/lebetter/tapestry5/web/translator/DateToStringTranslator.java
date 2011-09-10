package org.ohm.lebetter.tapestry5.web.translator;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.services.FormSupport;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 17.01.2010
 * Time: 12:56:12
 * To change this template use File | Settings | File Templates.
 */
public class DateToStringTranslator
        implements Translator<Date> {

    public static final String DF = "yyyy-MM-dd";
    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat(DF);
        }
    };

    @Override
    public String getName() {
        return "date2string";
    }

    @Override
    public String toClient(Date value) {
        return DATE_FORMAT.get().format(value);
    }

    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    @Override
    public String getMessageKey() {
        return "";  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Date parseClient(Field field, String s, String s1) throws ValidationException {
        try {
            return DATE_FORMAT.get().parse(s);
        } catch (ParseException ex) {
            throw new ValidationException(ex.getMessage());
        }
    }

    @Override
    public void render(Field field, String s, MarkupWriter markupWriter, FormSupport formSupport) {
    }
}
