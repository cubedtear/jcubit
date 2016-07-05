package io.github.cubedtear.jcubit.bds;

import com.google.common.base.Strings;
import io.github.cubedtear.jcubit.util.IOUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Aritz Lopez
 */
@SuppressWarnings("JavaDoc")
@Category(BDS.class)
public class BDSTest {

    private final File file = new File("test.txt");

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testBDS() throws Exception {

        BDS bds1 = new BDS("Parent");

        BDS nested1 = new BDS("Nested1");
        BDS nested2 = new BDS("Nested2");
        bds1.addBDSs("BDS Array", new BDS[]{nested1, nested2});

        final byte bite = (byte) -35;
        final byte[] bytes = new byte[]{-12, 35, 0, -1, Byte.MAX_VALUE, Byte.MIN_VALUE};

        final char shar = (char) 158;
        final char[] chars = new char[]{10, 9, 8, 7, 6, 5, 4, Character.MIN_VALUE, 2, 1, Character.MAX_VALUE, 1, 0};

        final short sort = (short) 5;
        final short[] shorts = new short[]{0, 32700, -32700, Short.MAX_VALUE, Short.MIN_VALUE, 0, -1, 1};

        final int in = 2115846111;
        final int[] ints = new int[]{10, 9, 8, 7, 6, Integer.MAX_VALUE, 4, 3, Integer.MIN_VALUE, 1, -12, -1, 0};

        final long loong = 15164613213165L;
        final long[] longs = {16346141614L, 46168416416L, Long.MIN_VALUE, 4681464641486L, Long.MAX_VALUE};

        final float flot = 2.056f;
        final float[] floats = new float[]{10.2f, Float.MIN_VALUE, 7.69f, Float.MAX_VALUE, 5.1f, Float.MIN_NORMAL};

        final double doble = 2.056d;
        final double[] doubles = new double[]{10.2, 9.8, 8.1, Double.MAX_VALUE, 7.69, Double.MIN_NORMAL, 6.3142, 5.1, Double.MIN_VALUE};


        final String str = "Adios";
        final String[] strings = new String[]{"", " ", Strings.repeat("A", Short.MAX_VALUE), Strings.repeat("A", Integer.MAX_VALUE / 20)};

        // Write them somewhere

        nested1.addByte("byte", bite);
        nested1.addBytes("bytes", bytes);

        nested1.addChar("char", shar);
        nested1.addChars("chars", chars);

        nested1.addShort("short", sort);
        nested1.addShorts("shorts", shorts);

        nested1.addInt("int", in);
        nested1.addInts("ints", ints);


        nested2.addLong("long", loong);
        nested2.addLongs("longs", longs);

        nested2.addFloat("float", flot);
        nested2.addFloats("floats", floats);

        nested2.addDouble("double", doble);
        nested2.addDoubles("doubles", doubles);

        nested2.addString("string", str);
        nested2.addStrings("strings", strings);

        bds1.writeToFile(file);

        BDS bds1Out = BDS.loadFromFile(file);
        BDS nestedOut1 = bds1Out.getBDSArray("BDS Array")[0];
        BDS nestedOut2 = bds1Out.getBDSArray("BDS Array")[1];

        assertEquals(bite, (byte) nestedOut1.getByte("byte"));
        assertEquals(shar, (char) nestedOut1.getChar("char"));
        assertEquals(sort, (short) nestedOut1.getShort("short"));
        assertEquals(in, (int) nestedOut1.getInt("int"));
        assertEquals(loong, (long) nestedOut2.getLong("long"));
        assertEquals(flot, nestedOut2.getFloat("float"), 0.0001f);
        assertEquals(doble, nestedOut2.getDouble("double"), 0.0001);
        assertEquals(str, nestedOut2.getString("string"));

        assertArrayEquals(bytes, nestedOut1.getByteArray("bytes"));
        assertArrayEquals(chars, nestedOut1.getCharArray("chars"));
        assertArrayEquals(shorts, nestedOut1.getShortArray("shorts"));
        assertArrayEquals(ints, nestedOut1.getIntArray("ints"));
        assertArrayEquals(longs, nestedOut2.getLongArray("longs"));
        assertArrayEquals(floats, nestedOut2.getFloatArray("floats"), 0.0001f);
        assertArrayEquals(doubles, nestedOut2.getDoubleArray("doubles"), 0.0001);
        assertArrayEquals(strings, nestedOut2.getStringArray("strings"));

        //BDSUtil.debug(bds1Out, System.out);
    }

    @After
    public void tearDown() throws Exception {
        IOUtil.delete(file);
    }
}
