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

package io.github.aritzhack.aritzh.util;

/**
 * @author Aritz Lopez
 */
@SuppressWarnings("UnusedDeclaration")
public class OSUtil {

    private static EnumOS OS = null;

    public static EnumOS getOs() {
        if (OSUtil.OS != null) return OS;

        String s = System.getProperty("os.name").toLowerCase();
        return OSUtil.OS = s.contains("win") ? EnumOS.WINDOWS :
                s.contains("mac") ? EnumOS.MACOS :
                        s.contains("solaris") || s.contains("sunos") || s.contains("linux") || s.contains("unix") ? EnumOS.UNIX :
                                EnumOS.UNKNOWN;
    }

    public static enum EnumOS {
        WINDOWS, UNIX, MACOS, UNKNOWN
    }
}
