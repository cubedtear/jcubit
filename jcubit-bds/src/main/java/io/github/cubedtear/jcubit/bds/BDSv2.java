package io.github.cubedtear.jcubit.bds;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
public class BDSv2 {

    private final static byte[] SIGNATURE = ".BDSv2\r\n".getBytes(StandardCharsets.UTF_8);
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

    public int getSize() {
        return size;
    }

    public byte[] write() {
        byte[] data = new byte[this.size];

        // TODO: Serialize everything

        return data;
    }

    // TODO: Create all getters and adders, and update the size after each addition
}
