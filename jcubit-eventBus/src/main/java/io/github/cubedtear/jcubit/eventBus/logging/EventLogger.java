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
import io.github.cubedtear.jcubit.logging.core.ALogger;
import io.github.cubedtear.jcubit.logging.core.LogLevel;
import io.github.cubedtear.jcubit.util.API;

/**
 * Logger implementation that works by posting {@link LogEvent LogEvents} in an {@link EventBus}.
 *
 * @author Aritz Lopez
 * @see LogEvent
 * @see EventBus
 */
@API
public class EventLogger extends ALogger {

    private final EventBus bus;

    /**
     * Creates an EventLogger that will post the events in the given bus.
     * @param bus The bus into which the events should be posted.
     */
    public EventLogger(EventBus bus) {
        this.bus = bus;
    }

    /**
     * Get the event bus this logger is posting to.
     * @return the event bus of this logger.
     */
    public EventBus getEventBus() {
        return bus;
    }

    @Override
    public void t(String msg) {
        this.bus.post(new LogEvent(LogLevel.TRACE, msg));
    }

    @Override
    public void t(String msg, Throwable t) {
        this.bus.post(new LogEvent(LogLevel.TRACE, msg, t));
    }

    @Override
    public void d(String msg) {
        this.bus.post(new LogEvent(LogLevel.DEBUG, msg));
    }

    @Override
    public void d(String msg, Throwable t) {
        this.bus.post(new LogEvent(LogLevel.DEBUG, msg, t));
    }

    @Override
    public void i(String msg) {
        this.bus.post(new LogEvent(LogLevel.INFO, msg));
    }

    @Override
    public void i(String msg, Throwable t) {
        this.bus.post(new LogEvent(LogLevel.INFO, msg, t));
    }

    @Override
    public void w(String msg) {
        this.bus.post(new LogEvent(LogLevel.WARN, msg));
    }

    @Override
    public void w(String msg, Throwable t) {
        this.bus.post(new LogEvent(LogLevel.WARN, msg, t));
    }

    @Override
    public void e(String msg) {
        this.bus.post(new LogEvent(LogLevel.ERROR, msg));
    }

    @Override
    public void e(String msg, Throwable t) {
        this.bus.post(new LogEvent(LogLevel.ERROR, msg, t));
    }
}
