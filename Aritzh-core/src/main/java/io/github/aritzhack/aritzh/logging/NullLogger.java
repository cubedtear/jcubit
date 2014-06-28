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

import io.github.aritzhack.aritzh.util.NotNull;
import io.github.aritzhack.aritzh.util.Nullable;

/**
 * @author Aritz Lopez
 */
public class NullLogger extends ALogger {

    /**
     * If the provided logger is not null, it is returned, otherwise a new {@link io.github.aritzhack.aritzh.logging.NullLogger} is returned
     *
     * @param logger A logger, or null
     * @return Either the provided logger if it is not null, or a new {@link io.github.aritzhack.aritzh.logging.NullLogger}
     */
    @NotNull
    public static ILogger getLogger(@Nullable ILogger logger) {
        return logger == null ? new NullLogger() : logger;
    }

    @Override
    public boolean isTraceEnabled() { return false; }

    @Override
    public void t(String msg) {}

    @Override
    public void t(String msg, Throwable t) {}

    @Override
    public boolean isDebugEnabled() { return false; }

    @Override
    public void d(String msg) {}

    @Override
    public void d(String msg, Throwable t) {}

    @Override
    public boolean isInfoEnabled() { return false; }

    @Override
    public void i(String msg) {}

    @Override
    public void i(String msg, Throwable t) {}

    @Override
    public boolean isWarnEnabled() { return false; }

    @Override
    public void w(String msg) {}

    @Override
    public void w(String msg, Throwable t) {}

    @Override
    public boolean isErrorEnabled() { return false; }

    @Override
    public void e(String msg) {}

    @Override
    public void e(String msg, Throwable t) {}


}
