package io.github.cubedtear.jcubit.bds;

import io.github.cubedtear.jcubit.util.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Aritz Lopez
 */
public class BAIS extends InputStream {

    private final byte[] data;
    private final int length;
    private int index = 0;

    public BAIS(byte[] data) {
        this(data, 0, data.length);
    }

    public BAIS(byte[] data, int offset) {
        this(data, offset, data.length - offset);
    }

    public BAIS(byte[] data, int offset, int length) {
        if (length < 0)
            throw new IllegalArgumentException("Length cannot be negative!");
        if (data == null && length != 0)
            throw new IllegalArgumentException("Data cannot be null, unless length is 0");
        if (data != null && offset + length > data.length)
            throw new IllegalArgumentException("Offset + length (" + (offset + length) + ") > data.length (" + data.length + ")");
        this.index = offset;
        this.data = data;
        this.length = length + offset;
    }


    @Override
    public int read() throws IOException {
        return data[index++] & 0xFF;
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        if (len == 0) return 0;
        int read = Math.min(len, this.getLeft());
        System.arraycopy(this.data, index, b, off, read);
        this.index += read;
        return read;
    }

    public int getLeft() {
        return length - index;
    }

    public byte[] getData() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public long skip(long n) throws IOException {
        long toSkip = Math.min(n, getLeft());
        this.index += toSkip;
        return toSkip;
    }
}
