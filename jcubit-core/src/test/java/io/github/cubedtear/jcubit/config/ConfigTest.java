/*
 * Copyright 2014 Aritz Lopez
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.cubedtear.jcubit.config;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Aritz Lopez
 */
public class ConfigTest {

	private static final String[] validProperties = new String[]{"key=val", " key=val", "key =val", "key= val", "key=val ", " key =val", " key= val", " key=val ", "key = val", "key =val ", "key= val ", " key = val", " key =val ", " key = val ", "     key     =   val        ", "key=v al", " key=v al", "key =v al", "key= v al", "key=v al ", " key =v al", " key= v al", " key=v al ", "key = v al", "key =v al ", "key= v al ", " key = v al", " key =v al ", " key = v al ", "     key     =   v al        "};
	private static final String[] invalidProperties = new String[]{"k ey = val", "key = ", "key =", "key =         ", "     =         ", " = ", "="};
	private static final String[] validCategories = new String[]{" [cat]", "[cat] ", " [ cat]", " [cat ]", "[ cat] ", "[cat ] ", " [cat] ", " [ cat] ", " [cat ] ", " [ cat ] ", "   [  cat  ]   ", "[ca t]", "   [ca   t]  ", "   [   ca t   ]   ", "   [   ca   t   ]   "};
	private static final String[] invalidCategories = new String[]{"[]", "[ ]", "[    ]", "    []    ", "    [   ]   "};

	private static final Path configPath = Paths.get("testPathConfig.cfg");
	private static final File configFile = new File("testFileConfig.cfg");

	@AfterClass
	public static void after() throws IOException {
		Files.deleteIfExists(configFile.toPath());
		Files.deleteIfExists(configPath);
	}

	private static boolean matchesProperty(String input) {
		return Configuration.PROP_REGEX.matcher(input).matches();
	}

	private static boolean matchesCategory(String input) {
		return Configuration.CATEGORY_REGEX.matcher(input).matches();
	}

	@Test
	public void regexTest() {
		for (String validProperty : validProperties) {
			assertTrue("\"" + validProperty + "\" does not match!", matchesProperty(validProperty));
		}

		for (String invalidProperty : invalidProperties) {
			assertFalse("\"" + invalidProperty + "\" matches!", matchesProperty(invalidProperty));
		}

		for (String validCategory : validCategories) {
			assertTrue("\"" + validCategory + "\" does not match!", matchesCategory(validCategory));
		}

		for (String invalidCategory : invalidCategories) {
			assertFalse("\"" + invalidCategory + "\" matches!", matchesCategory(invalidCategory));
		}
	}

	@Test
	public void fileReadWriteTest() throws IOException {
		Configuration c = Configuration.newConfig(configFile);

		String cat1 = "Category1";

		String key11 = "Key11", value11 = "Val11",
				key12 = "Key 12", value12 = "Val 12",
				key13 = "   Key13   ", value13 = "  Val13  ";

		String cat2 = "Category 2";

		String key21 = "Key21", value21 = "Val21",
				key22 = "Key 22", value22 = "Val 22",
				key23 = "   Key23   ", value23 = "  Val23  ";

		c.setProperty(cat1, key11, value11);
		c.setProperty(cat1, key12, value12);
		c.setProperty(cat1, key13, value13);

		c.setProperty(cat2, key21, value21);
		c.setProperty(cat2, key22, value22);
		c.setProperty(cat2, key23, value23);

		c.save();

		c = Configuration.loadConfig(configFile);

		String afterValue11 = c.getProperty(cat1, key11),
				afterValue12 = c.getProperty(cat1, key12),
				afterValue13 = c.getProperty(cat1, key13),
				afterValue21 = c.getProperty(cat2, key21),
				afterValue22 = c.getProperty(cat2, key22),
				afterValue23 = c.getProperty(cat2, key23);


		Assert.assertEquals(value11, afterValue11);
		Assert.assertEquals(value12, afterValue12);
		Assert.assertEquals(value13.trim(), afterValue13);
		Assert.assertEquals(value21, afterValue21);
		Assert.assertEquals(value22, afterValue22);
		Assert.assertEquals(value23.trim(), afterValue23);
	}

	@Test
	public void pathReadWriteTest() throws IOException {
		Configuration c = Configuration.newConfig(configPath);

		String cat1 = "Category1";

		String key11 = "Key11", value11 = "Val11",
				key12 = "Key 12", value12 = "Val 12",
				key13 = "   Key13   ", value13 = "  Val13  ";

		String cat2 = "Category 2";

		String key21 = "Key21", value21 = "Val21",
				key22 = "Key 22", value22 = "Val 22",
				key23 = "   Key23   ", value23 = "  Val23  ";

		c.setProperty(cat1, key11, value11);
		c.setProperty(cat1, key12, value12);
		c.setProperty(cat1, key13, value13);

		c.setProperty(cat2, key21, value21);
		c.setProperty(cat2, key22, value22);
		c.setProperty(cat2, key23, value23);

		c.save();

		c = Configuration.loadConfig(configPath);

		String afterValue11 = c.getProperty(cat1, key11),
				afterValue12 = c.getProperty(cat1, key12),
				afterValue13 = c.getProperty(cat1, key13),
				afterValue21 = c.getProperty(cat2, key21),
				afterValue22 = c.getProperty(cat2, key22),
				afterValue23 = c.getProperty(cat2, key23);


		Assert.assertEquals(value11, afterValue11);
		Assert.assertEquals(value12, afterValue12);
		Assert.assertEquals(value13.trim(), afterValue13);
		Assert.assertEquals(value21, afterValue21);
		Assert.assertEquals(value22, afterValue22);
		Assert.assertEquals(value23.trim(), afterValue23);
	}
}
