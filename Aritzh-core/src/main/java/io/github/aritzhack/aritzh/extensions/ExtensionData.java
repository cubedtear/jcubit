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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * All extension-classes must have this annotation in order to be loaded
 *
 * @author Aritz Lopez
 */
@Retention(value = RUNTIME)
@Target(value = TYPE)
public @interface ExtensionData {

    /**
     * Version of the extension. Can be any string in any format
     *
     * @return the version of the extension
     */
    String version();

    /**
     * The version of the app this extension is designed for
     *
     * @return the version of the app this extension is designed for
     */
    String appVersion() default "0.0.0"; // Non-existing version, to skip version-check

    /**
     * The name of this extension
     *
     * @return the name of this extension
     */
    String extensionName();
}
