package io.github.aritzhack.aritzh.logging;

import io.github.aritzhack.aritzh.util.Nullable;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Aritz Lopez
 */
public class OSLogger extends ALogger {

    private static final String DEFAULT_FORMAT = "%d - %L %m?t{\n%t}";

    final PrintWriter pw;
    final String format;

    public OSLogger(OutputStream os) {
        this(os, null);
    }

    public OSLogger(OutputStream os, @Nullable String format) {
        this.pw = new PrintWriter(os, true);
        this.format = format == null ? DEFAULT_FORMAT : format;
    }

    @Override
    public void t(String msg) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.TRACE, msg, this.format));
    }

    @Override
    public void t(String msg, Throwable t) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.TRACE, msg, t, this.format));
    }

    @Override
    public void d(String msg) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.DEBUG, msg, this.format));
    }

    @Override
    public void d(String msg, Throwable t) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.DEBUG, msg, t, this.format));
    }

    @Override
    public void i(String msg) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.INFO, msg, this.format));
    }

    @Override
    public void i(String msg, Throwable t) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.INFO, msg, t, this.format));
    }

    @Override
    public void w(String msg) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.WARN, msg, this.format));
    }

    @Override
    public void w(String msg, Throwable t) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.WARN, msg, t, this.format));
    }

    @Override
    public void e(String msg) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.ERROR, msg, this.format));
    }

    @Override
    public void e(String msg, Throwable t) {
        pw.println(LogFormatter.formatLogMessage(LogLevel.ERROR, msg, t, this.format));
    }
}
