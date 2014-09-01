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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * Wrapper for the {@link org.slf4j.Logger SLF4J Logger}
 * @author Aritz Lopez
 */
public class SLF4JLogger implements ILogger {

    public final Logger logger;

    public SLF4JLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }

    public SLF4JLogger(Class clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }

    public SLF4JLogger(Logger logger) {
        this.logger = logger;
    }

    //region ...Trace...

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.logger.isTraceEnabled(marker);
    }

    @Override
    public void t(String msg) {
        this.logger.trace(msg);
    }

    @Override
    public void t(Marker marker, String msg) {
        this.logger.trace(marker, msg);
    }

    @Override
    public void t(String format, Object... arguments) {
        this.logger.trace(format, arguments);
    }

    @Override
    public void t(Marker marker, String format, Object... arguments) {
        this.logger.trace(marker, format, arguments);
    }

    @Override
    public void t(String msg, Throwable t) {
        this.logger.trace(msg, t);
    }

    @Override
    public void t(String msg, Object arg, Throwable t) {
        this.logger.trace(msg, arg, t);
    }

    @Override
    public void t(Marker marker, String msg, Throwable t) {
        this.logger.trace(marker, msg, t);
    }

    @Override
    public void t(Marker marker, String msg, Object arg, Throwable t) {
        this.logger.trace(marker, msg, arg, t);
    }

    //endregion

    //region ...Debug...

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.logger.isDebugEnabled(marker);
    }

    @Override
    public void d(String msg) {
        this.logger.debug(msg);
    }

    @Override
    public void d(Marker marker, String msg) {
        this.logger.debug(marker, msg);
    }

    @Override
    public void d(String format, Object... arguments) {
        this.logger.debug(format, arguments);
    }

    @Override
    public void d(Marker marker, String format, Object... arguments) {
        this.logger.debug(marker, format, arguments);
    }

    @Override
    public void d(String msg, Throwable t) {
        this.logger.debug(msg, t);
    }

    @Override
    public void d(String msg, Object arg, Throwable t) {
        this.logger.debug(msg, arg, t);
    }

    @Override
    public void d(Marker marker, String msg, Throwable t) {
        this.logger.debug(marker, msg, t);
    }

    @Override
    public void d(Marker marker, String msg, Object arg, Throwable t) {
        this.logger.debug(marker, msg, arg, t);
    }

    //endregion

    //region ...Info...

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.logger.isInfoEnabled(marker);
    }

    @Override
    public void i(String msg) {
        this.logger.info(msg);
    }

    @Override
    public void i(Marker marker, String msg) {
        this.logger.info(marker, msg);
    }

    @Override
    public void i(String format, Object... arguments) {
        this.logger.info(format, arguments);
    }

    @Override
    public void i(Marker marker, String format, Object... arguments) {
        this.logger.info(marker, format, arguments);
    }

    @Override
    public void i(String msg, Throwable t) {
        this.logger.info(msg, t);
    }

    @Override
    public void i(String msg, Object arg, Throwable t) {
        this.logger.info(msg, arg, t);
    }

    @Override
    public void i(Marker marker, String msg, Throwable t) {
        this.logger.info(marker, msg, t);
    }

    @Override
    public void i(Marker marker, String msg, Object arg, Throwable t) {
        this.logger.info(marker, msg, arg, t);
    }

    //endregion

    //region ...Warn...

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.logger.isWarnEnabled(marker);
    }

    @Override
    public void w(String msg) {
        this.logger.warn(msg);
    }

    @Override
    public void w(Marker marker, String msg) {
        this.logger.warn(marker, msg);
    }

    @Override
    public void w(String format, Object... arguments) {
        this.logger.warn(format, arguments);
    }

    @Override
    public void w(Marker marker, String format, Object... arguments) {
        this.logger.warn(marker, format, arguments);
    }

    @Override
    public void w(String msg, Throwable t) {
        this.logger.warn(msg, t);
    }

    @Override
    public void w(String msg, Object arg, Throwable t) {
        this.logger.warn(msg, arg, t);
    }

    @Override
    public void w(Marker marker, String msg, Throwable t) {
        this.logger.warn(marker, msg, t);
    }

    @Override
    public void w(Marker marker, String msg, Object arg, Throwable t) {
        this.logger.warn(marker, msg, arg, t);
    }

    //endregion

    //region ...Error..

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.logger.isErrorEnabled(marker);
    }

    @Override
    public void e(String msg) {
        this.logger.error(msg);
    }

    @Override
    public void e(Marker marker, String msg) {
        this.logger.error(marker, msg);
    }

    @Override
    public void e(String format, Object... arguments) {
        this.logger.error(format, arguments);
    }

    @Override
    public void e(Marker marker, String format, Object... arguments) {
        this.logger.error(marker, format, arguments);
    }

    @Override
    public void e(String msg, Throwable t) {
        this.logger.error(msg, t);
    }

    @Override
    public void e(String msg, Object arg, Throwable t) {
        this.logger.error(msg, arg, t);
    }

    @Override
    public void e(Marker marker, String msg, Throwable t) {
        this.logger.error(marker, msg, t);
    }

    @Override
    public void e(Marker marker, String msg, Object arg, Throwable t) {
        this.logger.error(marker, msg, arg, t);
    }


    //endregion

    public String toString() {
        return this.logger.toString();
    }

}
