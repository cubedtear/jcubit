package io.github.cubedtear.jcubit.logging;

import com.google.common.collect.Sets;
import io.github.cubedtear.jcubit.logging.core.ALogger;
import io.github.cubedtear.jcubit.logging.core.ILogger;
import io.github.cubedtear.jcubit.logging.core.LogLevel;
import io.github.cubedtear.jcubit.util.Nullable;

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
    private static ILogger STD_LOGGER;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                for (OSLogger l : OS_LOGGERS) if (!l.isClosed()) l.close(false);
            }
        }, "Logger-closing thread"));
    }

    private LogLevel level;
    private final String loggerName;
    private final PrintStream outputStream;
    private final PrintStream errorStream;
    private final String format;

    public OSLogger(OutputStream os, String name) {
        this(os, name, null, null);
    }

    public OSLogger(OutputStream outputStream, OutputStream errorStream, String name) {
        this(outputStream, errorStream, name, null, null);
    }

    public OSLogger(OutputStream os, String loggerName, @Nullable LogLevel level, @Nullable String format) {
        this(os, os, loggerName, level, format);
    }

    public OSLogger(OutputStream outputStream, OutputStream errorStream, String loggerName, @Nullable LogLevel level, @Nullable String format) {
        this.level = level != null ? level : LogLevel.ALL;
        this.loggerName = loggerName;
        this.format = format == null ? DEFAULT_FORMAT : format;
        this.outputStream = new PrintStream(outputStream, true);
        this.errorStream = new PrintStream(errorStream, true);
        OS_LOGGERS.add(this);
    }

    public static ILogger getStdLogger() {
        if (STD_LOGGER == null) STD_LOGGER = new OSLogger(System.out, System.err, "");
        return STD_LOGGER;
    }

    public static ILogger getStdLogger(String name) {
        return new OSLogger(System.out, System.err, name);
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

    public OSLogger setLevel(LogLevel level) {
        this.level = level;
        return this;
    }

    @Override
    public void t(String msg) {
        if (this.level.shouldBeLogged(LogLevel.TRACE))
            outputStream.println(LogFormatter.formatLogMessage(LogLevel.TRACE, this.format, msg, loggerName));
    }

    @Override
    public void t(String msg, Throwable t) {
        if (this.level.shouldBeLogged(LogLevel.TRACE))
            outputStream.println(LogFormatter.formatLogMessage(LogLevel.TRACE, this.format, msg, t, loggerName));
    }

    @Override
    public void d(String msg) {
        if (this.level.shouldBeLogged(LogLevel.DEBUG))
            outputStream.println(LogFormatter.formatLogMessage(LogLevel.DEBUG, this.format, msg, loggerName));
    }

    @Override
    public void d(String msg, Throwable t) {
        if (this.level.shouldBeLogged(LogLevel.DEBUG))
            outputStream.println(LogFormatter.formatLogMessage(LogLevel.DEBUG, this.format, msg, t, loggerName));
    }

    @Override
    public void i(String msg) {
        if (this.level.shouldBeLogged(LogLevel.INFO))
            outputStream.println(LogFormatter.formatLogMessage(LogLevel.INFO, this.format, msg, loggerName));
    }

    @Override
    public void i(String msg, Throwable t) {
        if (this.level.shouldBeLogged(LogLevel.INFO))
            outputStream.println(LogFormatter.formatLogMessage(LogLevel.INFO, this.format, msg, t, loggerName));
    }

    @Override
    public void w(String msg) {
        if (this.level.shouldBeLogged(LogLevel.WARN))
            errorStream.println(LogFormatter.formatLogMessage(LogLevel.WARN, this.format, msg, loggerName));
    }

    @Override
    public void w(String msg, Throwable t) {
        if (this.level.shouldBeLogged(LogLevel.WARN))
            errorStream.println(LogFormatter.formatLogMessage(LogLevel.WARN, this.format, msg, t, loggerName));
    }

    @Override
    public void e(String msg) {
        if (this.level.shouldBeLogged(LogLevel.ERROR))
            errorStream.println(LogFormatter.formatLogMessage(LogLevel.ERROR, this.format, msg, loggerName));
    }

    @Override
    public void e(String msg, Throwable t) {
        if (this.level.shouldBeLogged(LogLevel.ERROR))
            errorStream.println(LogFormatter.formatLogMessage(LogLevel.ERROR, this.format, msg, t, loggerName));
    }

    public boolean isClosed() {
        return this.outputStream == null || this.errorStream == null || this.outputStream.checkError() || this.errorStream.checkError();
    }

    public void close() {
        this.close(true);
    }

    private void close(boolean remove) {
        this.outputStream.close();
        this.errorStream.close();
        if (remove) OS_LOGGERS.remove(this);
    }

    public static class Builder {
        private final OutputStream outputStream;
        private final OutputStream errorStream;
        private final String loggerName;
        private String format = null;
        private LogLevel level = null;

        public Builder(String path, String loggerName) throws FileNotFoundException {
            this(new File(path), loggerName);
        }

        public Builder(String outputFilePath, String errorFilePath, String loggerName) throws FileNotFoundException {
            this(new File(outputFilePath), new File(errorFilePath), loggerName);
        }

        public Builder(File f, String loggerName) throws FileNotFoundException {
            this(new FileOutputStream(f), loggerName);
        }

        public Builder(File outputFile, File errorFile, String loggerName) throws FileNotFoundException {
            this(new FileOutputStream(outputFile), new FileOutputStream(errorFile), loggerName);
        }

        public Builder(OutputStream outputStream, String loggerName) {
            this(outputStream, outputStream, loggerName);
        }

        public Builder(OutputStream outputStream, OutputStream errorStream, String loggerName) {
            this.outputStream = outputStream;
            this.errorStream = errorStream;
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
            return new OSLogger(outputStream, errorStream, loggerName, level, format);
        }
    }
}
