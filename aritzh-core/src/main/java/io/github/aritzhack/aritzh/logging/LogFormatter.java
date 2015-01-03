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

import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import io.github.aritzhack.aritzh.util.NotNull;
import io.github.aritzhack.aritzh.util.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author Aritz Lopez
 */
public enum LogFormatter {
    ;

    private static final DateFormat DF = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.getDefault());
    private static final char MESSAGE_TOKEN = 'm';
    private static final char DATE_TOKEN = 'd';
    private static final char LEVEL_TOKEN = 'l';
    private static final char FULL_LEVEL_TOKEN = 'L';
    private static final char THROWABLE_TOKEN = 't';
    private static final char TOKEN_CHAR = '%';
    private static final char CONDITION_CHAR = '?';
    private static final char DELIMITER_LEFT = '{';
    private static final char DELIMITER_RIGHT = '}';

    private static final Map<String, DateFormat> dateFormats = Maps.newHashMap();

    public static String formatLogMessage(LogLevel level, String message, String format) {
        return LogFormatter.formatLogMessage(level, message, null, format);
    }

    public static String formatLogMessage(@NotNull LogLevel level, @Nullable String message, @Nullable Throwable t, @NotNull String format) {
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

            if (c == TOKEN_CHAR && notLast) { // Check if we are in a replace-token
                i++;
                c = chars[i];
                switch (c) {
                    case TOKEN_CHAR:
                        out.append(TOKEN_CHAR);
                        break;
                    case MESSAGE_TOKEN:
                        out.append(message);
                        break;
                    case DATE_TOKEN:
                        if (notLast && chars[i + 1] == DELIMITER_LEFT) {
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
                    case LEVEL_TOKEN:
                        out.append(level.name());
                    case FULL_LEVEL_TOKEN:
                        out.append("[").append(level.name()).append("]");
                        out.append(Strings.repeat(" ", LogLevel.maxNameLength - level.name().length()));
                        break;
                    case THROWABLE_TOKEN:
                        if (t != null) {
                            out.append(Throwables.getStackTraceAsString(t));
                        }
                        break;
                }
            } else if (c == CONDITION_CHAR) { // Check if we are starting a condition environment
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
                if (condition.equalsIgnoreCase(toS(THROWABLE_TOKEN))) {
                    conditionMet = t != null;
                } else if (condition.startsWith(toS(LEVEL_TOKEN)) || condition.startsWith(toS(FULL_LEVEL_TOKEN))) {
                    String cLevel = condition.substring(1);
                    conditionMet = LogLevel.getLevel(cLevel) == level;
                } else if (condition.equalsIgnoreCase(toS(MESSAGE_TOKEN))) {
                    conditionMet = !Strings.isNullOrEmpty(message);
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
