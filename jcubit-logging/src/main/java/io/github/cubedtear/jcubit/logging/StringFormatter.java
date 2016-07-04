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

package io.github.cubedtear.jcubit.logging;

import io.github.cubedtear.jcubit.util.NotNull;
import io.github.cubedtear.jcubit.util.Nullable;

/**
 * Formats strings, replacing {} by arguments.
 * @author Aritz Lopez
 * @deprecated Use {@link SF} instead.
 */
@Deprecated
public class StringFormatter {

	/**
	 * Formats the given string by replacing <i>{}</i> by the given arguments.
	 * Order is preserved: the first occurrence of <i>{}</i> will be replaced by {@code args[0]}, and so on.
	 * If there are more <i>{}</i> tokens than arguments given, the token will be replaced by <i>{NULL}</i>.
	 * @param format The format string.
	 * @param args The arguments to replace in the string.
     * @return The formatted string.
	 * @deprecated Use {@link SF#f(String, Object... args)} instead.
	 * */
	@Deprecated
	@NotNull
	public static String format(@Nullable String format, @Nullable Object... args) {
		return SF.f(format, args);
	}
}
