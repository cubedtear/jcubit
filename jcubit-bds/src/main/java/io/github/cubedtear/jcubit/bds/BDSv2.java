package io.github.cubedtear.jcubit.bds;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
public class BDSv2 {

    public final static byte[] SIGNATURE = ".BDSv2\r\n".getBytes(StandardCharsets.UTF_8);
    private final static byte[] NEW_LINE = "\r\n".getBytes(StandardCharsets.UTF_8);

    private transient Set<String> takenNames = Sets.newHashSet();
    private Map<String, String> strings = Maps.newHashMap();
    private Map<String, Integer> ints = Maps.newHashMap();
    private Map<String, Byte> bytes = Maps.newHashMap();
    private Map<String, Character> chars = Maps.newHashMap();
    private Map<String, Long> longs = Maps.newHashMap();
    private Map<String, Short> shorts = Maps.newHashMap();
    private Map<String, Float> floats = Maps.newHashMap();
    private Map<String, Double> doubles = Maps.newHashMap();
    private Map<String, String[]> stringArrays = Maps.newHashMap();
    private Map<String, Integer[]> intArrays = Maps.newHashMap();
    private Map<String, Byte[]> byteArrays = Maps.newHashMap();
    private Map<String, Character[]> charArrays = Maps.newHashMap();
    private Map<String, Long[]> longArrays = Maps.newHashMap();
    private Map<String, Short[]> shortArrays = Maps.newHashMap();
    private Map<String, Float[]> floatArrays = Maps.newHashMap();
    private Map<String, Double[]> doubleArrays = Maps.newHashMap();
    private Map<String, BDS> bdss = Maps.newHashMap();
    private Map<String, BDS[]> bdsArrays = Maps.newHashMap();

    private int size;
    private static final int extraFileSize = SIGNATURE.length;

    public BDSv2() {

    }

    public int getSize() {
        // Size of the signature, + BDS signature + size (int) + data
        return SIGNATURE.length + 1 + 4 + size;
    }

    private void checkAndAddName(String name) {
        if (takenNames.contains(name)) throw new IllegalArgumentException("Name \"" + name + "\" is already taken!");
        takenNames.add(name);
        this.size += name.getBytes(StandardCharsets.UTF_8).length;
    }

    public void addByte(String name, byte s) {
        checkAndAddName(name);
        bytes.put(name, s);
        this.size += 1;
    }

    public void addShort(String name, short s) {
        checkAndAddName(name);
        shorts.put(name, s);
        this.size += 2;
    }

    public void addChar(String name, char s) {
        checkAndAddName(name);
        chars.put(name, s);
        this.size += 2;
    }

    public void addInt(String name, int s) {
        checkAndAddName(name);
        ints.put(name, s);
        this.size += 4;
    }

    public void addLong(String name, long s) {
        checkAndAddName(name);
        longs.put(name, s);
        this.size += 8;
    }

    public void addFloat(String name, float s) {
        checkAndAddName(name);
        floats.put(name, s);
        this.size += 4;
    }

    public void addDouble(String name, double s) {
        checkAndAddName(name);
        doubles.put(name, s);
        this.size += 8;
    }

    public void addString(String name, String s) {
        checkAndAddName(name);
        strings.put(name, s);
        this.size += s.getBytes(StandardCharsets.UTF_8).length;
    }

    // TODO: Add the adders for arrays

    public byte[] write() {
        byte[] data = new byte[this.getSize()];
        writeToStream(new BAOS(data));
        return data;
    }

    public void writeToStream(OutputStream os) {
        // TODO Serialize everything
    }

    private void writeString(OutputStream os, int[] index, String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeInt(os, index, bytes.length);
        os.write(bytes);
        index[0] += bytes.length;
    }

    private void writeByte(OutputStream os, int[] index, byte s) throws IOException {
        os.write(s);
        index[0]++;
    }

    private void writeShort(OutputStream os, int[] index, short s) throws IOException {
        os.write(s >> 8);
        os.write(s);

        index[0] += 2;
    }

    private void writeChar(OutputStream os, int[] index, char s) throws IOException {
        os.write(s >> 8);
        os.write(s);

        index[0] += 2;
    }

    private void writeInt(OutputStream os, int[] index, int s) throws IOException {
        os.write(s >> 24);
        os.write(s >> 16);
        os.write(s >> 8);
        os.write(s);

        index[0] += 4;
    }

    private void writeLong(OutputStream os, int[] index, long s) throws IOException {
        os.write((byte) (s >> 56));
        os.write((byte) (s >> 48));
        os.write((byte) (s >> 40));
        os.write((byte) (s >> 32));
        os.write((byte) (s >> 24));
        os.write((byte) (s >> 16));
        os.write((byte) (s >> 8));
        os.write((byte) s);

        index[0] += 8;
    }

    private void writeFloat(OutputStream os, int[] index, float s) throws IOException {
        writeInt(os, index, Float.floatToIntBits(s));
    }

    private void writeDouble(OutputStream os, int[] index, double s) throws IOException {
        writeLong(os, index, Double.doubleToLongBits(s));
    }

    private enum BDSv2Type {
        BYTE((byte) 0x01),
        CHAR((byte) 0x02),
        SHORT((byte) 0x03),
        INT((byte) 0x04),
        LONG((byte) 0x05),
        FLOAT((byte) 0x06),
        DOUBLE((byte) 0x07),
        BDS((byte) 0x08),
        STRING((byte) 0x09);

        private static final Map<Byte, BDSv2Type> TYPE_MAP;
        private static final byte ARRAY_MASK = 0x20;

        static {
            Map<Byte, BDSv2Type> types = Maps.newHashMap();
            for (BDSv2Type t : BDSv2Type.values()) {
                types.put(t.signature, t);
            }
            TYPE_MAP = ImmutableMap.copyOf(types);
        }

        private final byte signature;

        BDSv2Type(byte signature) {
            this.signature = signature;
        }

        private static BDSv2Type fromSignature(byte signature) {
            return TYPE_MAP.get(signature);
        }

        private static boolean isArray(byte signature) {
            return (signature & ARRAY_MASK) != 0;
        }

        public byte getSignature(boolean array) {
            return array ? (byte) (signature | ARRAY_MASK) : signature;
        }
    }
}
