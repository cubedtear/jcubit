/*
 * Copyright (c) 2014 Aritzh (Aritz Lopez)
 *
 * This file is part of AritzhUtil
 *
 * AritzhUtil is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * AritzhUtil is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with AritzhUtil.
 * If not, see http://www.gnu.org/licenses/.
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
