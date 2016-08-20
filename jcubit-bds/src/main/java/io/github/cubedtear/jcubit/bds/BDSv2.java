package io.github.cubedtear.jcubit.bds;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.cubedtear.jcubit.collections.ArrayUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
public class BDSv2 {

    public final static byte[] SIGNATURE = ".BDSv2\r\n".getBytes(StandardCharsets.UTF_8);

    // region ... Collection of elements ...

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
    private Map<String, BDSv2> bdss = Maps.newHashMap();
    private Map<String, BDSv2[]> bdsArrays = Maps.newHashMap();

    // endregion

    private int size = 0;

    public BDSv2() {

    }

    public int getSize() {
        // Size of the signature: Integer (size) + data
        return SIGNATURE.length + 4 + getInternalSize();
    }

    private int getInternalSize() {
        int partial = size;
        for (BDSv2 b : bdss.values()) partial += b.getInternalSize();
        for (BDSv2[] ba : bdsArrays.values()) for (BDSv2 b : ba) partial += b.getInternalSize();
        return partial;
    }

    private void checkAndAddName(String name) {
        if (takenNames.contains(name)) throw new IllegalArgumentException("Name \"" + name + "\" is already taken!");
        takenNames.add(name);
        this.size += name.getBytes(StandardCharsets.UTF_8).length + 4;
    }

    // region ... Adders ...

    // region ... Normal adders ...
    public void addByte(String name, byte s) {
        checkAndAddName(name);
        bytes.put(name, s);
        this.size += 1 + 1;
    }

    public void addShort(String name, short s) {
        checkAndAddName(name);
        shorts.put(name, s);
        this.size += 2 + 1;
    }

    public void addChar(String name, char s) {
        checkAndAddName(name);
        chars.put(name, s);
        this.size += 2 + 1;
    }

    public void addInt(String name, int s) {
        checkAndAddName(name);
        ints.put(name, s);
        this.size += 4 + 1;
    }

    public void addLong(String name, long s) {
        checkAndAddName(name);
        longs.put(name, s);
        this.size += 8 + 1;
    }

    public void addFloat(String name, float s) {
        checkAndAddName(name);
        floats.put(name, s);
        this.size += 4 + 1;
    }

    public void addDouble(String name, double s) {
        checkAndAddName(name);
        doubles.put(name, s);
        this.size += 8 + 1;
    }

    public void addString(String name, String s) {
        checkAndAddName(name);
        strings.put(name, s);
        this.size += s.getBytes(StandardCharsets.UTF_8).length + 4 + 1;
    }

    public void addBDS(String name, BDSv2 s) {
        checkAndAddName(name);
        bdss.put(name, s);
        this.size += 1 + 4; // Size is calculated each time in getSize(), since BDSs are mutable
    }
    // endregion

    // region ... Array adders ...
    public void addBytes(String name, byte[] s) {
        checkAndAddName(name);
        byteArrays.put(name, ArrayUtil.box(s));
        this.size += s.length + 1 + 4;
    }

    public void addShorts(String name, short[] s) {
        checkAndAddName(name);
        shortArrays.put(name, ArrayUtil.box(s));
        this.size += 2 * s.length + 1 + 4;
    }

    public void addChars(String name, char[] s) {
        checkAndAddName(name);
        charArrays.put(name, ArrayUtil.box(s));
        this.size += 2 * s.length + 1 + 4;
    }

    public void addInts(String name, int[] s) {
        checkAndAddName(name);
        intArrays.put(name, ArrayUtil.box(s));
        this.size += 4 * s.length + 1 + 4;
    }

    public void addLongs(String name, long[] s) {
        checkAndAddName(name);
        longArrays.put(name, ArrayUtil.box(s));
        this.size += 8 * s.length + 1 + 4;
    }

    public void addFloats(String name, float[] s) {
        checkAndAddName(name);
        floatArrays.put(name, ArrayUtil.box(s));
        this.size += 4 * s.length + 1 + 4;
    }

    public void addDoubles(String name, double[] s) {
        checkAndAddName(name);
        doubleArrays.put(name, ArrayUtil.box(s));
        this.size += 8 * s.length + 1 + 4;
    }

