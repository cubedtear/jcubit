package io.github.cubedtear.jcubit.bds;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;

/**
 * @author Aritz Lopez
 */
@Category(BDSv2.class)
public class BDSv2Test {
    @Test
    public void testBDSv2() throws Exception {
        BDSv2 top = new BDSv2();

        BDSv2 nested = new BDSv2();

        nested.addByte("byte", (byte) 26);
        nested.addShort("short", (short) 84);
        nested.addChar("char", '\u00fc');
        nested.addInt("int", Integer.MAX_VALUE / 2);
        nested.addLong("long", Integer.MAX_VALUE * 52L);
        nested.addFloat("float", -0.5f);
        nested.addDouble("double", -536.244);
        nested.addString("string", "h\u00aall\u00f2");

        top.addBDSs("bds", new BDSv2[]{nested, nested});

        byte[] serialized = top.write();

        BDSv2 parsedTop = BDSv2.parse(serialized);
        BDSv2 parsed = parsedTop.getBDSs("bds")[0];
        assertEquals((byte) 26, (byte) parsed.getByte("byte"));
        assertEquals((short) 84, (short) parsed.getShort("short"));
        assertEquals('\u00fc', (char) parsed.getChar("char"));
        assertEquals(Integer.MAX_VALUE / 2, (int) parsed.getInt("int"));
        assertEquals(Integer.MAX_VALUE * 52L, (long) parsed.getLong("long"));
        assertEquals(-0.5f, parsed.getFloat("float"), -0.5f / 10e3f);
        assertEquals(-536.244, parsed.getDouble("double"), -536.244 / 10e6);
        assertEquals("h\u00aall\u00f2", parsed.getString("string"));

        parsed = parsedTop.getBDSs("bds")[1];
        assertEquals((byte) 26, (byte) parsed.getByte("byte"));
        assertEquals((short) 84, (short) parsed.getShort("short"));
        assertEquals('\u00fc', (char) parsed.getChar("char"));
        assertEquals(Integer.MAX_VALUE / 2, (int) parsed.getInt("int"));
        assertEquals(Integer.MAX_VALUE * 52L, (long) parsed.getLong("long"));
        assertEquals(-0.5f, parsed.getFloat("float"), -0.5f / 10e3f);
        assertEquals(-536.244, parsed.getDouble("double"), -536.244 / 10e6);
        assertEquals("h\u00aall\u00f2", parsed.getString("string"));

    }
}
