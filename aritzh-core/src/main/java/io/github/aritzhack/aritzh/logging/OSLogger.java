package io.github.aritzhack.aritzh.logging;

import com.google.common.collect.Sets;
import io.github.aritzhack.aritzh.logging.core.ALogger;
import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.core.LogLevel;
import io.github.aritzhack.aritzh.util.Nullable;

import java.io.*;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
public class OSLogger extends ALogger {

	private static final String DEFAULT_FORMAT = "%d - [%t] %l?n{ %n} - %m?e{\n%e}";
	private static final Set<OSLogger> OS_LOGGERS = Sets.newHashSet();
	private static ILogger OUT_LOGGER;
	private static ILogger ERR_LOGGER;

	static {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				for (OSLogger l : OS_LOGGERS) {
					if (!l.isClosed()) {
						l.close();
					}
				}
			}
		};
		Runtime.getRuntime().addShutdownHook(new Thread(r, "Logger-closing thread"));
	}

	private final LogLevel level;
	private final String loggerName;
	private final PrintStream ps;
	private final String format;

	public OSLogger(OutputStream os, String name) {
		this(os, name, null, null);
	}

	public OSLogger(OutputStream os, String loggerName, @Nullable LogLevel level, @Nullable String format) {
		this.level = level != null ? level : LogLevel.ALL;
		this.loggerName = loggerName;
		this.format = format == null ? DEFAULT_FORMAT : format;
		this.ps = new PrintStream(os, true);
		OS_LOGGERS.add(this);
	}

	public static ILogger getSysoutLogger() {
		if (OUT_LOGGER == null) OUT_LOGGER = new OSLogger(System.out, "");
		return OUT_LOGGER;
	}

	public static ILogger getSysoutLogger(String name) {
		return new OSLogger(System.out, name);
	}

	public static ILogger getSyserrLogger() {
		if (ERR_LOGGER == null) ERR_LOGGER = new OSLogger(System.err, "");
		return ERR_LOGGER;
	}

	public static ILogger getSyserrLogger(String name) {
		return new OSLogger(System.err, name);
	}

	@Override
	public void t(String msg) {
		if (this.level.shouldBeLogged(LogLevel.TRACE))
			ps.println(LogFormatter.formatLogMessage(LogLevel.TRACE, this.format, msg, loggerName));
	}

	@Override
	public void t(String msg, Throwable t) {
		if (this.level.shouldBeLogged(LogLevel.TRACE))
			ps.println(LogFormatter.formatLogMessage(LogLevel.TRACE, this.format, msg, loggerName));
	}

	@Override
	public void d(String msg) {
		if (this.level.shouldBeLogged(LogLevel.DEBUG))
			ps.println(LogFormatter.formatLogMessage(LogLevel.DEBUG, this.format, msg, loggerName));
	}

	@Override
	public void d(String msg, Throwable t) {
		if (this.level.shouldBeLogged(LogLevel.DEBUG))
			ps.println(LogFormatter.formatLogMessage(LogLevel.DEBUG, this.format, msg, loggerName));
	}

	@Override
	public void i(String msg) {
		if (this.level.shouldBeLogged(LogLevel.INFO))
			ps.println(LogFormatter.formatLogMessage(LogLevel.INFO, this.format, msg, loggerName));
	}

	@Override
	public void i(String msg, Throwable t) {
		if (this.level.shouldBeLogged(LogLevel.INFO))
			ps.println(LogFormatter.formatLogMessage(LogLevel.INFO, this.format, msg, loggerName));
	}

	@Override
	public void w(String msg) {
		if (this.level.shouldBeLogged(LogLevel.WARN))
			ps.println(LogFormatter.formatLogMessage(LogLevel.WARN, this.format, msg, loggerName));
	}

	@Override
	public void w(String msg, Throwable t) {
		if (this.level.shouldBeLogged(LogLevel.WARN))
			ps.println(LogFormatter.formatLogMessage(LogLevel.WARN, this.format, msg, loggerName));
	}

	@Override
	public void e(String msg) {
		if (this.level.shouldBeLogged(LogLevel.ERROR))
			ps.println(LogFormatter.formatLogMessage(LogLevel.ERROR, this.format, msg, loggerName));
	}

	@Override
	public void e(String msg, Throwable t) {
		if (this.level.shouldBeLogged(LogLevel.ERROR))
			ps.println(LogFormatter.formatLogMessage(LogLevel.ERROR, this.format, msg, loggerName));
	}

	public boolean isClosed() {
		return this.ps == null || this.ps.checkError();
	}

	public void close() {
		this.ps.close();
		OS_LOGGERS.remove(this);
	}

	public static class Builder {
		private final OutputStream os;
		private final String loggerName;
		private String format = null;
		private LogLevel level = null;

		public Builder(String path, String loggerName) throws FileNotFoundException {
			this(new File(path), loggerName);
		}

		public Builder(File f, String loggerName) throws FileNotFoundException {
			this(new FileOutputStream(f), loggerName);
		}

		public Builder(OutputStream os, String loggerName) {
			this.os = os;
			this.loggerName = loggerName;
		}

		public Builder setFormat(String format) {
			this.format = format;
			return this;
		}

		public Builder setLevel(LogLevel level) {
			this.level = level;
			return this;
		}

		public OSLogger build() {
			return new OSLogger(os, loggerName, level, format);
		}
	}
}