    public void addStrings(String name, String[] s) {
        checkAndAddName(name);
        stringArrays.put(name, s);
        for (String ss : s) this.size += ss.getBytes(StandardCharsets.UTF_8).length;
        this.size += 1 + 4;
    }

    public void addBDSs(String name, BDSv2[] s) {
        checkAndAddName(name);
        bdsArrays.put(name, s);
        this.size += 1 + 4 + s.length * 4; // Size is calculated each time in getSize(), since BDSs are mutable
    }
    // endregion

    // endregion

    // region ... Getters ...

    // region ... Normal getters ...

    public Byte getByte(String name) {
        return this.bytes.get(name);
    }

    public Character getChar(String name) {
        return this.chars.get(name);
    }

    public Short getShort(String name) {
        return this.shorts.get(name);
    }

    public Integer getInt(String name) {
        return this.ints.get(name);
    }

    public Long getLong(String name) {
        return this.longs.get(name);
    }

    public Float getFloat(String name) {
        return this.floats.get(name);
    }

    public Double getDouble(String name) {
        return this.doubles.get(name);
    }

    public String getString(String name) {
        return this.strings.get(name);
    }

    public BDSv2 getBDS(String name) {
        return this.bdss.get(name);
    }

    // endregion

    // region ... Array getters ...

    public Byte[] getBytes(String name) {
        return this.byteArrays.get(name);
    }

    public Character[] getChars(String name) {
        return this.charArrays.get(name);
    }

    public Short[] getShorts(String name) {
        return this.shortArrays.get(name);
    }

    public Integer[] getInts(String name) {
        return this.intArrays.get(name);
    }

    public Long[] getLongs(String name) {
        return this.longArrays.get(name);
    }

    public Float[] getFloats(String name) {
        return this.floatArrays.get(name);
    }

    public Double[] getDoubles(String name) {
        return this.doubleArrays.get(name);
    }

    public String[] getStrings(String name) {
        return this.stringArrays.get(name);
    }

    public BDSv2[] getBDSs(String name) {
        return this.bdsArrays.get(name);
    }

    // endregion

    // endregion

    // region ... Writers ...

    public byte[] write() {
        byte[] data = new byte[this.getSize()];
        try {
            writeToStream(new BAOS(data));
        } catch (IOException ignored) {
            throw new AssertionError("BAOS cannot throw IOException");
        }
        return data;
    }

    public void writeToStream(OutputStream os) throws IOException {
        os.write(SIGNATURE);
        writeInternal(os);
    }

