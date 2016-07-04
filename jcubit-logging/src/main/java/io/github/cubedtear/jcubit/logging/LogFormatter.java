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
 * Singleton utility class to create log messages from the message, throwable and level.
 * <p>
 * The format string is a normal string, where some tokens are replaced as follows:
 * <ul>
 * <li>%m is replaced by the message.</li>
 * <li>%l by the {@link LogLevel#getShorthand() shorthand} of the level.</li>
 * <li>%L by the full name of the level.</li>
 * <li>%e by the throwable's stack trace</li>
 * <li>%n by the name of the logger</li>
 * <li>%t by the name of the current thread</li>
 * <li>%d by the current date/time</li>
 * </ul>
 * <p>
 * Tokens may also be added only if a condition is met. The condition is given in the following format:
 * {@code ?condition{ifMet}}. Where {@code condition} is one of the following:
 * <ul>
 * <li>{@code t} - Will be met if there is a throwable.</li>
 * <li>{@code n} - Will be met if the name of the logger is not null or empty.</li>
 * <li>{@code lX} or {@code LX} where X is any character - Will be met if X is an identifier of the level
 * of the message.</li>
 * <li>{@code m} - Will be met if the message is not null nor empty.</li>
 * </ul>
 * <p>
 * Note: the format of the date is by default done by
 * {@link SimpleDateFormat#SimpleDateFormat(String) new SimpleDateFormat("HH:mm:SSS")}. If a different format is desired,
 * the format may be specified in the following way: {@code %d{format}}, where {@code format} is accepted by the
 * constructor of {@link SimpleDateFormat}.
 * <p>
 * As an example, {@code %d{HH.mm.ss} - [%t] %l?n{ %n} - %m?e{\n%e}?lE{\n----ERROR----\n}} will print the following:
 * <ul>
 * <li>First the current time, in the given format.</li>
 * <li>Then, separated by a dash and spaces, inside brackets, the name of the current thread.</li>
 * <li>After a whitespace, the short name of the level</li>
 * <li>If the name is the logger is not null nor empty, it will be printed too, after a whitespace.</li>
 * <li>After a separation, the message will be printed.</li>
 * <li>If there is an exception, after a line break its stack trace will be printed.</li>
 * <li>If the level is ERROR, a marker line will be printed, so that it is easily found on the log.</li>
 * </ul>
 *
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

    /**
     * Formats the given log message, with {@code null} as the throwable.
     *
     * @param level      The level of the message.
     * @param format     The format string.
     * @param message    The log message.
     * @param loggerName The name of the logger.
     * @return The formatted log message.
     * @see LogFormatter
     */
    public static String formatLogMessage(@NotNull LogLevel level, @NotNull String format, @Nullable String message, @Nullable String loggerName) {
        return LogFormatter.formatLogMessage(level, format, message, null, loggerName);
    }

    /**
     * Formats the given message.
     *
     * @param level      The level of the message.
     * @param format     The format string.
     * @param message    The log message.
     * @param t          The throwable thrown.
     * @param loggerName The name of the logger.
     * @return The formatted log message.
     * @see LogFormatter
     */
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
                if (condition.equalsIgnoreCase(String.valueOf(TOKEN_THROWABLE))) {
                    conditionMet = t != null;
                } else if (condition.startsWith(String.valueOf(TOKEN_LEVEL)) || condition.startsWith(String.valueOf(TOKEN_FULL_LEVEL))) {
                    String cLevel = condition.substring(1);
                    conditionMet = LogLevel.getLevel(cLevel) == level;
                } else if (condition.equalsIgnoreCase(String.valueOf(TOKEN_MESSAGE))) {
                    conditionMet = !Strings.isNullOrEmpty(message);
                } else if (condition.equalsIgnoreCase(String.valueOf(TOKEN_NAME))) {
                    conditionMet = !Strings.isNullOrEmpty(loggerName);
                }
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }
}
