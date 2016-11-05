package io.github.cubedtear.jcubit.bds;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.primitives.*;
import io.github.cubedtear.jcubit.collections.ArrayUtil;
import io.github.cubedtear.jcubit.util.NotNull;
import io.github.cubedtear.jcubit.util.Nullable;
import io.github.cubedtear.jcubit.util.Set2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * Binary Data Storage
 * Used to store different data types in byte arrays,
 * write them somewhere (file, DB, network, etc.), and then read them back.
 *
 * @author Aritz Lopez
 */
@SuppressWarnings("unused")
public class BDS {

    private final static byte[] SIGNATURE = ".BDS\r\n".getBytes(StandardCharsets.UTF_8);
    private final static byte[] NEW_LINE = "\r\n".getBytes(StandardCharsets.UTF_8);

    private String name;
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

    /**
     * Creates an empty BDS with the specified name.
     * @param name The name of the BDS to create.
     */
    public BDS(String name) {
        this.name = name;
    }

    /**
     * Reads a BDS from the specified file.
     * @param f The file from which the BDS will be read.
     * @return The BDS read from the file.
     * @throws IOException if an I/O error occurs. See {@link com.google.common.io.Files#toByteArray(File)}
     * @see BDS#load(byte[])
     */
    public static BDS loadFromFile(File f) throws IOException {
        return BDS.load(com.google.common.io.Files.toByteArray(f));
    }

    /**
     * Reads a BDS from the specified Path
     *
     * @param p The path from which the BDS will be read.
     * @return The BDS read from the Path.
     * @throws IOException if an I/O error occurs. See {@link java.nio.file.Files#readAllBytes(Path)}
     * @see BDS#load(byte[])
     */
    public static BDS loadFromFile(Path p) throws IOException {
        return BDS.load(java.nio.file.Files.readAllBytes(p));
    }

    /**
     * Reads a BDS from the given binary data.
     *
     * @param data The data from which the BDS should be parsed.
     * @return The BDS read from the given data.
     */
    public static BDS load(byte[] data) {
        if (data.length < SIGNATURE.length + 2)
            throw new IllegalArgumentException("Data is too short, it is not in the correct format!");

        if (!ArrayUtil.equals(data, SIGNATURE, SIGNATURE.length))
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        return parseBDS(data, SIGNATURE.length).getT();
    }

