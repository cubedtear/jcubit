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

package io.github.cubedtear.jcubit;

import io.github.cubedtear.jcubit.util.OSUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author Aritz Lopez
 */
public class OSTest {

    @Test
    public void getOsTest() {
        File f = new File("");

        if (OSUtil.getOs() == OSUtil.EnumOS.UNKNOWN) Assert.fail("Unknown OS: " + System.getProperty("os.name").toLowerCase());
        try {
            Assert.assertTrue(f.getCanonicalPath().startsWith("/") == (OSUtil.getOs() == OSUtil.EnumOS.UNIX || OSUtil.getOs() == OSUtil.EnumOS.MACOS));
        } catch (IOException e) {
            Assert.assertTrue(f.getAbsolutePath().startsWith("/") == (OSUtil.getOs() == OSUtil.EnumOS.UNIX || OSUtil.getOs() == OSUtil.EnumOS.MACOS));
        }
    }
}
