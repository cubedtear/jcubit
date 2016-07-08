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

package io.github.cubedtear.jcubit.eventBus.logging;

import io.github.cubedtear.jcubit.eventBus.EventBus;
import io.github.cubedtear.jcubit.logging.SF;
import io.github.cubedtear.jcubit.logging.core.LogLevel;
import io.github.cubedtear.jcubit.util.API;
import io.github.cubedtear.jcubit.util.Nullable;

/**
 * An event posted when something is logged in an {@link EventLogger}
 *
 * @author Aritz Lopez
 * @see EventLogger
 * @see EventBus
 */
public class LogEvent {

    private final String message;
    private final LogLevel level;
    private final Throwable throwable;

    protected LogEvent(LogLevel level, String format, Object... args) {
        this(level, SF.f(format, args));
    }

    protected LogEvent(LogLevel level, String message) {
        this(level, message, (Throwable) null);
    }

    protected LogEvent(LogLevel level, String message, @Nullable Throwable throwable) {
        this.level = level;
        this.throwable = throwable;
        this.message = message;
    }

   protected LogEvent(LogLevel level, String format, @Nullable Throwable throwable, Object... args) {
        this(level, SF.f(format, args), throwable);
    }

    /**
     * @return the message logged.
     */
    @API
    public String getMessage() {
        return message;
    }

    /**
     * @return the level of the logged message.
     */
    @API
    public LogLevel getLevel() {
        return level;
    }

    /**
     * @return the throwable to log. May be null.
     */
    @API
    @Nullable
    public Throwable getThrowable() {
        return throwable;
    }
}