    private void writeInternal(OutputStream os) throws IOException {
        writeInt(os, getInternalSize());

        // region ... Normal elements ...

        for (Map.Entry<String, Byte> e : this.bytes.entrySet()) {
            writeByte(os, BDSv2Type.BYTE.getSignature(false));
            writeString(os, e.getKey());
            writeByte(os, e.getValue());
        }

        for (Map.Entry<String, Short> e : this.shorts.entrySet()) {
            writeByte(os, BDSv2Type.SHORT.getSignature(false));
            writeString(os, e.getKey());
            writeShort(os, e.getValue());
        }
        for (Map.Entry<String, Character> e : this.chars.entrySet()) {
            writeByte(os, BDSv2Type.CHAR.getSignature(false));
            writeString(os, e.getKey());
            writeChar(os, e.getValue());
        }
        for (Map.Entry<String, Integer> e : this.ints.entrySet()) {
            writeByte(os, BDSv2Type.INT.getSignature(false));
            writeString(os, e.getKey());
            writeInt(os, e.getValue());
        }
        for (Map.Entry<String, Long> e : this.longs.entrySet()) {
            writeByte(os, BDSv2Type.LONG.getSignature(false));
            writeString(os, e.getKey());
            writeLong(os, e.getValue());
        }
        for (Map.Entry<String, Float> e : this.floats.entrySet()) {
            writeByte(os, BDSv2Type.FLOAT.getSignature(false));
            writeString(os, e.getKey());
            writeFloat(os, e.getValue());
        }
        for (Map.Entry<String, Double> e : this.doubles.entrySet()) {
            writeByte(os, BDSv2Type.DOUBLE.getSignature(false));
            writeString(os, e.getKey());
            writeDouble(os, e.getValue());
        }
        for (Map.Entry<String, String> e : this.strings.entrySet()) {
            writeByte(os, BDSv2Type.STRING.getSignature(false));
            writeString(os, e.getKey());
            writeString(os, e.getValue());
        }
        for (Map.Entry<String, BDSv2> e : this.bdss.entrySet()) {
            writeByte(os, BDSv2Type.BDS.getSignature(false));
            writeString(os, e.getKey());
            e.getValue().writeInternal(os);
        }

        // endregion
        // region ... Array types ...

        for (Map.Entry<String, Byte[]> e : this.byteArrays.entrySet()) {
            writeByte(os, BDSv2Type.BYTE.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (Byte b : e.getValue()) writeByte(os, b);
        }

        for (Map.Entry<String, Short[]> e : this.shortArrays.entrySet()) {
            writeByte(os, BDSv2Type.SHORT.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (Short b : e.getValue()) writeShort(os, b);
        }
        for (Map.Entry<String, Character[]> e : this.charArrays.entrySet()) {
            writeByte(os, BDSv2Type.CHAR.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (Character b : e.getValue()) writeChar(os, b);
        }
        for (Map.Entry<String, Integer[]> e : this.intArrays.entrySet()) {
            writeByte(os, BDSv2Type.INT.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (Integer b : e.getValue()) writeInt(os, b);
        }
        for (Map.Entry<String, Long[]> e : this.longArrays.entrySet()) {
            writeByte(os, BDSv2Type.LONG.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (Long b : e.getValue()) writeLong(os, b);
        }
        for (Map.Entry<String, Float[]> e : this.floatArrays.entrySet()) {
            writeByte(os, BDSv2Type.FLOAT.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (Float b : e.getValue()) writeFloat(os, b);
        }
        for (Map.Entry<String, Double[]> e : this.doubleArrays.entrySet()) {
            writeByte(os, BDSv2Type.DOUBLE.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (Double b : e.getValue()) writeDouble(os, b);
        }
        for (Map.Entry<String, String[]> e : this.stringArrays.entrySet()) {
            writeByte(os, BDSv2Type.STRING.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (String b : e.getValue()) writeString(os, b);
        }
        for (Map.Entry<String, BDSv2[]> e : this.bdsArrays.entrySet()) {
            writeByte(os, BDSv2Type.BDS.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (BDSv2 b : e.getValue()) b.writeInternal(os);
        }

        // endregion
    }

    // region ... Internal writers ...
    private static void writeString(OutputStream os, String s) throws IOException {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        writeInt(os, bytes.length);
        os.write(bytes);
    }

    private static void writeByte(OutputStream os, byte s) throws IOException {
        os.write(s);
    }

    private static void writeShort(OutputStream os, short s) throws IOException {
        os.write(s >> 8);
        os.write(s);
    }

    private static void writeChar(OutputStream os, char s) throws IOException {
        os.write(s >> 8);
        os.write(s);
    }

    private static void writeInt(OutputStream os, int s) throws IOException {
        os.write(s >> 24);
        os.write(s >> 16);
        os.write(s >> 8);
        os.write(s);
    }

    private static void writeLong(OutputStream os, long s) throws IOException {
        os.write((byte) (s >> 56));
        os.write((byte) (s >> 48));
        os.write((byte) (s >> 40));
        os.write((byte) (s >> 32));
        os.write((byte) (s >> 24));
        os.write((byte) (s >> 16));
        os.write((byte) (s >> 8));
        os.write((byte) s);
    }

    private static void writeFloat(OutputStream os, float s) throws IOException {
        writeInt(os, Float.floatToIntBits(s));
    }

    private static void writeDouble(OutputStream os, double s) throws IOException {
        writeLong(os, Double.doubleToLongBits(s));
    }

    // endregion

    // endregion

    // region ... Parsers ...

    public static BDSv2 parse(byte[] data) throws SerializationException {
        try {
            return parseStream(new BAIS(data));
        } catch (IOException e) {
            throw new AssertionError("BAIS never throws an IO Exception");
        }
    }

    public static BDSv2 parseStream(InputStream is) throws IOException, SerializationException {
        for (byte b : SIGNATURE)
            if ((byte) is.read() != b) throw new SerializationException("BDSv2 signature not present, or incorrect!");
        return parseStreamInternal(is);
    }

    private static BDSv2 parseStreamInternal(InputStream is) throws IOException, SerializationException {
        BDSv2 bds = new BDSv2();
        int length = parseInt(is);
        BAIS dis = new BAIS(readBytes(is, length));

        while (dis.getLeft() > 0) {
            byte signature = (byte) dis.read();
            String name = parseString(dis);
            BDSv2Type type = BDSv2Type.fromSignature(signature);
            if (type == null)
                throw new SerializationException("Unknown type signature: " + Integer.toHexString(signature & 0xFF));
            switch (type) {
                case BYTE:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        byte[] array = new byte[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseByte(dis);
                        }
                        bds.addBytes(name, array);
                    } else {
                        bds.addByte(name, parseByte(dis));
                    }
                    break;
                case CHAR:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        char[] array = new char[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseChar(dis);
                        }
                        bds.addChars(name, array);
                    } else {
                        bds.addChar(name, parseChar(dis));
                    }
                    break;
                case SHORT:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        short[] array = new short[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseShort(dis);
                        }
                        bds.addShorts(name, array);
                    } else {
                        bds.addShort(name, parseShort(dis));
                    }
                    break;
                case INT:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        int[] array = new int[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseInt(dis);
                        }
                        bds.addInts(name, array);
                    } else {
                        bds.addInt(name, parseInt(dis));
                    }
                    break;
                case LONG:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        long[] array = new long[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseLong(dis);
                        }
                        bds.addLongs(name, array);
                    } else {
                        bds.addLong(name, parseLong(dis));
                    }
                    break;
                case FLOAT:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        float[] array = new float[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseFloat(dis);
                        }
                        bds.addFloats(name, array);
                    } else {
                        bds.addFloat(name, parseFloat(dis));
                    }
                    break;
                case DOUBLE:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        double[] array = new double[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseDouble(dis);
                        }
                        bds.addDoubles(name, array);
                    } else {
                        bds.addDouble(name, parseDouble(dis));
                    }
                    break;
                case BDS:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        BDSv2[] array = new BDSv2[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseStreamInternal(dis);
                        }
                        bds.addBDSs(name, array);
                    } else {
                        bds.addBDS(name, BDSv2.parseStreamInternal(dis));
                    }
                    break;
                case STRING:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(dis);
                        String[] array = new String[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseString(dis);
                        }
                        bds.addStrings(name, array);
                    } else {
                        bds.addString(name, parseString(dis));
                    }
                    break;
            }
        }
        return bds;
    }

