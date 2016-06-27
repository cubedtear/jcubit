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

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.logging.core.LogLevel;
import io.github.cubedtear.jcubit.util.NotNull;
import io.github.cubedtear.jcubit.util.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public enum LogFormatter {
	;

	private static final DateFormat DF = new SimpleDateFormat("HH:mm:ss.SSS");

	private static final char TOKEN_MESSAGE = 'm';
	private static final char TOKEN_DATE = 'd';
	private static final char TOKEN_LEVEL = 'l';
	private static final char TOKEN_FULL_LEVEL = 'L';
	private static final char TOKEN_THROWABLE = 'e';
	private static final char TOKEN_THREAD = 't';
	private static final char TOKEN_NAME = 'n';

	private static final char BEGIN_TOKEN = '%';
	private static final char BEGIN_CONDITION = '?';
	private static final char DELIMITER_LEFT = '{';
	private static final char DELIMITER_RIGHT = '}';

	private static final Map<String, DateFormat> dateFormats = Maps.newHashMap();

	public static String formatLogMessage(@NotNull LogLevel level, @NotNull String format, @Nullable String message, @Nullable String loggerName) {
		return LogFormatter.formatLogMessage(level, format, message, null, loggerName);
	}

	public static String formatLogMessage(@NotNull LogLevel level, @NotNull String format, @Nullable String message, @Nullable Throwable t, @Nullable String loggerName) {
		boolean inCondition = false;
		boolean conditionMet = false;
		StringBuilder out = new StringBuilder();

		char[] chars = format.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			boolean notLast = i < chars.length - 1;
			char c = chars[i];

			// Check for ending the condition environment
			if (inCondition && c == DELIMITER_RIGHT) {
				inCondition = false;
				conditionMet = false;
				continue;
			}

			// Skip if the condition is unmet
			if (inCondition && !conditionMet) continue;

			if (c == BEGIN_TOKEN && notLast) { // Check if we are in a replace-token
				i++;
				c = chars[i];
				switch (c) {
					case BEGIN_TOKEN:
						out.append(BEGIN_TOKEN);
						break;
					case TOKEN_MESSAGE:
						out.append(message);
						break;
					case TOKEN_DATE:
						if (chars[i + 1] == DELIMITER_LEFT) {
							i++;
							String dFormat = "";
							while (i < chars.length - 1) {
								i++;
								c = chars[i];
								if (c == DELIMITER_RIGHT) break;
								dFormat += c;
							}
							if (!dateFormats.containsKey(dFormat)) {
								dateFormats.put(dFormat, new SimpleDateFormat(dFormat));
							}
							out.append(dateFormats.get(dFormat).format(new Date()));
						} else {
							out.append(DF.format(new Date()));
						}
						break;
					case TOKEN_LEVEL:
						out.append(level.name());
						break;
					case TOKEN_FULL_LEVEL:
						out.append("[").append(level.name()).append("]");
						out.append(Strings.repeat(" ", LogLevel.maxNameLength - level.name().length()));
						break;
					case TOKEN_THROWABLE:
						if (t != null) {
							out.append(Throwables.getStackTraceAsString(t));
						}
						break;
					case TOKEN_THREAD:
						out.append(Thread.currentThread().getName());
						break;
					case TOKEN_NAME:
						out.append(loggerName);
						break;
				}
			} else if (c == BEGIN_CONDITION) { // Check if we are starting a condition environment
				inCondition = true;

				// Read the condition
				String condition = "";

				while (i < chars.length - 1) { // Safe-check not to get an ArrayIndexOutOfBoundsException
					i++;
					c = chars[i];
					if (c == DELIMITER_LEFT) break; // If we reach the beginning if the environment, stop
					condition += c;
				}

				// Parse the condition
				if (condition.equalsIgnoreCase(toS(TOKEN_THROWABLE))) {
					conditionMet = t != null;
				} else if (condition.startsWith(toS(TOKEN_LEVEL)) || condition.startsWith(toS(TOKEN_FULL_LEVEL))) {
					String cLevel = condition.substring(1);
					conditionMet = LogLevel.getLevel(cLevel) == level;
				} else if (condition.equalsIgnoreCase(toS(TOKEN_MESSAGE))) {
					conditionMet = !Strings.isNullOrEmpty(message);
				} else if (condition.equalsIgnoreCase(toS(TOKEN_NAME))) {
					conditionMet = !Strings.isNullOrEmpty(loggerName);
				}
			} else {
				out.append(c);
			}
		}
		return out.toString();
	}

	private static String toS(char c) {
		return String.valueOf(c);
	}
}
