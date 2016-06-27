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

package io.github.cubedtear.jcubit.extensions;

/**
 * This class should not be the extended by mods, it's just used to store the mods' data on runtime
 *
 * @author Aritz Lopez
 */
public class Extension {
	public final String name;
	public final String version;
	public final String appVersion;
	public final Object extensionInstance;

	protected Extension(String name, String version, String appVersion, Object extensionInstance) {
		this.name = name;
		this.version = version;
		this.appVersion = appVersion;
		this.extensionInstance = extensionInstance;
	}

}
