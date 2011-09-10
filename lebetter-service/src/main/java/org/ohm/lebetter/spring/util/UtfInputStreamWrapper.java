package org.ohm.lebetter.spring.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Читающий поток, выбрасывающий все <%...%>
 * Работает медленно по причине побайтового чтения
 */
public class UtfInputStreamWrapper extends InputStream {

    private final InputStream src;
    public static final int STATE_OK = 0;
    public static final int STATE_SKIP = 1;
    private int state = STATE_OK;

    public UtfInputStreamWrapper(InputStream is) {
        src = is;
    }

    public int getState() {
        return state;
    }

    @Override
    @SuppressWarnings("PMD")
    public int read() throws IOException {
        // читаем байт
        int b = src.read();
        // если уже был "<", возможно появление "%" и пропуск. проверяем
        if (state == STATE_SKIP) {
            // если "%", начинаем пропуск
            if (b == 37) {
                // в цикле до тех пор, пока не будет "<"
                while ((b = src.read()) != 60 && b > 0) {
                    //do nothing;
                }
                // возвращаем след. символ после "<"
                return src.read();
            }
            state = STATE_OK;
        }
        // если "<", возможно появление "%"
        if (b == 60) {
            state = STATE_SKIP;
        }
        return b;
    }
}
