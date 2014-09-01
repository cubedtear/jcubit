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

package io.github.aritzhack.aritzh.logging;

import org.apache.logging.log4j.message.ParameterizedMessage;

/**
 * @author Aritz Lopez
 */
public class LogEvent {

    private final String message;
    private final LogLevel level;
    private final Throwable throwable;

    public LogEvent(LogLevel level, String format, Object... args) {
        this(level, new ParameterizedMessage(format, args).getFormattedMessage());
    }

    public LogEvent(LogLevel level, String message) {
        this(level, null, message);
    }

    public LogEvent(LogLevel level, Throwable throwable, String message) {
        this.level = level;
        this.throwable = throwable;
        this.message = message;
    }

    public LogEvent(LogLevel level, Throwable throwable, String format, Object... args) {
        this(level, throwable, new ParameterizedMessage(format, args).getFormattedMessage());
    }

    public static enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR;

        private final String tag;

        LogLevel() {
            this.tag = "[" + this.name() + "]";
        }

        public String getTag() {
            return this.tag;
        }
    }
}
