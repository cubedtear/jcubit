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
import org.slf4j.Marker;

/**
 * Abstract Logger that does nothing. Useful if you want to implement only part of the logging system
 *
 * @author Aritz Lopez
 */
public abstract class ALogger implements ILogger {

    private String format(String format, Object... args) {
        return new ParameterizedMessage(format, args).getFormattedMessage();
    }

    @Override
    public abstract boolean isTraceEnabled();

    @Override
    public boolean isTraceEnabled(Marker marker) { return isTraceEnabled(); }

    @Override
    public abstract void t(String msg);

    @Override
    public void t(Marker marker, String msg) { this.t(msg); }

    @Override
    public void t(String format, Object... arguments) { this.t(format(format, arguments)); }

    @Override
    public void t(Marker marker, String format, Object... arguments) { this.t(format(format, arguments)); }

    @Override
    public abstract void t(String msg, Throwable t);

    @Override
    public void t(String msg, Object arg, Throwable t) { this.t(format(msg, arg), t); }

    @Override
    public void t(Marker marker, String msg, Throwable t) { this.t(msg, t); }

    @Override
    public void t(Marker marker, String msg, Object arg, Throwable t) { this.t(format(msg, arg), t); }

    @Override
    public abstract boolean isDebugEnabled();

    @Override
    public boolean isDebugEnabled(Marker marker) { return isDebugEnabled(); }

    @Override
    public abstract void d(String msg);

    @Override
    public void d(Marker marker, String msg) { this.d(msg); }

    @Override
    public void d(String format, Object... arguments) { this.d(format(format, arguments)); }

    @Override
    public void d(Marker marker, String format, Object... arguments) { this.d(format(format, arguments)); }

    @Override
    public abstract void d(String msg, Throwable t);

    @Override
    public void d(String msg, Object arg, Throwable t) { this.d(format(msg, arg), t); }

    @Override
    public void d(Marker marker, String msg, Throwable t) { this.d(msg, t); }

    @Override
    public void d(Marker marker, String msg, Object arg, Throwable t) { this.d(format(msg, arg), t); }

    @Override
    public abstract boolean isInfoEnabled();

    @Override
    public boolean isInfoEnabled(Marker marker) { return isInfoEnabled(); }

    @Override
    public abstract void i(String msg);

    @Override
    public void i(Marker marker, String msg) { this.i(msg); }

    @Override
    public void i(String format, Object... arguments) { this.i(format(format, arguments)); }

    @Override
    public void i(Marker marker, String format, Object... arguments) { this.i(format(format, arguments)); }

    @Override
    public abstract void i(String msg, Throwable t);

    @Override
    public void i(String msg, Object arg, Throwable t) { this.i(format(msg, arg), t); }

    @Override
    public void i(Marker marker, String msg, Throwable t) { this.i(msg, t); }

    @Override
    public void i(Marker marker, String msg, Object arg, Throwable t) { this.i(format(msg, arg), t); }

    @Override
    public abstract boolean isWarnEnabled();

    @Override
    public boolean isWarnEnabled(Marker marker) { return isWarnEnabled(); }

    @Override
    public abstract void w(String msg);

    @Override
    public void w(Marker marker, String msg) { this.w(msg); }

    @Override
    public void w(String format, Object... arguments) { this.w(format(format, arguments)); }

    @Override
    public void w(Marker marker, String format, Object... arguments) { this.w(format(format, arguments)); }

    @Override
    public abstract void w(String msg, Throwable t);

    @Override
    public void w(String msg, Object arg, Throwable t) { this.w(format(msg, arg), t); }

    @Override
    public void w(Marker marker, String msg, Throwable t) { this.w(msg, t); }

    @Override
    public void w(Marker marker, String msg, Object arg, Throwable t) { this.w(format(msg, arg), t); }

    @Override
    public abstract boolean isErrorEnabled();

    @Override
    public boolean isErrorEnabled(Marker marker) { return isErrorEnabled(); }

    @Override
    public abstract void e(String msg);

    @Override
    public void e(Marker marker, String msg) { this.e(msg); }

    @Override
    public void e(String format, Object... arguments) { this.e(format(format, arguments)); }

    @Override
    public void e(Marker marker, String format, Object... arguments) { this.e(format(format, arguments)); }

    @Override
    public abstract void e(String msg, Throwable t);

    @Override
    public void e(String msg, Object arg, Throwable t) { this.e(format(msg, arg), t); }

    @Override
    public void e(Marker marker, String msg, Throwable t) { this.e(msg, t); }

    @Override
    public void e(Marker marker, String msg, Object arg, Throwable t) { this.e(format(msg, arg), t); }


}
