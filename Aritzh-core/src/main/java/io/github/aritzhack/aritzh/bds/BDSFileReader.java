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

package io.github.aritzhack.aritzh.bds;

import io.github.aritzhack.aritzh.IOUtil;

import java.io.File;
import java.io.IOException;

/**
 * @author Aritz Lopez
 */
public class BDSFileReader {

    public static void main(String[] args) throws IOException {
        File f = IOUtil.chooseFile("", null);
        if (f == null) System.exit(0);

        BDSCompound c = new BDSCompound(f);
        System.out.println("");
        System.out.println(c.toString());
    }
}
