package io.github.cubedtear.jcubit.bds;

import com.google.common.base.Strings;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.text.DecimalFormat;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Aritz Lopez
 */
@Category(BDSv2.class)
public class BDSv2Test {

    private final byte BYTE_VALUE = (byte) 26;
    private final short SHORT_VALUE = (short) 84;
    private final char CHAR_VALUE = '\u00fc';
    private final int NICE_INT_VALUE = -51000548;
    private final byte WORKS_BYTE_VALUE = (byte) 42;
    private final float FLOAT_VALUE = -0.5f;
    private final double DOUBLE_VALUE = -536.244;
    private final int INT_VALUE = Integer.MAX_VALUE / 2;
    private final long LONG_VALUE = Integer.MAX_VALUE * 52L;
    private final String STRING_VALUE = "h\u00aall\u00f2";
    private final byte[] BYTE_ARRAY = {Byte.MIN_VALUE, 0, Byte.MAX_VALUE};
    private final short[] SHORT_ARRAY = {Short.MIN_VALUE, 0, Short.MAX_VALUE};
    private final char[] CHAR_ARRAY = {Character.MIN_VALUE, Character.MAX_VALUE, Character.MAX_SURROGATE};
    private final int[] INT_ARRAY = {Integer.MIN_VALUE, 0, Integer.MAX_VALUE};
    private final long[] LONG_ARRAY = {Long.MIN_VALUE, 0, Long.MAX_VALUE};
    private final float[] FLOAT_ARRAY = {Float.MIN_VALUE, Float.MIN_NORMAL, 0, Float.MAX_VALUE};
    private final double[] DOUBLE_ARRAY = {Double.MIN_VALUE, Double.MIN_NORMAL, 0.0, Double.MAX_VALUE};
    private final String[] STRING_ARRAY = {"", " ", Strings.repeat("X", 10)};

    @Test
    public void testBDSv2() throws Exception {
        BDSv2 top = new BDSv2();
        top.addString("", "");
        BDSv2 nested0 = new BDSv2();

        nested0.addByte("byte", BYTE_VALUE);
        nested0.addShort("short", SHORT_VALUE);
        nested0.addChar("char", CHAR_VALUE);
        nested0.addInt("int", INT_VALUE);
        nested0.addLong("long", LONG_VALUE);
        nested0.addFloat("float", FLOAT_VALUE);
        nested0.addDouble("double", DOUBLE_VALUE);
        nested0.addString("string", STRING_VALUE);

        nested0.addBytes("bytes", BYTE_ARRAY);
        nested0.addShorts("shorts", SHORT_ARRAY);
        nested0.addChars("chars", CHAR_ARRAY);
        nested0.addInts("ints", INT_ARRAY);
        nested0.addLongs("longs", LONG_ARRAY);
        nested0.addFloats("floats", FLOAT_ARRAY);
        nested0.addDoubles("doubles", DOUBLE_ARRAY);
        nested0.addStrings("strings", STRING_ARRAY);

        BDSv2 nested1 = new BDSv2();
        nested1.addByte("works", WORKS_BYTE_VALUE);

        top.addBDSs("bds", new BDSv2[]{nested0, nested1});
        top.addInt("nice", NICE_INT_VALUE);

        byte[] serialized = top.write();

        BDSv2 parsedTop = BDSv2.parse(serialized);

        BDSv2 parsed0 = parsedTop.getBDSs("bds")[0];
        BDSv2 parsed1 = parsedTop.getBDSs("bds")[1];

        assertEquals(NICE_INT_VALUE, (int) parsedTop.getInt("nice"));
        assertEquals(BYTE_VALUE, (byte) parsed0.getByte("byte"));
        assertEquals(SHORT_VALUE, (short) parsed0.getShort("short"));
        assertEquals(CHAR_VALUE, (char) parsed0.getChar("char"));
        assertEquals(INT_VALUE, (int) parsed0.getInt("int"));
        assertEquals(LONG_VALUE, (long) parsed0.getLong("long"));
        // Delta is 0 because the number should be restored exactly the same
        assertEquals(FLOAT_VALUE, parsed0.getFloat("float"), 0);
        assertEquals(DOUBLE_VALUE, parsed0.getDouble("double"), 0);
        assertEquals(STRING_VALUE, parsed0.getString("string"));

        assertArrayEquals(BYTE_ARRAY, parsed0.getBytes("bytes"));
        assertArrayEquals(SHORT_ARRAY, parsed0.getShorts("shorts"));
        assertArrayEquals(CHAR_ARRAY, parsed0.getChars("chars"));
        assertArrayEquals(INT_ARRAY, parsed0.getInts("ints"));
        assertArrayEquals(LONG_ARRAY, parsed0.getLongs("longs"));
        assertArrayEquals(FLOAT_ARRAY, parsed0.getFloats("floats"), 0);
        assertArrayEquals(DOUBLE_ARRAY, parsed0.getDoubles("doubles"), 0);
        assertArrayEquals(STRING_ARRAY, parsed0.getStrings("strings"));

        assertEquals(WORKS_BYTE_VALUE, (byte) parsed1.getByte("works"));

    }
}
