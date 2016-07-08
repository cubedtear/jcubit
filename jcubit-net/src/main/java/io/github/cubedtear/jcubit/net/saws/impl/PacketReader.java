package io.github.cubedtear.jcubit.net.saws.impl;

import com.google.common.primitives.Ints;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Aritz Lopez
 */
public class PacketReader {

    private static final byte[] INITIAL_MAGIC = new byte[]{0xB, 0xE, 0xE, 0xF};
    private static final byte[] FINAL_MAGIC = new byte[]{0xF, 0xA, 0xC, 0xE};

    public static byte[] readPacket(InputStream inStream) throws IOException {
        int read;
        byte[] buffer = new byte[4];

        if ((read = inStream.read()) == -1 || read != INITIAL_MAGIC[0]) return null;
        if ((read = inStream.read()) == -1 || read != INITIAL_MAGIC[1]) return null;
        if ((read = inStream.read()) == -1 || read != INITIAL_MAGIC[2]) return null;
        if ((read = inStream.read()) == -1 || read != INITIAL_MAGIC[3]) return null;

        if ((read = inStream.read()) != -1) buffer[0] = (byte) read;
        else return null;

        if ((read = inStream.read()) != -1) buffer[1] = (byte) read;
        else return null;

        if ((read = inStream.read()) != -1) buffer[2] = (byte) read;
        else return null;

        if ((read = inStream.read()) != -1) buffer[3] = (byte) read;
        else return null;

        int payloadLength = Ints.fromByteArray(buffer);
        ByteArrayOutputStream baos = new ByteArrayOutputStream(payloadLength);
        int readBytes = 0;
        while (readBytes < payloadLength) {
            read = inStream.read(buffer, 0, Math.min(payloadLength - readBytes, buffer.length));
            if (read == -1) return null;
            baos.write(buffer, 0, read);
            readBytes += read;
        }
        baos.close();

        if ((read = inStream.read()) == -1 || read != FINAL_MAGIC[0]) return null;
        if ((read = inStream.read()) == -1 || read != FINAL_MAGIC[1]) return null;
        if ((read = inStream.read()) == -1 || read != FINAL_MAGIC[2]) return null;
        if ((read = inStream.read()) == -1 || read != FINAL_MAGIC[3]) return null;
        return baos.toByteArray();
    }
}
