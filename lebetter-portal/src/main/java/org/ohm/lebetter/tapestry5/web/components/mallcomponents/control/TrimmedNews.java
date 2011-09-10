package org.ohm.lebetter.tapestry5.web.components.mallcomponents.control;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.util.StringUtil;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 17:47:04
 * To change this template use File | Settings | File Templates.
 */
public class TrimmedNews extends AbstractBaseComponent {

    private static final int BUFFER_SIZE = 512;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String value;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "8")
    private int length;

    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL, value = "...")
    private String suffix;

    void beginRender(final MarkupWriter writer) throws Exception {
        writer.writeRaw(getFilteredBody(value, length, suffix).trim());
    }

    public static String getFilteredBody(final String body,
                                         final int length,
                                         final String suffix,
                                         final int bufferSize) throws Exception {
        if (StringUtil.isEmpty(body)) {
            return "";
        }

        Reader reader = new Reader() {

            private int pos = -1;
            private final char[] chars = StringEscapeUtils.unescapeHtml(body).toCharArray();
            private boolean special = false;

            @Override
            public int read(char[] chars, int i, int i1) throws IOException {
                throw new RuntimeException("not implemented");
            }

            @Override
            public void close() throws IOException {
                throw new RuntimeException("not implemented");
            }

            @Override
            public int read() throws IOException {
                pos++;
                if (pos == chars.length) {
                    return -1;
                } else if (chars[pos] == '&' || special && chars[pos] != ';') {
                    special = true;
                    return chars[pos];
                } else if (chars[pos] == ';' && special) {
                    special = false;
                    return chars[pos];
                } else if (chars[pos] != '<') {
                    return chars[pos];
                } else {
                    while (pos < (chars.length - 1) && chars[pos] != '>') {
                        pos++;
                    }
                    pos++;
                    if (pos == chars.length) {
                        return -1;
                    } else if (chars[pos] == '<') {
                        while (pos < (chars.length - 1) && chars[pos] != '>') {
                            pos++;
                        }
                        pos++;
                    }
                    return pos == chars.length ? -1 : chars[pos];
                }
            }
        };

        char[] buffer = new char[bufferSize];
        char symbol;
        int bufPos = -1;
        int totalPos = -1;
        boolean trimmed = false;
        StringBuffer result = new StringBuffer();

        while ((symbol = (char) reader.read()) != (char) -1) {
            totalPos++;
            if (totalPos == length) {
                trimmed = true;
                break;
            }
            bufPos++;
            if (bufPos == bufferSize) {
                bufPos = 0;
                result.append(buffer);
            }
            buffer[bufPos] = symbol;
        }

        if (bufPos != bufferSize && bufPos != -1) {
            result.append(buffer, 0, bufPos + 1);
        }

        if (trimmed) {
            result.append(suffix);
        }

        return StringUtil.transformHTML(result.toString());
    }

    public static String getFilteredBody(final String body,
                                         final int length,
                                         final String suffix)
            throws Exception {
        return getFilteredBody(body, length, suffix, BUFFER_SIZE);
    }

}