package io.github.cubedtear.jcubit.bds;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.util.Set2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Binary Data Storage version 2
 * Used to serialize different data types into byte arrays or OutputStreams,
 * write them somewhere (file, DB, network, etc.), and then read them back.
 *
 * @author Aritz Lopez
 */
public class BDSv2 {

    /**
     * The sequence of bytes that will always be at the beginning of a serialized BDSv2.
     * Useful to detect a BDSv2-serialized file, when opened with a text editor.
     */
    public final static byte[] SIGNATURE = ".BDSv2\r\n".getBytes(StandardCharsets.UTF_8);

    // region ... Collection of elements ...

    private transient Map<String, byte[]> takenNames = Maps.newHashMap();
    private Map<String, Set2<String, byte[]>> strings = Maps.newHashMap();
    private Map<String, Integer> ints = Maps.newHashMap();
    private Map<String, Byte> bytes = Maps.newHashMap();
    private Map<String, Character> chars = Maps.newHashMap();
    private Map<String, Long> longs = Maps.newHashMap();
    private Map<String, Short> shorts = Maps.newHashMap();
    private Map<String, Float> floats = Maps.newHashMap();
    private Map<String, Double> doubles = Maps.newHashMap();
    private Map<String, Set2<String, byte[]>[]> stringArrays = Maps.newHashMap();
    private Map<String, int[]> intArrays = Maps.newHashMap();
    private Map<String, byte[]> byteArrays = Maps.newHashMap();
    private Map<String, char[]> charArrays = Maps.newHashMap();
    private Map<String, long[]> longArrays = Maps.newHashMap();
    private Map<String, short[]> shortArrays = Maps.newHashMap();
    private Map<String, float[]> floatArrays = Maps.newHashMap();
    private Map<String, double[]> doubleArrays = Maps.newHashMap();
    private Map<String, BDSv2> bdss = Maps.newHashMap();
    private Map<String, BDSv2[]> bdsArrays = Maps.newHashMap();

    // endregion

    private int size = 0;

    /**
     * Creates an empty BDSv2.
     */
    public BDSv2() {

    }

