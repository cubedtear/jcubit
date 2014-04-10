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
public class NullLogger implements ILogger {

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void t(String msg) {

    }

    @Override
    public void t(Marker marker, String msg) {

    }

    @Override
    public void t(String format, Object... arguments) {

    }

    @Override
    public void t(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void t(String msg, Throwable t) {

    }

    @Override
    public void t(String msg, Object arg, Throwable t) {

    }

    @Override
    public void t(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void t(Marker marker, String msg, Object arg, Throwable t) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void d(String msg) {

    }

    @Override
    public void d(Marker marker, String msg) {

    }

    @Override
    public void d(String format, Object... arguments) {

    }

    @Override
    public void d(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void d(String msg, Throwable t) {

    }

    @Override
    public void d(String msg, Object arg, Throwable t) {

    }

    @Override
    public void d(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void d(Marker marker, String msg, Object arg, Throwable t) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void i(String msg) {

    }

    @Override
    public void i(Marker marker, String msg) {

    }

    @Override
    public void i(String format, Object... arguments) {

    }

    @Override
    public void i(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void i(String msg, Throwable t) {

    }

    @Override
    public void i(String msg, Object arg, Throwable t) {

    }

    @Override
    public void i(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void i(Marker marker, String msg, Object arg, Throwable t) {

    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void w(String msg) {

    }

    @Override
    public void w(Marker marker, String msg) {

    }

    @Override
    public void w(String format, Object... arguments) {

    }

    @Override
    public void w(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void w(String msg, Throwable t) {

    }

    @Override
    public void w(String msg, Object arg, Throwable t) {

    }

    @Override
    public void w(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void w(Marker marker, String msg, Object arg, Throwable t) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void e(String msg) {

    }

    @Override
    public void e(Marker marker, String msg) {

    }

    @Override
    public void e(String format, Object... arguments) {

    }

    @Override
    public void e(Marker marker, String format, Object... arguments) {

    }

    @Override
    public void e(String msg, Throwable t) {

    }

    @Override
    public void e(String msg, Object arg, Throwable t) {

    }

    @Override
    public void e(Marker marker, String msg, Throwable t) {

    }

    @Override
    public void e(Marker marker, String msg, Object arg, Throwable t) {

    }
}