    @NotNull
    private static Set2<BDS, Integer> parseBDS(byte[] data, int offset) {
        if (data[offset] != BDSType.BDS.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        BDS bds = new BDS("");

        Set2<String, Integer> name = parseString(data, offset + 1);
        bds.setName(name.getT());

        int i = name.getU();

        A:
        while (i < data.length) {
            switch (BDSType.fromSignature(data[i])) {
                case BYTE:
                    i = bds.parseByte(data, i);
                    break;
                case CHAR:
                    i = bds.parseChar(data, i);
                    break;
                case SHORT:
                    i = bds.parseShort(data, i);
                    break;
                case INT:
                    i = bds.parseInt(data, i);
                    break;
                case LONG:
                    i = bds.parseLong(data, i);
                    break;
                case FLOAT:
                    i = bds.parseFloat(data, i);
                    break;
                case DOUBLE:
                    i = bds.parseDouble(data, i);
                    break;
                case STRING:
                    i = bds.parseBDSString(data, i);
                    break;
                case BDS:
                    Set2<BDS, Integer> bds1 = parseBDS(data, i);
                    bds.addBDS(bds1.getT());
                    i = bds1.getU();
                    break;
                case LIST:
                    switch (BDSType.fromSignature(data[i + 1])) {
                        case BYTE:
                            i = bds.parseByteArray(data, i);
                            break;
                        case CHAR:
                            i = bds.parseCharArray(data, i);
                            break;
                        case SHORT:
                            i = bds.parseShortArray(data, i);
                            break;
                        case INT:
                            i = bds.parseIntArray(data, i);
                            break;
                        case LONG:
                            i = bds.parseLongArray(data, i);
                            break;
                        case FLOAT:
                            i = bds.parseFloatArray(data, i);
                            break;
                        case DOUBLE:
                            i = bds.parseDoubleArray(data, i);
                            break;
                        case STRING:
                            i = bds.parseStringArray(data, i);
                            break;
                        case BDS:
                            i = bds.parseBDSArray(data, i);
                            break;
                        case LIST:
                            throw new IllegalArgumentException("Nested lists are not allowed!");
                        default:
                            throw new IllegalArgumentException("Given data is not in the appropriate format!");
                    }
                    break;
                case END:
                    break A;
                default:
                    throw new IllegalArgumentException("Unknown data type found, with byte: " + data[i]);
            }
        }
        return Set2.of(bds, i + 1);
    }

    private static Set2<String, Integer> parseString(byte[] data, int offset) {
        int length = Ints.fromBytes(data[offset++], data[offset++], data[offset++], data[offset++]);
        if (length == 0) return Set2.of("", offset);
        return Set2.of(new String(data, offset, length, StandardCharsets.UTF_8), offset + length);
    }

    private static void writeString(String nameStr, ByteArrayOutputStream baos) {
        byte[] name = nameStr.getBytes(StandardCharsets.UTF_8);
        for (byte b : Ints.toByteArray(name.length)) baos.write(b);
        for (byte b : name) baos.write(b);
    }

    private static short getShort(byte[] data, int offset) {
        return Shorts.fromBytes(data[offset++], data[offset]);
    }

    private static char getChar(byte[] data, int offset) {
        return Chars.fromBytes(data[offset++], data[offset]);
    }

    private static int getInt(byte[] data, int offset) {
        return Ints.fromBytes(data[offset++], data[offset++], data[offset++], data[offset]);
    }

    private static long getLong(byte[] data, int offset) {
        byte[] longData = new byte[8];
        System.arraycopy(data, offset, longData, 0, 8);
        return Longs.fromByteArray(longData);
    }

    private static void writeShort(short s, ByteArrayOutputStream baos) {
        baos.write((byte) (s >> 8));
        baos.write((byte) s);
    }


    private static void writeChar(char c, ByteArrayOutputStream baos) {
        baos.write((byte) (c >> 8));
        baos.write((byte) c);
    }

    private static void writeInt(int i, ByteArrayOutputStream baos) {
        for (byte b : Ints.toByteArray(i)) baos.write(b);
    }

    private static void writeLong(long l, ByteArrayOutputStream baos) {
        for (byte b : Longs.toByteArray(l)) baos.write(b);
    }

    private static void writeFloat(float f, ByteArrayOutputStream baos) {
        for (byte b : Ints.toByteArray(Float.floatToIntBits(f))) baos.write(b);
    }

    private static void writeDouble(double d, ByteArrayOutputStream baos) {
        for (byte b : Longs.toByteArray(Double.doubleToLongBits(d))) baos.write(b);
    }

    private int parseByte(byte[] data, int offset) {
        if (data[offset] != BDSType.BYTE.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 1);
        byte v = data[name.getU()];
        this.addByte(name.getT(), v);
        return name.getU() + 1;
    }

    private int parseChar(byte[] data, int offset) {
        if (data[offset] != BDSType.CHAR.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 1);
        offset = name.getU();
        char v = getChar(data, offset);
        this.addChar(name.getT(), v);
        return offset + Chars.BYTES;
    }

    private int parseShort(byte[] data, int offset) {
        if (data[offset] != BDSType.SHORT.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 1);
        offset = name.getU();
        short v = getShort(data, offset);
        this.addShort(name.getT(), v);
        return offset + Shorts.BYTES;
    }

    private int parseInt(byte[] data, int offset) {
        if (data[offset] != BDSType.INT.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 1);
        offset = name.getU();
        int v = getInt(data, offset);
        this.addInt(name.getT(), v);
        return offset + Ints.BYTES;
    }

    private int parseLong(byte[] data, int offset) {
        if (data[offset] != BDSType.LONG.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 1);
        offset = name.getU();
        this.addLong(name.getT(), getLong(data, offset));
        return offset + Longs.BYTES;
    }

    private int parseFloat(byte[] data, int offset) {
        if (data[offset] != BDSType.FLOAT.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 1);
        offset = name.getU();
        float f = Float.intBitsToFloat(Ints.fromBytes(data[offset++], data[offset++], data[offset++], data[offset++]));
        this.addFloat(name.getT(), f);
        return offset;
    }

    private int parseDouble(byte[] data, int offset) {
        if (data[offset] != BDSType.DOUBLE.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 1);
        offset = name.getU();

        byte[] longData = new byte[8];
        System.arraycopy(data, offset, longData, 0, 8);
        long l = Longs.fromByteArray(longData);
        double d = Double.longBitsToDouble(l);
        this.addDouble(name.getT(), d);
        return offset + Doubles.BYTES;
    }

    private int parseBDSString(byte[] data, int offset) {
        if (data[offset] != BDSType.STRING.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 1);
        Set2<String, Integer> str = parseString(data, name.getU());
        this.addString(name.getT(), str.getT());
        return str.getU();
    }

    private int parseByteArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.BYTE.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        byte[] byteArray = new byte[length];
        offset = name.getU() + Ints.BYTES;
        System.arraycopy(data, offset, byteArray, 0, length);
        this.addBytes(name.getT(), byteArray);
        return offset + length;
    }

