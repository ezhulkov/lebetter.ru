package org.ohm.lebetter.spring.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Пропускает часть текста, ограниченного start и end последовательностями.
 * Оставляет сами start и end.
 */
public class SkipInputStream extends InputStream {
    protected InputStream source;
    protected OutputStream wasteOut;
    protected boolean keepSeparators;
    protected char[] start;
    protected char[] end;
    protected char[] backBuf;

    protected State state = State.NORMAL;
    protected int startIdx = 0;
    protected int endIdx = 0;

    public SkipInputStream(InputStream source, OutputStream offersPart,
                           String startSequence, String endSequence)
            throws FileNotFoundException {
        this.source = source;
        this.wasteOut = offersPart;
        this.start = startSequence.toCharArray();
        this.end = endSequence.toCharArray();
        this.backBuf = new char[end.length];
    }

    @Override
    public int read() throws IOException {
        int c;

        switch (state) {
            case SKIP:
                while ((c = source.read()) > 0) {
                    if (c == end[endIdx]) {
                        backBuf[endIdx] = (char) c;
                        endIdx++;

                        if (endIdx == end.length) {
                            state = State.BACK;
                            break;
                        }
                    } else {
                        if (wasteOut != null) {
                            for (int i = 0; i < endIdx; i++) {
                                wasteOut.write(backBuf[i]);
                            }
                            wasteOut.write(c);
                        }
                        endIdx = 0;
                    }
                }
                return -1;
            case BACK:
                if (endIdx == 1) {
                    state = State.NORMAL;
                }
                return end[end.length - (endIdx--)];
            default:
                if ((c = source.read()) == start[startIdx]) {
                    startIdx++;
                    if (startIdx == start.length) {
                        state = State.SKIP;
                        startIdx = 0;
                    }
                } else if (c == start[0]) {
                    startIdx = 1;
                } else {
                    startIdx = 0;
                }
                return c;
        }
    }

    @Override
    public void close() throws IOException {
    }

    protected static enum State {
        NORMAL, SKIP, BACK
    }
}
