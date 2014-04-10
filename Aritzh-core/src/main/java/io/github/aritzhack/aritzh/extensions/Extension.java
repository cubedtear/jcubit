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

package io.github.aritzhack.aritzh.extensions;

/**
 * This class should not be the extended by mods, it's just used to store the mods' data on runtime
 *
 * @author Aritz Lopez
 */
public class Extension {
    public final String name;
    public final String version;
    public final String waywiaVersion;
    public final Object extensionInstance;

    protected Extension(String name, String version, String waywiaVersion, Object extensionInstance) {
        this.name = name;
        this.version = version;
        this.waywiaVersion = waywiaVersion;
        this.extensionInstance = extensionInstance;
    }

}
