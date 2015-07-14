package io.github.aritzhack.aritzh.bds2;

import junit.framework.Assert;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author Aritz Lopez
 */
public class BDS2Test {

	@Test
	public void testBDS() throws Exception {
		BDS bds1 = BDS.createEmpty();

		BDS nested = BDS.createEmpty("C");

		double value = 2.056d;
		nested.addDouble("A", value);
		bds1.addBDS(nested);

		File f = new File("test.txt");
		bds1.writeToFile(f);

		BDS bds2 = BDS.loadFromFile(f);
		assertEquals(value, bds2.getBDS("C").getDouble("A"), 0.0d);
	}
}