    // region ... Internal parsers ...

    private static byte parseByte(InputStream is) throws IOException {
        return (byte) is.read();
    }

    private static char parseChar(InputStream is) throws IOException {
        return (char) (is.read() << 8 | (is.read() & 0xFF));
    }

    private static short parseShort(InputStream is) throws IOException {
        return (short) (is.read() << 8 | (is.read() & 0xFF));
    }

    private static int parseInt(InputStream is) throws IOException {
        return is.read() << 24 | (is.read() & 0xFF) << 16 | (is.read() & 0xFF) << 8 | (is.read() & 0xFF);
    }

    private static long parseLong(InputStream is) throws IOException {
        return ((long) is.read()) << 56L | (is.read() & 0xFFL) << 48L | (is.read() & 0xFFL) << 40L | (is.read() & 0xFFL) << 32 | (is.read() & 0xFFL) << 24 | (is.read() & 0xFFL) << 16 | (is.read() & 0xFFL) << 8 | (is.read() & 0xFFL);
    }

    private static float parseFloat(InputStream is) throws IOException {
        return Float.intBitsToFloat(parseInt(is));
    }

    private static double parseDouble(InputStream is) throws IOException {
        return Double.longBitsToDouble(parseLong(is));
    }

    private static String parseString(InputStream is) throws IOException {
        return new String(readBytes(is, parseInt(is)), StandardCharsets.UTF_8);
    }

    private static byte[] readBytes(InputStream is, int bytes) throws IOException {
        byte[] data = new byte[bytes];
        int written = 0;
        while (written < bytes) {
            int read = is.read(data, written, bytes - written);
            if (read == -1) {
                throw new IOException("Error reading from stream!");
            }
            written += read;
        }
        return data;
    }

    // endregion

    // endregion

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
                types.put(t.getSignature(false), t);
                types.put(t.getSignature(true), t);
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
