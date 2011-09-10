package org.ohm.lebetter.spring.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;


public class SkipWithBoundsInputStream extends InputStream {
    protected Reader source;
    protected Writer wasteOut;
    protected boolean keepSeparators;
    protected char[] start;
    protected char[] end;
    protected char[] backBuf;

    protected int startIdx = 0;
    protected int endIdx = 0;

    public SkipWithBoundsInputStream(Reader source, Writer offersPart,
                                     String startSequence, String endSequence) throws FileNotFoundException {
        this.source = source;
        this.wasteOut = offersPart;
        this.start = startSequence.toCharArray();
        this.end = endSequence.toCharArray();
        this.backBuf = new char[end.length];
    }

    @Override
    public int read() throws IOException {
        throw new RuntimeException();
    }

    @Override
    public void close() throws IOException {
    }
}