    private int parseCharArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.CHAR.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        char[] charArray = new char[length];
        offset = name.getU() + Ints.BYTES;
        for (int i = 0; i < length; i++) {
            charArray[i] = getChar(data, offset + i * Chars.BYTES);
        }
        this.addChars(name.getT(), charArray);
        return offset + length * Chars.BYTES;
    }

    private int parseShortArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.SHORT.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        short[] shortArray = new short[length];
        offset = name.getU() + Ints.BYTES;
        for (int i = 0; i < length; i++) {
            shortArray[i] = getShort(data, offset + i * Shorts.BYTES);
        }
        this.addShorts(name.getT(), shortArray);
        return offset + length * Shorts.BYTES;
    }

    private int parseIntArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.INT.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        int[] intArray = new int[length];
        offset = name.getU() + Ints.BYTES;
        for (int i = 0; i < length; i++) {
            intArray[i] = getInt(data, offset + i * Ints.BYTES);
        }
        this.addInts(name.getT(), intArray);
        return offset + length * Ints.BYTES;
    }

    private int parseLongArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.LONG.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        long[] longArray = new long[length];
        offset = name.getU() + Ints.BYTES;
        for (int i = 0; i < length; i++) {
            longArray[i] = getLong(data, offset + i * Longs.BYTES);
        }
        this.addLongs(name.getT(), longArray);
        return offset + length * Longs.BYTES;
    }

    private int parseFloatArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.FLOAT.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        float[] floatArray = new float[length];
        offset = name.getU() + Ints.BYTES;
        for (int i = 0; i < length; i++) {
            floatArray[i] = Float.intBitsToFloat(getInt(data, offset + i * Ints.BYTES));
        }
        this.addFloats(name.getT(), floatArray);
        return offset + length * Floats.BYTES;
    }

    private int parseDoubleArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.DOUBLE.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        double[] doubleArray = new double[length];
        offset = name.getU() + Ints.BYTES;
        for (int i = 0; i < length; i++) {
            doubleArray[i] = Double.longBitsToDouble(getLong(data, offset + i * Longs.BYTES));
        }
        this.addDoubles(name.getT(), doubleArray);
        return offset + length * Doubles.BYTES;
    }

    private int parseStringArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.STRING.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        String[] doubleArray = new String[length];
        offset = name.getU() + Ints.BYTES;
        for (int i = 0; i < length; i++) {
            Set2<String, Integer> str = parseString(data, offset);
            doubleArray[i] = str.getT();
            offset = str.getU();
        }
        this.addStrings(name.getT(), doubleArray);
        return offset;
    }

    private int parseBDSArray(byte[] data, int offset) {
        if (data[offset] != BDSType.LIST.signature || data[offset + 1] != BDSType.BDS.signature)
            throw new IllegalArgumentException("Given data is not in the appropriate format!");

        Set2<String, Integer> name = parseString(data, offset + 2);
        int length = getInt(data, name.getU());
        BDS[] bdsArray = new BDS[length];
        offset = name.getU() + Ints.BYTES;
        for (int i = 0; i < length; i++) {
            Set2<BDS, Integer> bds = parseBDS(data, offset);
            bdsArray[i] = bds.getT();
            offset = bds.getU();
        }
        this.addBDSs(name.getT(), bdsArray);
        return offset;
    }

    /**
     * Writes the BDS to the given file.
     *
     * @param f The file to write the BDS to.
     * @see BDS#loadFromFile(File)
     */
    public void writeToFile(File f) {
        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(write());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the name of this BDS.
     *
     * @return the name of this BDS.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this BDS.
     *
     * @param name The name.
     */
    public void setName(String name) {
        this.name = name;
    }

    private byte[] writeInternal() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        baos.write(BDSType.BDS.signature);

        writeString(this.name, baos);

        for (Map.Entry<String, Byte> bite : bytes.entrySet()) {
            baos.write(BDSType.BYTE.signature);
            writeString(bite.getKey(), baos);
            baos.write(bite.getValue());
        }

        for (Map.Entry<String, Character> shar : chars.entrySet()) {
            baos.write(BDSType.CHAR.signature);
            writeString(shar.getKey(), baos);
            writeChar(shar.getValue(), baos);
        }


        for (Map.Entry<String, Short> sort : shorts.entrySet()) {
            baos.write(BDSType.SHORT.signature);
            writeString(sort.getKey(), baos);
            writeShort(sort.getValue(), baos);
        }

        for (Map.Entry<String, Integer> integer : ints.entrySet()) {
            baos.write(BDSType.INT.signature);
            writeString(integer.getKey(), baos);
            writeInt(integer.getValue(), baos);
        }

        for (Map.Entry<String, Long> loong : longs.entrySet()) {
            baos.write(BDSType.LONG.signature);
            writeString(loong.getKey(), baos);
            writeLong(loong.getValue(), baos);
        }

        for (Map.Entry<String, Float> flot : floats.entrySet()) {
            baos.write(BDSType.FLOAT.signature);
            writeString(flot.getKey(), baos);
            writeFloat(flot.getValue(), baos);
        }

        for (Map.Entry<String, Double> doble : doubles.entrySet()) {
            baos.write(BDSType.DOUBLE.signature);
            writeString(doble.getKey(), baos);
            writeDouble(doble.getValue(), baos);

        }

        for (Map.Entry<String, String> str : strings.entrySet()) {
            baos.write(BDSType.STRING.signature);
            writeString(str.getKey(), baos);
            writeString(str.getValue(), baos);
        }

        for (Map.Entry<String, BDS> bds : bdss.entrySet()) {
            for (byte b : bds.getValue().writeInternal()) baos.write(b);
        }

        for (Map.Entry<String, Byte[]> byteArray : byteArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.BYTE.signature);
            writeString(byteArray.getKey(), baos);
            writeInt(byteArray.getValue().length, baos);
            for (Byte b : byteArray.getValue()) baos.write(b);
        }

        for (Map.Entry<String, Character[]> charArray : charArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.CHAR.signature);
            writeString(charArray.getKey(), baos);
            writeInt(charArray.getValue().length, baos);
            for (Character c : charArray.getValue()) writeChar(c, baos);
        }

        for (Map.Entry<String, Short[]> shortArray : shortArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.SHORT.signature);
            writeString(shortArray.getKey(), baos);
            writeInt(shortArray.getValue().length, baos);
            for (Short s : shortArray.getValue()) writeShort(s, baos);
        }

        for (Map.Entry<String, Integer[]> intArray : intArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.INT.signature);
            writeString(intArray.getKey(), baos);
            writeInt(intArray.getValue().length, baos);
            for (Integer i : intArray.getValue()) writeInt(i, baos);
        }

        for (Map.Entry<String, Long[]> longArray : longArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.LONG.signature);
            writeString(longArray.getKey(), baos);
            writeInt(longArray.getValue().length, baos);
            for (Long l : longArray.getValue()) writeLong(l, baos);
        }

        for (Map.Entry<String, Float[]> floatArray : floatArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.FLOAT.signature);
            writeString(floatArray.getKey(), baos);
            writeInt(floatArray.getValue().length, baos);
            for (Float f : floatArray.getValue()) writeFloat(f, baos);
        }

        for (Map.Entry<String, Double[]> doubleArray : doubleArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.DOUBLE.signature);
            writeString(doubleArray.getKey(), baos);
            writeInt(doubleArray.getValue().length, baos);
            for (Double d : doubleArray.getValue()) writeDouble(d, baos);
        }

        for (Map.Entry<String, String[]> stringArray : stringArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.STRING.signature);
            writeString(stringArray.getKey(), baos);
            writeInt(stringArray.getValue().length, baos);
            for (String s : stringArray.getValue()) writeString(s, baos);
        }

        for (Map.Entry<String, BDS[]> bdsArray : bdsArrays.entrySet()) {
            baos.write(BDSType.LIST.signature);
            baos.write(BDSType.BDS.signature);
            writeString(bdsArray.getKey(), baos);
            writeInt(bdsArray.getValue().length, baos);
            for (BDS b : bdsArray.getValue()) {
                byte[] data = b.writeInternal();
                baos.write(data, 0, data.length);
            }
        }

        baos.write(BDSType.END.signature);

        return baos.toByteArray();
    }

    /**
     * Returns the byte representation of this BDS. This is the standard method to store or send a BDS.
     * In order to store it to a file, it is recommended that {@link BDS#writeToFile(File)} is used.
     *
     * @return the byte representation of this BDS.
     * @see BDS#load(byte[])
     */
    public byte[] write() {
        byte[] data = writeInternal();
        byte[] newData = new byte[data.length + SIGNATURE.length + NEW_LINE.length];
        System.arraycopy(SIGNATURE, 0, newData, 0, SIGNATURE.length);
        System.arraycopy(data, 0, newData, SIGNATURE.length, data.length);
        System.arraycopy(NEW_LINE, 0, newData, SIGNATURE.length + data.length, NEW_LINE.length);
        return newData;
    }

    private boolean isTaken(String name) {
        return takenNames.contains(name);
    }

    private <T> boolean addElement(String name, @Nullable T value, Map<String, T> map) {
        if (value == null) throw new IllegalArgumentException("Cannot add null value");
        if (isTaken(name)) return false;
        takenNames.add(name);
        map.put(name, value);
        return true;
    }

    /**
     * Adds a string with the given name and value to this BDS.
     *
     * @param name  The name of the string.
     * @param value The value of the string. Must not be null.
     * @return whether the string was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addString(String name, @NotNull String value) {
        return addElement(name, value, strings);
    }

    /**
     * Adds a byte with the given name and value to this BDS.
     *
     * @param name  The name of the byte.
     * @param value The value of the byte.
     * @return whether the byte was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addByte(String name, byte value) {
        return addElement(name, value, bytes);
    }

    /**
     * Adds a char with the given name and value to this BDS.
     *
     * @param name  The name of the char.
     * @param value The value of the char.
     * @return whether the char was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addChar(String name, char value) {
        return addElement(name, value, chars);
    }

    /**
     * Adds a int with the given name and value to this BDS.
     *
     * @param name  The name of the int.
     * @param value The value of the int.
     * @return whether the int was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addInt(String name, int value) {
        return addElement(name, value, ints);
    }

    /**
     * Adds a short with the given name and value to this BDS.
     *
     * @param name  The name of the short.
     * @param value The value of the short.
     * @return whether the short was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addShort(String name, short value) {
        return addElement(name, value, shorts);
    }

    /**
     * Adds a long with the given name and value to this BDS.
     *
     * @param name  The name of the long.
     * @param value The value of the long.
     * @return whether the long was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addLong(String name, long value) {
        return addElement(name, value, longs);
    }

    /**
     * Adds a float with the given name and value to this BDS.
     *
     * @param name  The name of the float.
     * @param value The value of the float.
     * @return whether the float was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addFloat(String name, float value) {
        return addElement(name, value, floats);
    }

    /**
     * Adds a double with the given name and value to this BDS.
     *
     * @param name  The name of the double.
     * @param value The value of the double.
     * @return whether the double was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addDouble(String name, double value) {
        return addElement(name, value, doubles);
    }

    /**
     * Adds a nested BDS to this BDS. The name will be taken from the {@code value} parameter.
     *
     * @param value The BDS to add. Must not be null.
     * @return whether the BDS was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addBDS(@NotNull BDS value) {
        return addElement(value.getName(), value, bdss);
    }

    /**
     * Adds a string array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addStrings(String name, @NotNull String[] values) {
        return addElement(name, values, stringArrays);
    }

    /**
     * Adds a byte array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addBytes(String name, @NotNull byte[] values) {
        return addElement(name, ArrayUtil.box(values), byteArrays);
    }

    /**
     * Adds an int array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addInts(String name, @NotNull int[] values) {
        return addElement(name, ArrayUtil.box(values), intArrays);
    }

    /**
     * Adds a char array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addChars(String name, @NotNull char[] values) {
        return addElement(name, ArrayUtil.box(values), charArrays);
    }

    /**
     * Adds a short array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addShorts(String name, @NotNull short[] values) {
        return addElement(name, ArrayUtil.box(values), shortArrays);
    }

    /**
     * Adds a long array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addLongs(String name, @NotNull long[] values) {
        return addElement(name, ArrayUtil.box(values), longArrays);
    }

    /**
     * Adds a float array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addFloats(String name, @NotNull float[] values) {
        return addElement(name, ArrayUtil.box(values), floatArrays);
    }

    /**
     * Adds a double array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addDoubles(String name, @NotNull double[] values) {
        return addElement(name, ArrayUtil.box(values), doubleArrays);
    }

    /**
     * Adds a nested BDS array with the given name to this BDS.
     *
     * @param name   The name of the array.
     * @param values The array. Must not be null.
     * @return whether the array was added or not. In case it is false, it will be because the name has already been taken for this BDS.
     */
    public boolean addBDSs(String name, @NotNull BDS[] values) {
        return addElement(name, values, bdsArrays);
    }

    /**
     * Returns the byte with the given name.
     *
     * @param name The name of the byte.
     * @return the byte with the given name. {@code null} means there is no byte with the given name.
     */
    @Nullable
    public Byte getByte(String name) {
        return this.bytes.get(name);
    }

    /**
     * Returns the char with the given name.
     *
     * @param name The name of the char.
     * @return the char with the given name. {@code null} means there is no char with the given name.
     */
    @Nullable
    public Character getChar(String name) {
        return this.chars.get(name);
    }

    /**
     * Returns the short with the given name.
     *
     * @param name The name of the short.
     * @return the short with the given name. {@code null} means there is no short with the given name.
     */
    @Nullable
    public Short getShort(String name) {
        return this.shorts.get(name);
    }

    /**
     * Returns the int with the given name.
     *
     * @param name The name of the int.
     * @return the int with the given name. {@code null} means there is no int with the given name.
     */
    @Nullable
    public Integer getInt(String name) {
        return this.ints.get(name);
    }

    /**
     * Returns the long with the given name.
     *
     * @param name The name of the long.
     * @return the long with the given name. {@code null} means there is no long with the given name.
     */
    @Nullable
    public Long getLong(String name) {
        return this.longs.get(name);
    }

    /**
     * Returns the float with the given name.
     *
     * @param name The name of the float.
     * @return the float with the given name. {@code null} means there is no float with the given name.
     */
    @Nullable
    public Float getFloat(String name) {
        return this.floats.get(name);
    }

    /**
     * Returns the double with the given name.
     *
     * @param name The name of the double.
     * @return the double with the given name. {@code null} means there is no double with the given name.
     */
    @Nullable
    public Double getDouble(String name) {
        return this.doubles.get(name);
    }

    /**
     * Returns the String with the given name.
     *
     * @param name The name of the string.
     * @return the string with the given name. {@code null} means there is no String with the given name.
     */
    @Nullable
    public String getString(String name) {
        return this.strings.get(name);
    }

    /**
     * Returns the nested BDS with the given name.
     *
     * @param name The name of the nested BDS.
     * @return the nested BDS with the given name. {@code null} means there is no BDS with the given name.
     */
    @Nullable
    public BDS getBDS(String name) {
        return this.bdss.get(name);
    }

    /**
     * Returns the byte array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no byte array with the given name.
     */
    @Nullable
    public byte[] getByteArray(String name) {
        return ArrayUtil.unbox(this.byteArrays.get(name));
    }

    /**
     * Returns the char array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no char array with the given name.
     */
    @Nullable
    public char[] getCharArray(String name) {
        return ArrayUtil.unbox(this.charArrays.get(name));
    }

    /**
     * Returns the short array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no short array with the given name.
     */
    @Nullable
    public short[] getShortArray(String name) {
        return ArrayUtil.unbox(this.shortArrays.get(name));
    }

    /**
     * Returns the int array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no int array with the given name.
     */
    @Nullable
    public int[] getIntArray(String name) {
        return ArrayUtil.unbox(this.intArrays.get(name));
    }

    /**
     * Returns the long array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no long array with the given name.
     */
    @Nullable
    public long[] getLongArray(String name) {
        return ArrayUtil.unbox(this.longArrays.get(name));
    }

    /**
     * Returns the float array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no float array with the given name.
     */
    @Nullable
    public float[] getFloatArray(String name) {
        return ArrayUtil.unbox(this.floatArrays.get(name));
    }

    /**
     * Returns the double array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no double array with the given name.
     */
    @Nullable
    public double[] getDoubleArray(String name) {
        return ArrayUtil.unbox(this.doubleArrays.get(name));
    }

    /**
     * Returns the String array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no String array with the given name.
     */
    @Nullable
    public String[] getStringArray(String name) {
        return this.stringArrays.get(name);
    }

    /**
     * Returns the nested BDS array with the given name.
     *
     * @param name The name of the array.
     * @return the array with the given name. {@code null} means there is no BDS Array with the given name.
     */
    @Nullable
    public BDS[] getBDSArray(String name) {
        return this.bdsArrays.get(name);
    }

    /**
     * Returns the set of names of all the longs. That is, for all String {@code e} returned,
     * {@link BDS#getLong(String) this.getLong(e)} is guaranteed to not return null.
     * @return the set of names of all the longs.
     */
    public Set<String> getAllLongs() {
        return longs.keySet();
    }

    /**
     * Returns the set of names of all the longs. That is, for all String e returned,
     * {@link BDS#getBDSArray(String)} this.getBDSArray(e)} is guaranteed to not return null.
     * @return the set of names of all the longs.
     */
    public Set<String> getAllBDSArrays() {
        return bdsArrays.keySet();
    }

    /**
     * Returns the set of names of all the nested BDSs. That is, for all String e returned,
     * {@link BDS#getBDS(String)} this.getBDS(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the nested BDSs.
     */
    public Set<String> getAllBDSs() {
        return bdss.keySet();
    }

    /**
     * Returns the set of names of all the byte arrays. That is, for all String e returned,
     * {@link BDS#getByteArray(String) this.getByteArray(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the byte arrays.
     */
    public Set<String> getAllByteArrays() {
        return byteArrays.keySet();
    }

    /**
     * Returns the set of names of all the bytes. That is, for all String e returned,
     * {@link BDS#getByte(String) this.getByte(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the bytes.
     */
    public Set<String> getAllBytes() {
        return bytes.keySet();
    }

    /**
     * Returns the set of names of all the char arrays. That is, for all String e returned,
     * {@link BDS#getCharArray(String) this.getCharArray(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the char arrays.
     */
    public Set<String> getAllCharArrays() {
        return charArrays.keySet();
    }

    /**
     * Returns the set of names of all the chars. That is, for all String e returned,
     * {@link BDS#getChar(String) this.getChar(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the chars.
     */
    public Set<String> getAllChars() {
        return chars.keySet();
    }

    /**
     * Returns the set of names of all the longs. That is, for all String e returned,
     * {@link BDS#getDoubleArray(String)} this.getDoubleArray(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the longs.
     */
    public Set<String> getAllDoubleArrays() {
        return doubleArrays.keySet();
    }

    /**
     * Returns the set of names of all the doubles. That is, for all String e returned,
     * {@link BDS#getDouble(String) this.getDouble(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the doubles.
     */
    public Set<String> getAllDoubles() {
        return doubles.keySet();
    }

    /**
     * Returns the set of names of all the float arrays. That is, for all String e returned,
     * {@link BDS#getFloatArray(String) this.getFloatArray(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the float arrays.
     */
    public Set<String> getAllFloatArrays() {
        return floatArrays.keySet();
    }

    /**
     * Returns the set of names of all the floats. That is, for all String e returned,
     * {@link BDS#getFloat(String) this.getFloat(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the floats.
     */
    public Set<String> getAllFloats() {
        return floats.keySet();
    }

    /**
     * Returns the set of names of all the int arrays. That is, for all String e returned,
     * {@link BDS#getIntArray(String) this.getIntArray(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the int arrays.
     */
    public Set<String> getAllIntArrays() {
        return intArrays.keySet();
    }

    /**
     * Returns the set of names of all the ints. That is, for all String e returned,
     * {@link BDS#getInt(String) this.getInt(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the ints.
     */
    public Set<String> getAllInts() {
        return ints.keySet();
    }

    /**
     * Returns the set of names of all the long arrays. That is, for all String e returned,
     * {@link BDS#getLongArray(String) this.getLongArray(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the long arrays.
     */
    public Set<String> getAllLongArrays() {
        return longArrays.keySet();
    }

    /**
     * Returns the set of names of all the short arrays. That is, for all String e returned,
     * {@link BDS#getShortArray(String) this.getString(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the short arrays.
     */
    public Set<String> getAllShortArrays() {
        return shortArrays.keySet();
    }

    /**
     * Returns the set of names of all the shorts. That is, for all String e returned,
     * {@link BDS#getShort(String) this.getShort(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the shorts.
     */
    public Set<String> getAllShorts() {
        return shorts.keySet();
    }

    /**
     * Returns the set of names of all the string arrays. That is, for all String e returned,
     * {@link BDS#getStringArray(String) this.getStringArray(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the string arrays.
     */
    public Set<String> getAllStringArrays() {
        return stringArrays.keySet();
    }

    /**
     * Returns the set of names of all the strings. That is, for all String e returned,
     * {@link BDS#getString(String) this.getString(e)} is guaranteed to not return null.
     *
     * @return the set of names of all the strings.
     */
    public Set<String> getAllStrings() {
        return strings.keySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BDS bds = (BDS) o;

        return name.equals(bds.name) && strings.equals(bds.strings) && ints.equals(bds.ints) &&
                bytes.equals(bds.bytes) && chars.equals(bds.chars) && longs.equals(bds.longs) &&
                shorts.equals(bds.shorts) && floats.equals(bds.floats) && doubles.equals(bds.doubles) &&
                stringArrays.equals(bds.stringArrays) && intArrays.equals(bds.intArrays) &&
                byteArrays.equals(bds.byteArrays) && charArrays.equals(bds.charArrays) &&
                longArrays.equals(bds.longArrays) && shortArrays.equals(bds.shortArrays) &&
                floatArrays.equals(bds.floatArrays) && doubleArrays.equals(bds.doubleArrays) &&
                bdss.equals(bds.bdss) && bdsArrays.equals(bds.bdsArrays);

    }

    @Override
    public int hashCode() {
        return 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 *
                (31 * name.hashCode() + strings.hashCode()) + ints.hashCode()) + bytes.hashCode()) + chars.hashCode()) +
                longs.hashCode()) + shorts.hashCode()) + floats.hashCode()) + doubles.hashCode()) +
                stringArrays.hashCode()) + intArrays.hashCode()) + byteArrays.hashCode()) + charArrays.hashCode()) +
                longArrays.hashCode()) + shortArrays.hashCode()) + floatArrays.hashCode()) + doubleArrays.hashCode()) +
                bdss.hashCode()) + bdsArrays.hashCode();
    }

    private enum BDSType {
        BYTE((byte) 1),
        CHAR((byte) 11),
        SHORT((byte) 2),
        INT((byte) 3),
        LONG((byte) 4),
        FLOAT((byte) 5),
        DOUBLE((byte) 6),
        STRING((byte) 7),
        LIST((byte) 8),
        BDS((byte) 9),
        END((byte) 10),
        ERROR((byte) -1);

        private static final Map<Byte, BDSType> TYPE_MAP;

        static {
            Map<Byte, BDSType> types = Maps.newHashMap();
            for (BDSType t : BDSType.values()) {
                if (t != ERROR) types.put(t.signature, t);
            }
            TYPE_MAP = ImmutableMap.copyOf(types);
        }

        private final byte signature;

        BDSType(byte signature) {
            this.signature = signature;
        }

        private static BDSType fromSignature(byte signature) {
            return TYPE_MAP.containsKey(signature) ? TYPE_MAP.get(signature) : ERROR;
        }
    }
}
