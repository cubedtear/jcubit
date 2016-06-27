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
 * @author Aritz Lopez
 */
public interface ILogger {

	void t(String msg);

	void t(String format, Object... arguments);

	void t(String msg, Throwable t);

	void t(String msg, Throwable t, Object... args);

	void d(String msg);

	void d(String format, Object... arguments);

	void d(String msg, Throwable t);

	void d(String msg, Throwable t, Object... args);

	void i(String msg);

	void i(String format, Object... arguments);

	void i(String msg, Throwable t);

	void i(String msg, Throwable t, Object... args);

	void w(String msg);

	void w(String format, Object... arguments);

	void w(String msg, Throwable t);

	void w(String msg, Throwable t, Object... args);

	void e(String msg);

	void e(String format, Object... arguments);

	void e(String msg, Throwable t);

	void e(String msg, Throwable t, Object... args);
}
