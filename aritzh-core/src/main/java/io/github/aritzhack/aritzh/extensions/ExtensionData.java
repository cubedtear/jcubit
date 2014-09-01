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
