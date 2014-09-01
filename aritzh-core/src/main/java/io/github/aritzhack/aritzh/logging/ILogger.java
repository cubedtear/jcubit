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
 * @author Aritz Lopez
 */
public interface ILogger {

    boolean isTraceEnabled();

    boolean isTraceEnabled(Marker marker);

    void t(String msg);

    void t(Marker marker, String msg);

    void t(String format, Object... arguments);

    void t(Marker marker, String format, Object... arguments);

    void t(String msg, Throwable t);

    void t(String msg, Object arg, Throwable t);

    void t(Marker marker, String msg, Throwable t);

    void t(Marker marker, String msg, Object arg, Throwable t);

    boolean isDebugEnabled();

    boolean isDebugEnabled(Marker marker);

    void d(String msg);

    void d(Marker marker, String msg);

    void d(String format, Object... arguments);

    void d(Marker marker, String format, Object... arguments);

    void d(String msg, Throwable t);

    void d(String msg, Object arg, Throwable t);

    void d(Marker marker, String msg, Throwable t);

    void d(Marker marker, String msg, Object arg, Throwable t);

    boolean isInfoEnabled();

    boolean isInfoEnabled(Marker marker);

    void i(String msg);

    void i(Marker marker, String msg);

    void i(String format, Object... arguments);

    void i(Marker marker, String format, Object... arguments);

    void i(String msg, Throwable t);

    void i(String msg, Object arg, Throwable t);

    void i(Marker marker, String msg, Throwable t);

    void i(Marker marker, String msg, Object arg, Throwable t);

    boolean isWarnEnabled();

    boolean isWarnEnabled(Marker marker);

    void w(String msg);

    void w(Marker marker, String msg);

    void w(String format, Object... arguments);

    void w(Marker marker, String format, Object... arguments);

    void w(String msg, Throwable t);

    void w(String msg, Object arg, Throwable t);

    void w(Marker marker, String msg, Throwable t);

    void w(Marker marker, String msg, Object arg, Throwable t);

    boolean isErrorEnabled();

    boolean isErrorEnabled(Marker marker);

    void e(String msg);

    void e(Marker marker, String msg);

    void e(String format, Object... arguments);

    void e(Marker marker, String format, Object... arguments);

    void e(String msg, Throwable t);

    void e(String msg, Object arg, Throwable t);

    void e(Marker marker, String msg, Throwable t);

    void e(Marker marker, String msg, Object arg, Throwable t);
}
