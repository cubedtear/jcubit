/*
 * Copyright 2015 Aritz Lopez
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

package io.github.aritzhack.aritzh.logging;

import io.github.aritzhack.aritzh.util.NotNull;
import io.github.aritzhack.aritzh.util.Nullable;

/**
 * @author Aritz Lopez
 */
public class StringFormatter {

	private static final char TOKEN_START = '{';
	private static final char TOKEN_END = '}';
	private static final String NULL_STRING = "{NULL}";

	@NotNull
	public static String format(@Nullable String format, @Nullable Object... args) {
		if (format == null || format.length() == 0 || "".equals(format)) return "";
		char[] chars = format.toCharArray();

		StringBuilder b = new StringBuilder(format.length());

		int token = 0;

		for (int i = 0; i < chars.length; i++) {
			if (i < chars.length - 1 && chars[i] == TOKEN_START && chars[i + 1] == TOKEN_END) {
				if (args == null || token >= args.length) b.append(NULL_STRING);
				else b.append(args[token++]);
				i++;
			} else {
				b.append(chars[i]);
			}
		}

		return b.toString();
	}
}
