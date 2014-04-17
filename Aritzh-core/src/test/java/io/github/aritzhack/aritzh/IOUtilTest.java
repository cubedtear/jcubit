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

package io.github.aritzhack.aritzh;

import io.github.aritzhack.aritzh.util.IOUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Aritz Lopez
 */
public class IOUtilTest {

    @Test
    public void deleteTest() {
        try {
            File folder1 = new File("folder1");
            IOUtil.delete(folder1);

            Assert.assertTrue("Folder 1 not be created", folder1.mkdirs());

            File folder11 = new File(folder1, "folder11");
            Assert.assertTrue("Folder 11 not be created", folder11.mkdirs());

            File file1 = new File(folder1, "file1.txt");
            Assert.assertTrue("File 1 not be created", file1.createNewFile());

            File file11 = new File(folder11, "file11.txt");
            Assert.assertTrue("File 11 could not be created", file11.createNewFile());

            Assert.assertTrue(folder1.exists());
            Assert.assertTrue(folder11.exists());
            Assert.assertTrue(file1.exists());
            Assert.assertTrue(file11.exists());

            boolean deleted = IOUtil.delete(folder1);

            Assert.assertTrue(deleted);

            Assert.assertFalse(folder1.exists());
            Assert.assertFalse(folder11.exists());
            Assert.assertFalse(file1.exists());
            Assert.assertFalse(file11.exists());

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
