/*
 * Copyright (c) 2021. Roman P.
 * All code is owned by Roman P. APIs are mentioned.
 * Last modified: 26.02.21, 21:42
 */

package net.bplaced.abzzezz.vivodownloader.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.function.Consumer;

/**
 * https://stackoverflow.com/questions/2263062/how-to-monitor-progress-jprogressbar-with-filechannels-transferfrom-method
 */
public class RBCWrapper implements ReadableByteChannel {

    private final Consumer<Integer> progressConsumer;
    private final ReadableByteChannel rbc;

    public RBCWrapper(ReadableByteChannel rbc, Consumer<Integer> progressConsumer) {
        this.progressConsumer = progressConsumer;
        this.rbc = rbc;
    }

    public void close() throws IOException {
        rbc.close();
    }

    public boolean isOpen() {
        return rbc.isOpen();
    }

    public int read(ByteBuffer bb) throws IOException {
        int n;

        if ((n = rbc.read(bb)) > 0) {
            progressConsumer.accept(n);
        }
        return n;
    }
}