    /**
     * Calculates the bytes this BDSv2 will take once serialized.
     *
     * @return the size of this BDSv2 in bytes.
     */
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
        if (takenNames.containsKey(name)) throw new IllegalArgumentException("Name \"" + name + "\" is already taken!");
        try {
            byte[] bytes = name.getBytes("UTF-8");
            takenNames.put(name, bytes);
            this.size += bytes.length + 4;
        } catch (UnsupportedEncodingException ignored) {
            throw new AssertionError("UTF-8 encoding is not found!");
        }
    }

    // region ... Adders ...

    // region ... Normal adders ...

    /**
     * Adds the given byte to this BDSv2 with the given name.
     *
     * @param name The name to give the byte.
     * @param s    The byte to store.
     */
    public void addByte(String name, byte s) {
        checkAndAddName(name);
        bytes.put(name, s);
        this.size += 1 + 1;
    }

    /**
     * Adds the given short to this BDSv2 with the given name.
     *
     * @param name The name to give the short.
     * @param s    The short to store.
     */
    public void addShort(String name, short s) {
        checkAndAddName(name);
        shorts.put(name, s);
        this.size += 2 + 1;
    }

    /**
     * Adds the given char to this BDSv2 with the given name.
     *
     * @param name The name to give the char.
     * @param s    The char to store.
     */
    public void addChar(String name, char s) {
        checkAndAddName(name);
        chars.put(name, s);
        this.size += 2 + 1;
    }

    /**
     * Adds the given int to this BDSv2 with the given name.
     *
     * @param name The name to give the int.
     * @param s    The int to store.
     */
    public void addInt(String name, int s) {
        checkAndAddName(name);
        ints.put(name, s);
        this.size += 4 + 1;
    }

    /**
     * Adds the given long to this BDSv2 with the given name.
     *
     * @param name The name to give the long.
     * @param s    The long to store.
     */
    public void addLong(String name, long s) {
        checkAndAddName(name);
        longs.put(name, s);
        this.size += 8 + 1;
    }

    /**
     * Adds the given float to this BDSv2 with the given name.
     *
     * @param name The name to give the float.
     * @param s    The float to store.
     */
    public void addFloat(String name, float s) {
        checkAndAddName(name);
        floats.put(name, s);
        this.size += 4 + 1;
    }

    /**
     * Adds the given double to this BDSv2 with the given name.
     *
     * @param name The name to give the double.
     * @param s    The double to store.
     */
    public void addDouble(String name, double s) {
        checkAndAddName(name);
        doubles.put(name, s);
        this.size += 8 + 1;
    }

    /**
     * Adds the given string to this BDSv2 with the given name.
     *
     * @param name The name to give the string.
     * @param s    The string to store.
     */
    public void addString(String name, String s) {
        try {
            checkAndAddName(name);
            byte[] bytes = s.getBytes("UTF-8");
            strings.put(name, Set2.of(s, bytes));
            this.size += bytes.length + 4 + 1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds the given BDSv2 as child to this BDSv2 with the given name.
     *
     * @param name The name to give the BDSv2.
     * @param s    The BDSv2 to store.
     */
    public void addBDS(String name, BDSv2 s) {
        checkAndAddName(name);
        bdss.put(name, s);
        this.size += 1 + 4; // Size is calculated each time in getSize(), since BDSs are mutable
    }
    // endregion

    // region ... Array adders ...

    /**
     * Adds the given byte array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the byte array.
     * @param s    The byte array to store.
     */
    public void addBytes(String name, byte[] s) {
        checkAndAddName(name);
        byteArrays.put(name, s);
        this.size += s.length + 1 + 4;
    }

    /**
     * Adds the given short array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the short array.
     * @param s    The short array to store.
     */
    public void addShorts(String name, short[] s) {
        checkAndAddName(name);
        shortArrays.put(name, s);
        this.size += 2 * s.length + 1 + 4;
    }

    /**
     * Adds the given char array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the char array.
     * @param s    The char array to store.
     */
    public void addChars(String name, char[] s) {
        checkAndAddName(name);
        charArrays.put(name, s);
        this.size += 2 * s.length + 1 + 4;
    }

    /**
     * Adds the given int array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the int array.
     * @param s    The int array to store.
     */
    public void addInts(String name, int[] s) {
        checkAndAddName(name);
        intArrays.put(name, s);
        this.size += 4 * s.length + 1 + 4;
    }

    /**
     * Adds the given long array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the long array.
     * @param s    The long array to store.
     */
    public void addLongs(String name, long[] s) {
        checkAndAddName(name);
        longArrays.put(name, s);
        this.size += 8 * s.length + 1 + 4;
    }

    /**
     * Adds the given float array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the float array.
     * @param s    The float array to store.
     */
    public void addFloats(String name, float[] s) {
        checkAndAddName(name);
        floatArrays.put(name, s);
        this.size += 4 * s.length + 1 + 4;
    }

    /**
     * Adds the given double array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the double array.
     * @param s    The double array to store.
     */
    public void addDoubles(String name, double[] s) {
        checkAndAddName(name);
        doubleArrays.put(name, s);
        this.size += 8 * s.length + 1 + 4;
    }

    /**
     * Adds the given String array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the String array.
     * @param s    The String array to store.
     */
    public void addStrings(String name, String[] s) {
        checkAndAddName(name);
        Set2<String, byte[]>[] array = new Set2[s.length];
        for (int i = 0; i < s.length; i++) {
            try {
                String ss = s[i];
                byte[] bytes = ss.getBytes("UTF-8");
                array[i] = Set2.of(ss, bytes);
                this.size += bytes.length + 4;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        stringArrays.put(name, array);
        this.size += 1 + 4;
    }

    /**
     * Adds the given BDSv2 array as child to this BDSv2 with the given name.
     *
     * @param name The name to give the BDSv2 array.
     * @param s    The BDSv2 array to store.
     */
    public void addBDSs(String name, BDSv2[] s) {
        checkAndAddName(name);
        bdsArrays.put(name, s);
        this.size += 1 + 4 + s.length * 4; // Size is calculated each time in getSize(), since BDSs are mutable
    }
    // endregion

    // endregion

    // region ... Getters ...

    // region ... Normal getters ...

    /**
     * Gets the byte with the given name from this BDSv2.
     *
     * @param name The name of the byte.
     * @return the byte with the given name.
     */
    public Byte getByte(String name) {
        return this.bytes.get(name);
    }

    /**
     * Gets the char with the given name from this BDSv2.
     *
     * @param name The name of the char.
     * @return the char with the given name.
     */
    public Character getChar(String name) {
        return this.chars.get(name);
    }

    /**
     * Gets the short with the given name from this BDSv2.
     *
     * @param name The name of the short.
     * @return the short with the given name.
     */
    public Short getShort(String name) {
        return this.shorts.get(name);
    }

    /**
     * Gets the int with the given name from this BDSv2.
     *
     * @param name The name of the int.
     * @return the int with the given name.
     */
    public Integer getInt(String name) {
        return this.ints.get(name);
    }

    /**
     * Gets the long with the given name from this BDSv2.
     *
     * @param name The name of the long.
     * @return the long with the given name.
     */
    public Long getLong(String name) {
        return this.longs.get(name);
    }

    /**
     * Gets the float with the given name from this BDSv2.
     *
     * @param name The name of the float.
     * @return the float with the given name.
     */
    public Float getFloat(String name) {
        return this.floats.get(name);
    }

    /**
     * Gets the double with the given name from this BDSv2.
     *
     * @param name The name of the double.
     * @return the double with the given name.
     */
    public Double getDouble(String name) {
        return this.doubles.get(name);
    }

    /**
     * Gets the String with the given name from this BDSv2.
     *
     * @param name The name of the String.
     * @return the String with the given name.
     */
    public String getString(String name) {
        return this.strings.get(name).getT();
    }

    /**
     * Gets the BDSv2 with the given name from this BDSv2.
     *
     * @param name The name of the BDSv2.
     * @return the BDSv2 with the given name.
     */
    public BDSv2 getBDS(String name) {
        return this.bdss.get(name);
    }

    // endregion

    // region ... Array getters ...

    /**
     * Gets the byte array with the given name from this BDSv2.
     *
     * @param name The name of the byte array.
     * @return the byte array with the given name.
     */
    public byte[] getBytes(String name) {
        return this.byteArrays.get(name);
    }

    /**
     * Gets the char array with the given name from this BDSv2.
     *
     * @param name The name of the char array.
     * @return the char array with the given name.
     */
    public char[] getChars(String name) {
        return this.charArrays.get(name);
    }

    /**
     * Gets the short array with the given name from this BDSv2.
     *
     * @param name The name of the short array.
     * @return the short array with the given name.
     */
    public short[] getShorts(String name) {
        return this.shortArrays.get(name);
    }

    /**
     * Gets the int array with the given name from this BDSv2.
     *
     * @param name The name of the int array.
     * @return the int array with the given name.
     */
    public int[] getInts(String name) {
        return this.intArrays.get(name);
    }

    /**
     * Gets the long array with the given name from this BDSv2.
     *
     * @param name The name of the long array.
     * @return the long array with the given name.
     */
    public long[] getLongs(String name) {
        return this.longArrays.get(name);
    }

    /**
     * Gets the float array with the given name from this BDSv2.
     *
     * @param name The name of the float array.
     * @return the float array with the given name.
     */
    public float[] getFloats(String name) {
        return this.floatArrays.get(name);
    }

    /**
     * Gets the double array with the given name from this BDSv2.
     *
     * @param name The name of the double array.
     * @return the double array with the given name.
     */
    public double[] getDoubles(String name) {
        return this.doubleArrays.get(name);
    }

    /**
     * Gets the String array with the given name from this BDSv2.
     *
     * @param name The name of the String array.
     * @return the String array with the given name.
     */
    public String[] getStrings(String name) {
        Set2<String, byte[]>[] strs = this.stringArrays.get(name);
        if (strs == null) return null;
        String[] strings = new String[strs.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = strs[i].getT();
        }
        return strings;
    }

    /**
     * Gets the BDSv2 array with the given name from this BDSv2.
     *
     * @param name The name of the BDSv2 array.
     * @return the BDSv2 array with the given name.
     */
    public BDSv2[] getBDSs(String name) {
        return this.bdsArrays.get(name);
    }

    // endregion

    // endregion

    // region ... Writers ...

    /**
     * Writes this BDSv2 to a byte array.
     *
     * @return A byte array containing this BDSv2, serialized.
     */
    public byte[] write() {
        byte[] data = new byte[this.getSize()];
        try {
            writeToStream(new BAOS(data));
        } catch (IOException ignored) {
            throw new AssertionError("BAOS cannot throw IOException");
        }
        return data;
    }

    /**
     * Writes this BDSv2 to an OutputStream.
     */
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
        for (Map.Entry<String, Set2<String, byte[]>> e : this.strings.entrySet()) {
            writeByte(os, BDSv2Type.STRING.getSignature(false));
            writeString(os, e.getKey());
            writeString(os, e.getValue().getU());
        }
        for (Map.Entry<String, BDSv2> e : this.bdss.entrySet()) {
            writeByte(os, BDSv2Type.BDS.getSignature(false));
            writeString(os, e.getKey());
            e.getValue().writeInternal(os);
        }

        // endregion
        // region ... Array types ...

        for (Map.Entry<String, byte[]> e : this.byteArrays.entrySet()) {
            writeByte(os, BDSv2Type.BYTE.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (byte b : e.getValue()) writeByte(os, b);
        }

        for (Map.Entry<String, short[]> e : this.shortArrays.entrySet()) {
            writeByte(os, BDSv2Type.SHORT.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (short b : e.getValue()) writeShort(os, b);
        }
        for (Map.Entry<String, char[]> e : this.charArrays.entrySet()) {
            writeByte(os, BDSv2Type.CHAR.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (char b : e.getValue()) writeChar(os, b);
        }
        for (Map.Entry<String, int[]> e : this.intArrays.entrySet()) {
            writeByte(os, BDSv2Type.INT.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (int b : e.getValue()) writeInt(os, b);
        }
        for (Map.Entry<String, long[]> e : this.longArrays.entrySet()) {
            writeByte(os, BDSv2Type.LONG.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (long b : e.getValue()) writeLong(os, b);
        }
        for (Map.Entry<String, float[]> e : this.floatArrays.entrySet()) {
            writeByte(os, BDSv2Type.FLOAT.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (float b : e.getValue()) writeFloat(os, b);
        }
        for (Map.Entry<String, double[]> e : this.doubleArrays.entrySet()) {
            writeByte(os, BDSv2Type.DOUBLE.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (double b : e.getValue()) writeDouble(os, b);
        }
        for (Map.Entry<String, Set2<String, byte[]>[]> e : this.stringArrays.entrySet()) {
            writeByte(os, BDSv2Type.STRING.getSignature(true));
            writeString(os, e.getKey());
            writeInt(os, e.getValue().length);
            for (Set2<String, byte[]> b : e.getValue()) writeString(os, b.getU());
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
        writeString(os, s.getBytes("UTF-8"));
    }

    private static void writeString(OutputStream os, byte[] data) throws IOException {
        writeInt(os, data.length);
        os.write(data);
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

    /**
     * Parses a BDSv2 from the beginning of the given byte array.
     *
     * @param data The array from which to parse.
     * @return The parsed BDSv2.
     * @throws SerializationException If there is an error deserializing.
     */
    public static BDSv2 parse(byte[] data) throws SerializationException {
        return BDSv2.parse(data, 0);
    }

    /**
     * Parses a BDSv2 from the beginning of the given byte array.
     *
     * @param data   The array from which to parse.
     * @param offset The index of the array from which to start parsing the BDSv2.
     * @return The parsed BDSv2.
     * @throws SerializationException If there is an error deserializing.
     */
    public static BDSv2 parse(byte[] data, int offset) throws SerializationException {
        try {
            for (byte b : SIGNATURE) {
                if (data[offset++] != b)
                    throw new SerializationException("BDSv2 signature not present, or incorrect!");
            }
            return parseInternal(data, new int[]{offset});
        } catch (IOException e) {
            throw new AssertionError("BAIS never throws an IO Exception");
        }
    }

    /**
     * Parses a BDSv2 from an InputStream.
     *
     * @param is The InputStream to parse from.
     * @return The parsed BDSv2.
     * @throws SerializationException If there is an error deserializing.
     */
    public static BDSv2 parseStream(InputStream is) throws IOException, SerializationException {
        for (byte b : SIGNATURE)
            if ((byte) is.read() != b) throw new SerializationException("BDSv2 signature not present, or incorrect!");
        int length = 0;
        for (int i = 0; i < 4; i++) {
            final byte read = (byte) (is.read() & 0xFF);
            length = (length << 8) | read & 0xFF;
        }
        byte[] data = new byte[length + 4];

        data[0] = (byte) ((length & 0xFF000000) >> 24);
        data[1] = (byte) ((length & 0xFF0000) >> 16);
        data[2] = (byte) ((length & 0xFF00) >> 8);
        data[3] = (byte) (length & 0xFF);

        for (int i = 0; i < length; i++) {
            data[i + 4] = (byte) is.read();
        }

        return parseInternal(data, new int[]{0});
    }

    private static BDSv2 parseInternal(byte[] is, int[] offset) throws IOException, SerializationException {
        BDSv2 bds = new BDSv2();
        int initialOffset = offset[0];
        int length = parseInt(is, offset);

        while (initialOffset + length > offset[0]) {
            byte signature = is[offset[0]++];
            String name = parseString(is, offset);
            BDSv2Type type = BDSv2Type.fromSignature(signature);
            if (type == null)
                throw new SerializationException("Unknown type signature: " + Integer.toHexString(signature & 0xFF));
            switch (type) {
                case BYTE:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        byte[] array = new byte[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseByte(is, offset);
                        }
                        bds.addBytes(name, array);
                    } else {
                        bds.addByte(name, parseByte(is, offset));
                    }
                    break;
                case CHAR:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        char[] array = new char[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseChar(is, offset);
                        }
                        bds.addChars(name, array);
                    } else {
                        bds.addChar(name, parseChar(is, offset));
                    }
                    break;
                case SHORT:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        short[] array = new short[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseShort(is, offset);
                        }
                        bds.addShorts(name, array);
                    } else {
                        bds.addShort(name, parseShort(is, offset));
                    }
                    break;
                case INT:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        int[] array = new int[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseInt(is, offset);
                        }
                        bds.addInts(name, array);
                    } else {
                        bds.addInt(name, parseInt(is, offset));
                    }
                    break;
                case LONG:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        long[] array = new long[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseLong(is, offset);
                        }
                        bds.addLongs(name, array);
                    } else {
                        bds.addLong(name, parseLong(is, offset));
                    }
                    break;
                case FLOAT:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        float[] array = new float[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseFloat(is, offset);
                        }
                        bds.addFloats(name, array);
                    } else {
                        bds.addFloat(name, parseFloat(is, offset));
                    }
                    break;
                case DOUBLE:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        double[] array = new double[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseDouble(is, offset);
                        }
                        bds.addDoubles(name, array);
                    } else {
                        bds.addDouble(name, parseDouble(is, offset));
                    }
                    break;
                case BDS:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        BDSv2[] array = new BDSv2[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseInternal(is, offset);
                        }
                        bds.addBDSs(name, array);
                    } else {
                        bds.addBDS(name, BDSv2.parseInternal(is, offset));
                    }
                    break;
                case STRING:
                    if (BDSv2Type.isArray(signature)) {
                        int arrayLength = parseInt(is, offset);
                        String[] array = new String[arrayLength];
                        for (int i = 0; i < arrayLength; i++) {
                            array[i] = parseString(is, offset);
                        }
                        bds.addStrings(name, array);
                    } else {
                        bds.addString(name, parseString(is, offset));
                    }
                    break;
            }
        }
        return bds;
    }

    // region ... Internal parsers ...

    private static byte parseByte(byte[] is, int[] offset) throws IOException {
        return is[offset[0]++];
    }

    private static char parseChar(byte[] is, int[] offset) throws IOException {
        return (char) (is[offset[0]++] << 8 | (is[offset[0]++] & 0xFF));
    }

    private static short parseShort(byte[] is, int[] offset) throws IOException {
        return (short) (is[offset[0]++] << 8 | (is[offset[0]++] & 0xFF));
    }

    private static int parseInt(byte[] is, int[] offset) throws IOException {
        return is[offset[0]++] << 24 | (is[offset[0]++] & 0xFF) << 16 | (is[offset[0]++] & 0xFF) << 8 | (is[offset[0]++] & 0xFF);
    }

    private static long parseLong(byte[] is, int[] offset) throws IOException {
        return ((long) is[offset[0]++]) << 56L | (is[offset[0]++] & 0xFFL) << 48L | (is[offset[0]++] & 0xFFL) << 40L | (is[offset[0]++] & 0xFFL) << 32 |
                (is[offset[0]++] & 0xFFL) << 24 | (is[offset[0]++] & 0xFFL) << 16 | (is[offset[0]++] & 0xFFL) << 8 | (is[offset[0]++] & 0xFFL);
    }

    private static float parseFloat(byte[] is, int[] offset) throws IOException {
        return Float.intBitsToFloat(parseInt(is, offset));
    }

    private static double parseDouble(byte[] is, int[] offset) throws IOException {
        return Double.longBitsToDouble(parseLong(is, offset));
    }

    private static String parseString(byte[] is, int[] offset) throws IOException, SerializationException {
        int length = parseInt(is, offset);
        String s = new String(is, offset[0], length, StandardCharsets.UTF_8);
        offset[0] += length;
        return s;
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
