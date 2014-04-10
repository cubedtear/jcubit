/*
 * Copyright (c) 2014 Aritzh (Aritz Lopez)
 *
 * This file is part of AritzhUtil
 *
 * AritzhUtil is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * AritzhUtil is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with AritzhUtil.
 * If not, see http://www.gnu.org/licenses/.
 */

package io.github.aritzhack.aritzh;

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
