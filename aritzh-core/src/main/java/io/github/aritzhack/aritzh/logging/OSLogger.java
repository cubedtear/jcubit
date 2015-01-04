package io.github.aritzhack.aritzh.logging;

import io.github.aritzhack.aritzh.logging.core.ALogger;
import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.core.LogLevel;
import io.github.aritzhack.aritzh.util.Nullable;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Aritz Lopez
 */
public class OSLogger extends ALogger {

    private static final String DEFAULT_FORMAT = "%d - %L %m?t{\n%t}";

    private static ILogger OUT_LOGGER;
    private static ILogger ERR_LOGGER;
    final PrintStream ps;
    final String format;

    public OSLogger(OutputStream os) {
        this(os, null);
    }

    public OSLogger(OutputStream os, @Nullable String format) {
        this.ps = new PrintStream(os, true);
        this.format = format == null ? DEFAULT_FORMAT : format;
    }

    public static ILogger getSysoutLogger() {
        if (OUT_LOGGER == null) OUT_LOGGER = new OSLogger(System.out);
        return OUT_LOGGER;
    }

    public static ILogger getSyserrLogger() {
        if (ERR_LOGGER == null) ERR_LOGGER = new OSLogger(System.err);
        return ERR_LOGGER;
    }

    @Override
    public void t(String msg) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.TRACE, msg, this.format));
    }

    @Override
    public void t(String msg, Throwable t) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.TRACE, msg, t, this.format));
    }

    @Override
    public void d(String msg) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.DEBUG, msg, this.format));
    }

    @Override
    public void d(String msg, Throwable t) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.DEBUG, msg, t, this.format));
    }

    @Override
    public void i(String msg) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.INFO, msg, this.format));
    }

    @Override
    public void i(String msg, Throwable t) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.INFO, msg, t, this.format));
    }

    @Override
    public void w(String msg) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.WARN, msg, this.format));
    }

    @Override
    public void w(String msg, Throwable t) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.WARN, msg, t, this.format));
    }

    @Override
    public void e(String msg) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.ERROR, msg, this.format));
    }

    @Override
    public void e(String msg, Throwable t) {
        ps.println(LogFormatter.formatLogMessage(LogLevel.ERROR, msg, t, this.format));
    }
}
