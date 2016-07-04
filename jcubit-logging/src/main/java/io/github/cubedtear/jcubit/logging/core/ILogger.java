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

package io.github.cubedtear.jcubit.logging.core;

/**
 * Interface that contains the minimum operations all loggers should have.
 * @author Aritz Lopez
 */
public interface ILogger {

	/**
	 * Logs a message with the log level {@link LogLevel#TRACE} (the lowest level).
	 * Used usually to log when a method starts and ends, and fine debugging messages.
	 * @param msg The message
     */
	void t(String msg);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#TRACE} (the lowest level).
	 * Used usually to log when a method starts and ends, and fine debugging messages.
	 * @param format The format of the message.
	 * @param arguments The arguments to insert into the message.
	 */
	void t(String format, Object... arguments);

	/**
	 * Logs a message with the log level {@link LogLevel#TRACE} (the lowest level) and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log when a method starts and ends, and fine debugging messages.
	 * @param msg The message
	 * @param t The throwable
	 */
	void t(String msg, Throwable t);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#TRACE} (the lowest level) and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log when a method starts and ends, and fine debugging messages.
	 * @param msg The format of the message.
	 * @param t The throwable
	 * @param args The arguments to insert into the message.
	 */
	void t(String msg, Throwable t, Object... args);

	/**
	 * Logs a message with the log level {@link LogLevel#DEBUG}.
	 * Used usually to log information that is useful for debugging, but will not get to production.
	 * @param msg The message
	 */
	void d(String msg);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#DEBUG}.
	 * Used usually to log information that is useful for debugging, but will not get to production.
	 * @param format The format of the message.
	 * @param arguments The arguments to insert into the message.
	 */
	void d(String format, Object... arguments);

	/**
	 * Logs a message with the log level {@link LogLevel#DEBUG} and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log information that is useful for debugging, but will not get to production.
	 * @param msg The message
	 * @param t The throwable
	 */
	void d(String msg, Throwable t);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#DEBUG}.
	 * Used usually to log information that is useful for debugging, but will not get to production.
	 * @param msg The format of the message.
	 * @param t The throwable
	 * @param args The arguments to insert into the message.
	 */
	void d(String msg, Throwable t, Object... args);

	/**
	 * Logs a message with the log level {@link LogLevel#INFO}.
	 * Used usually to log information that is relevant in production code, but is not any kind of warning or error.
	 * @param msg The message
	 */
	void i(String msg);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#INFO}.
	 * Used usually to log information that is relevant in production code, but is not any kind of warning or error.
	 * @param format The format of the message.
	 * @param arguments The arguments to insert into the message.
	 */
	void i(String format, Object... arguments);

	/**
	 * Logs a message with the log level {@link LogLevel#INFO} and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log information that is relevant in production code, but is not any kind of warning or error.
	 * @param msg The message
	 * @param t The throwable
	 */
	void i(String msg, Throwable t);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#INFO} and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log information that is relevant in production code, but is not any kind of warning or error.
	 * @param msg The format of the message.
	 * @param t The throwable
	 * @param args The arguments to insert into the message.
	 */
	void i(String msg, Throwable t, Object... args);

	/**
	 * Logs a message with the log level {@link LogLevel#WARN}.
	 * Used usually to log information that could cause issues, but is not (or may be) an issue in itself.
	 * @param msg The message
	 */
	void w(String msg);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#WARN}.
	 * Used usually to log information that could cause issues, but is not (or may be) an issue in itself.
	 * @param format The format of the message.
	 * @param arguments The arguments to insert into the message.
	 */
	void w(String format, Object... arguments);

	/**
	 * Logs a message with the log level {@link LogLevel#WARN} and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log information that could cause issues, but is not (or may be) an issue in itself.
	 * @param msg The message
	 * @param t The throwable
	 */
	void w(String msg, Throwable t);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#WARN} and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log information that could cause issues, but is not (or may be) an issue in itself.
	 * @param msg The format of the message.
	 * @param t The throwable
	 * @param args The arguments to insert into the message.
	 */
	void w(String msg, Throwable t, Object... args);

	/**
	 * Logs a message with the log level {@link LogLevel#ERROR}.
	 * Used usually to log errors, either caught or uncaught.
	 * @param msg The message
	 */
	void e(String msg);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#ERROR}.
	 * Used usually to log errors, either caught or uncaught.
	 * @param format The format of the message.
	 * @param arguments The arguments to insert into the message.
	 */
	void e(String format, Object... arguments);

	/**
	 * Logs a message with the log level {@link LogLevel#ERROR} and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log errors, either caught or uncaught.
	 * @param msg The message
	 * @param t The throwable
	 */
	void e(String msg, Throwable t);

	/**
	 * Logs a formatted message with the log level {@link LogLevel#ERROR} and
	 * prints relevant information related to the given throwable (usually the stack trace).
	 * Used usually to log errors, either caught or uncaught.
	 * @param msg The format of the message.
	 * @param t The throwable
	 * @param args The arguments to insert into the message.
	 */
	void e(String msg, Throwable t, Object... args);
}
