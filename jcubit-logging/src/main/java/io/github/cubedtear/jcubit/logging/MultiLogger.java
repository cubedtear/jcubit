/*
 * Copyright 2015 Aritz Lopez
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

package io.github.cubedtear.jcubit.logging;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import io.github.cubedtear.jcubit.logging.core.ALogger;
import io.github.cubedtear.jcubit.logging.core.ILogger;
import io.github.cubedtear.jcubit.util.API;

import java.util.Set;

/**
 * Logger that takes together different loggers. Useful for things like logging everything to the console,
 * but only warnings and errors to a file.
 * @author Aritz Lopez
 */
@API
public class MultiLogger extends ALogger {

	private final Set<ILogger> loggers;

	/**
	 * Creates a multi-logger from the given loggers. Note: These loggers may be MultiLogger-s themselves.
	 * @param loggers The loggers.
     */
	@API
	public MultiLogger(ILogger... loggers) {
		this.loggers = Sets.newHashSet(loggers);
	}

	/**
	 * Adds another logger to this multi-logger.
	 * @param logger The logger to add.
     */
	@API
	public void addLogger(ILogger logger) {
		this.loggers.add(logger);
	}

	/**
	 * Ads the given logger to this multi-logger.
	 * @param loggers The loggers to add.
     */
	@API
	public void addLoggers(ILogger... loggers) {
		this.loggers.addAll(Sets.newHashSet(loggers));
	}

	/**
	 * @return an immutable copy of the loggers this multi-logger logs to.
     */
	@API
	public Set<ILogger> getLoggers() {
		return ImmutableSet.copyOf(loggers);
	}

	@Override
	public void t(String msg) {
		for (ILogger l : this.loggers) {
			l.t(msg);
		}
	}

	@Override
	public void t(String msg, Throwable t) {
		for (ILogger l : this.loggers) {
			l.t(msg, t);
		}
	}

	@Override
	public void d(String msg) {
		for (ILogger l : this.loggers) {
			l.d(msg);
		}
	}

	@Override
	public void d(String msg, Throwable t) {
		for (ILogger l : this.loggers) {
			l.d(msg, t);
		}
	}

	@Override
	public void i(String msg) {
		for (ILogger l : this.loggers) {
			l.i(msg);
		}
	}

	@Override
	public void i(String msg, Throwable t) {
		for (ILogger l : this.loggers) {
			l.i(msg, t);
		}
	}

	@Override
	public void w(String msg) {
		for (ILogger l : this.loggers) {
			l.w(msg);
		}
	}

	@Override
	public void w(String msg, Throwable t) {
		for (ILogger l : this.loggers) {
			l.w(msg, t);
		}
	}

	@Override
	public void e(String msg) {
		for (ILogger l : this.loggers) {
			l.e(msg);
		}
	}

	@Override
	public void e(String msg, Throwable t) {
		for (ILogger l : this.loggers) {
			l.e(msg, t);
		}
	}
}
