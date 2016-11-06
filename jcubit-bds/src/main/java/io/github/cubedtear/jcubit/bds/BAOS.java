package io.github.cubedtear.jcubit.bds;

import io.github.cubedtear.jcubit.util.NotNull;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Aritz Lopez
 */
public class BAOS extends OutputStream {

    private final byte[] data;
    private final int length;
    private int index = 0;

    public BAOS(byte[] data) {
        this(data, 0, data.length);
    }

    public BAOS(byte[] data, int offset) {
        this(data, offset, data.length - offset);
    }

    public BAOS(byte[] data, int offset, int length) {
        if (length < 0)
            throw new IllegalArgumentException("Length cannot be negative!");
        else if (data == null && length != 0)
            throw new IllegalArgumentException("Data cannot be null, unless length is 0");
        else if (data != null && offset + length > data.length)
            throw new IllegalArgumentException("Offset + length (" + (offset + length) + ") > data.length (" + data.length + ")");
        this.data = data;
        this.length = length;
    }

    @Override
    public void write(int b) throws IOException {
        data[index++] = (byte) b;
    }

    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        if (len == 0) return;
        System.arraycopy(b, off, this.data, index, len);
        this.index += len;
    }
}
