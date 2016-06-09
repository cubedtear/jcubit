package io.github.aritzhack.aritzh.bds;

import io.github.aritzhack.aritzh.util.IOUtil;
import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Aritz Lopez
 */
public class BDSTest {

	private final File file = new File("test.txt");

	@Test
	public void testBDS() throws Exception {
		BDS bds1 = BDS.createEmpty("");

		BDS nested1 = BDS.createEmpty("C");
		BDS nested2 = BDS.createEmpty("D");
		bds1.addBDSs("bdss", new BDS[]{nested1, nested2});

		final double doble = 2.056d;
		final int[] ints = new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, -12, -1, 0};
		final char[] chars = new char[]{10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 12, 1, 0};
		final String str = "Adios";
		final byte bite = (byte) 5648;
		final char shar = (char) 158;
		final short sort = (short) 5;
		final long loong = 15164613213165L;
		final long[] longs = {16346141614L, 46168416416L, 4681464641486L};

		bds1.addLong("long", loong);
		nested1.addDouble("double", doble);
		nested1.addString("str", str);
		nested2.addByte("byte", bite);
		nested2.addChar("char", shar);
		nested2.addChars("chars", chars);
		nested2.addShort("short", sort);
		bds1.addLongs("longs", longs);
		nested1.addInts("ints", ints);

		bds1.writeToFile(file);

		BDS bds2 = BDS.loadFromFile(file);
		BDS nestedOut1 = bds2.getBDSArray("bdss")[0];
		BDS nestedOut2 = bds2.getBDSArray("bdss")[1];

		assertEquals(loong, (long) bds2.getLong("long"));
		assertEquals(doble, nestedOut1.getDouble("double"), 0.0d);
		assertEquals(str, nestedOut1.getString("str"));
		assertEquals(bite, (byte) nestedOut2.getByte("byte"));
		assertEquals(sort, (short) nestedOut2.getShort("short"));
		assertEquals(shar, (char) nestedOut2.getChar("char"));
		assertArrayEquals(chars, nestedOut2.getCharArray("chars"));
		assertArrayEquals(longs, bds2.getLongArray("longs"));
		assertArrayEquals(ints, nested1.getIntArray("ints"));
	}

	@After
	public void tearDown() throws Exception {
		IOUtil.delete(file);

	}
}
