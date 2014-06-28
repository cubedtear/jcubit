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

import org.slf4j.Marker;

/**
 * Abstract Logger that does nothing. Useful if you want to implement only part of the logging system
 *
 * @author Aritz Lopez
 */
public abstract class ALogger implements ILogger {

    @Override
    public boolean isTraceEnabled() { return false; }

    @Override
    public boolean isTraceEnabled(Marker marker) { return false; }

    @Override
    public void t(String msg) {}

    @Override
    public void t(Marker marker, String msg) {}

    @Override
    public void t(String format, Object... arguments) {}

    @Override
    public void t(Marker marker, String format, Object... arguments) {}

    @Override
    public void t(String msg, Throwable t) {}

    @Override
    public void t(String msg, Object arg, Throwable t) {}

    @Override
    public void t(Marker marker, String msg, Throwable t) {}

    @Override
    public void t(Marker marker, String msg, Object arg, Throwable t) {}

    @Override
    public boolean isDebugEnabled() { return false; }

    @Override
    public boolean isDebugEnabled(Marker marker) { return false; }

    @Override
    public void d(String msg) {}

    @Override
    public void d(Marker marker, String msg) {}

    @Override
    public void d(String format, Object... arguments) {}

    @Override
    public void d(Marker marker, String format, Object... arguments) {}

    @Override
    public void d(String msg, Throwable t) {}

    @Override
    public void d(String msg, Object arg, Throwable t) {}

    @Override
    public void d(Marker marker, String msg, Throwable t) {}

    @Override
    public void d(Marker marker, String msg, Object arg, Throwable t) {}

    @Override
    public boolean isInfoEnabled() { return false; }

    @Override
    public boolean isInfoEnabled(Marker marker) { return false; }

    @Override
    public void i(String msg) {}

    @Override
    public void i(Marker marker, String msg) {}

    @Override
    public void i(String format, Object... arguments) {}

    @Override
    public void i(Marker marker, String format, Object... arguments) {}

    @Override
    public void i(String msg, Throwable t) {}

    @Override
    public void i(String msg, Object arg, Throwable t) {}

    @Override
    public void i(Marker marker, String msg, Throwable t) {}

    @Override
    public void i(Marker marker, String msg, Object arg, Throwable t) {}

    @Override
    public boolean isWarnEnabled() { return false; }

    @Override
    public boolean isWarnEnabled(Marker marker) { return false; }

    @Override
    public void w(String msg) {}

    @Override
    public void w(Marker marker, String msg) {}

    @Override
    public void w(String format, Object... arguments) {}

    @Override
    public void w(Marker marker, String format, Object... arguments) {}

    @Override
    public void w(String msg, Throwable t) {}

    @Override
    public void w(String msg, Object arg, Throwable t) {}

    @Override
    public void w(Marker marker, String msg, Throwable t) {}

    @Override
    public void w(Marker marker, String msg, Object arg, Throwable t) {}

    @Override
    public boolean isErrorEnabled() { return false; }

    @Override
    public boolean isErrorEnabled(Marker marker) { return false; }

    @Override
    public void e(String msg) {}

    @Override
    public void e(Marker marker, String msg) {}

    @Override
    public void e(String format, Object... arguments) {}

    @Override
    public void e(Marker marker, String format, Object... arguments) {}

    @Override
    public void e(String msg, Throwable t) {}

    @Override
    public void e(String msg, Object arg, Throwable t) {}

    @Override
    public void e(Marker marker, String msg, Throwable t) {}

    @Override
    public void e(Marker marker, String msg, Object arg, Throwable t) {}
}
