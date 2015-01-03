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
 * Abstract Logger that does nothing. Useful if you want to implement only part of the logging system
 *
 * @author Aritz Lopez
 */
public abstract class ALogger implements ILogger {

    private String format(String format, Object... args) {
        return ParameterizedMessage.format(format, args);
    }

    // region ...Abstracts...

    @Override
    public abstract void t(String msg);

    @Override
    public abstract void t(String msg, Throwable t);

    @Override
    public abstract void d(String msg);

    @Override
    public abstract void d(String msg, Throwable t);

    @Override
    public abstract void i(String msg);

    @Override
    public abstract void i(String msg, Throwable t);

    @Override
    public abstract void w(String msg);

    @Override
    public abstract void w(String msg, Throwable t);

    @Override
    public abstract void e(String msg);

    @Override
    public abstract void e(String msg, Throwable t);

    // endregion

    @Override
    public void t(String format, Object... arguments) { this.t(format(format, arguments)); }

    @Override
    public void t(String msg, Throwable t, Object... args) { this.t(format(msg, args), t); }

    @Override
    public void d(String format, Object... arguments) { this.d(format(format, arguments)); }

    @Override
    public void d(String msg, Throwable t, Object... args) { this.d(format(msg, args), t); }

    @Override
    public void i(String format, Object... arguments) { this.i(format(format, arguments)); }

    @Override
    public void i(String msg, Throwable t, Object... args) { this.i(format(msg, args), t); }

    @Override
    public void w(String format, Object... arguments) { this.w(format(format, arguments)); }

    @Override
    public void w(String msg, Throwable t, Object... args) { this.w(format(msg, args), t); }

    @Override
    public void e(String format, Object... arguments) { this.e(format(format, arguments)); }

    @Override
    public void e(String msg, Throwable t, Object... args) { this.e(format(msg, args), t); }


}
