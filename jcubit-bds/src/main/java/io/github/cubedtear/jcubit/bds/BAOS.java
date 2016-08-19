package io.github.cubedtear.jcubit.bds;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Aritz Lopez
 */
public class BAOS extends OutputStream {

    private final byte[] data;
    private final int offset;
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
        if (data == null && length != 0)
            throw new IllegalArgumentException("Data cannot be null, unless length is 0");
        if (offset + length > data.length)
            throw new IllegalArgumentException("Offset + length (" + (offset + length) + ") > data.length (" + data.length + ")");
        this.data = data;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public void write(int b) throws IOException {
        if (index >= length) throw new ArrayIndexOutOfBoundsException("OutputStream size exceeded!");
        data[index++] = (byte) b;
    }
}
