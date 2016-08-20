package io.github.cubedtear.jcubit.bds;

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
        this.data = data;
        this.length = length;
    }


    @Override
    public int read() throws IOException {
        if (index >= length)
            throw new ArrayIndexOutOfBoundsException("OutputStream size exceeded!");
        return data[index++] & 0xFF;
    }

    public int getLeft() {
        return length - index;
    }
}
