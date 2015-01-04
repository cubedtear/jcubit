package io.github.aritzhack.aritzh.logging;

import com.google.common.collect.Sets;
import io.github.aritzhack.aritzh.logging.core.ALogger;
import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.core.LogLevel;
import io.github.aritzhack.aritzh.util.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Set;

/**
 * @author Aritz Lopez
 */
public class OSLogger extends ALogger {

    private static final String DEFAULT_FORMAT = "%d - %L %m?t{\n%t}";
    private static final Set<OSLogger> OS_LOGGERS = Sets.newHashSet();

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

    private static ILogger OUT_LOGGER;
    private static ILogger ERR_LOGGER;
    private final PrintStream ps;
    private final String format;
    private boolean closed = false;

    public OSLogger(OutputStream os) {
        this(os, null);
    }

    public OSLogger(OutputStream os, @Nullable String format) {
        this.ps = new PrintStream(os, true);
        this.format = format == null ? DEFAULT_FORMAT : format;
        OS_LOGGERS.add(this);
    }

    public static OSLogger getFileLogger(String path) throws FileNotFoundException {
        return getFileLogger(path, null);
    }

    public static OSLogger getFileLogger(String path, String format) throws FileNotFoundException {
        return getFileLogger(new File(path), format);
    }

    public static OSLogger getFileLogger(File file, String format) throws FileNotFoundException {
        return getFileLogger(new FileOutputStream(file), format);
    }

    public static OSLogger getFileLogger(FileOutputStream fos, String format) {
        return new OSLogger(fos, format);
    }

    public static OSLogger getFileLogger(File file) throws FileNotFoundException {
        return getFileLogger(file, null);
    }

    public static OSLogger getFileLogger(FileOutputStream fos) {
        return getFileLogger(fos, null);
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

    public boolean isClosed() {
        return this.ps == null || this.closed;
    }

    public void close() {
        this.ps.close();
        this.closed = true;
        OS_LOGGERS.remove(this);
    }
}
