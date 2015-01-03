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

import com.google.common.base.Throwables;
import io.github.aritzhack.aritzh.util.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Aritz Lopez
 */
public enum LogFormatter {
    ;

    private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss");
    private static final char MESSAGE_TOKEN = 'm';
    private static final char TIME_TOKEN = 't';
    private static final char LEVEL_TOKEN = 'l';
    private static final char THROWABLE_TOKEN = 'h';
    private static final char TOKEN_CHAR = '%';

    public static String formatLogMessage(LogLevel level, String message, @Nullable Throwable t, String format) {
        boolean inToken = false;
        String out = "";

        for(char c : format.toCharArray()) {
            if(!inToken) {
                if (c == TOKEN_CHAR) {
                    inToken = true;
                } else {
                    out+=c;
                }
            } else {
                inToken = false;
                switch (c) {
                    case TOKEN_CHAR:
                        out+=TOKEN_CHAR;
                        break;
                    case MESSAGE_TOKEN:
                        out+=message;
                        break;
                    case TIME_TOKEN:
                        out+=DF.format(new Date());
                        break;
                    case LEVEL_TOKEN:
                        out+=level.name();
                        break;
                    case THROWABLE_TOKEN:
                        if(t != null) {
                            out+=Throwables.getStackTraceAsString(t);
                        }
                        break;
                }
            }
        }
        return out;
    }

    public static String formatLogMessage(LogLevel level, String message, String format) {
        return LogFormatter.formatLogMessage(level, message, null, format);
    }
}
