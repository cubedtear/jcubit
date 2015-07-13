package io.github.aritzhack.aritzh.bds2;

import java.io.File;

/**
 * @author Aritz Lopez
 */
public class BDS2Test {

	public static void main(String[] args) {
		BDS bds = BDS.createEmpty();
		bds.writeToFile(new File("test.txt"));
	}
}
