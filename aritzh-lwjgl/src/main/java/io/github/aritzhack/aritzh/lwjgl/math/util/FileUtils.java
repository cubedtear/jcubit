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

package io.github.aritzhack.aritzh.lwjgl.math.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Aritz Lopez
 */
public class FileUtils {

    private FileUtils() {}

    public static String loadAsString(String file) {
        String result = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
            reader.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
