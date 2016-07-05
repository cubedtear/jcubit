package io.github.cubedtear.jcubit.logging;

import com.google.common.collect.Sets;
import io.github.cubedtear.jcubit.logging.core.ALogger;
import io.github.cubedtear.jcubit.logging.core.ILogger;
import io.github.cubedtear.jcubit.logging.core.LogLevel;
import io.github.cubedtear.jcubit.util.API;
import io.github.cubedtear.jcubit.util.Nullable;

import java.io.*;
import java.util.Set;

/**
 * Logger implementation that writes to an {@link OutputStream}.
 * Note: The output streams are closed automatically with a shutdown hook, so there is no need to close them manually.
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

    /**
     * Creates a logger with the given name that will write to the given output stream.
     * @param os The output stream.
     * @param name The name of the logger.
     */
    public OSLogger(OutputStream os, String name) {
        this(os, name, null, null);
    }

    /**
     * Creates a logger with the given name that will write messages with level {@link LogLevel#INFO INFO} and below to
     * the first output stream, and messages with levels {@link LogLevel#WARN WARN} and {@link LogLevel#ERROR ERROR} to
     * the second one.
     * @param outputStream The output stream for messages with level {@link LogLevel#INFO INFO} and below.
     * @param errorStream The output stream for messages with level above {@link LogLevel#INFO INFO}.
     * @param name The name of the logger.
     */
    public OSLogger(OutputStream outputStream, OutputStream errorStream, String name) {
        this(outputStream, errorStream, name, null, null);
    }

    /**
     * Creates a logger with the given name, level, and log message format, that will write all its messages
     * to the given OutputStream.
     * @param os The output stream.
     * @param loggerName The name of the logger.
     * @param level The level below which messages will be ignored.
     * @param format The log format. See {@link LogFormatter}.
     * @see LogFormatter
     */
    public OSLogger(OutputStream os, String loggerName, @Nullable LogLevel level, @Nullable String format) {
        this(os, os, loggerName, level, format);
    }

    /**
     * Creates a logger with the given name, level, and log message format, that will write messages with level
     * {@link LogLevel#INFO INFO} and below to the first output stream, and messages with levels
     * {@link LogLevel#WARN WARN} and {@link LogLevel#ERROR ERROR} to the second one.
     * @param outputStream The output stream for messages with level {@link LogLevel#INFO INFO} and below.
     * @param errorStream The output stream for messages with level above {@link LogLevel#INFO INFO}.
     * @param loggerName The name of the logger.
     * @param level The level below which messages will be ignored.
     * @param format The log format. See {@link LogFormatter}.
     * @see LogFormatter
     */
    public OSLogger(OutputStream outputStream, OutputStream errorStream, String loggerName, @Nullable LogLevel level, @Nullable String format) {
        this.level = level != null ? level : LogLevel.ALL;
        this.loggerName = loggerName;
        this.format = format == null ? DEFAULT_FORMAT : format;
        this.outputStream = new PrintStream(outputStream, true);
        this.errorStream = new PrintStream(errorStream, true);
        OS_LOGGERS.add(this);
    }

    /**
     * Returns a cached logger that will print messages with level {@link LogLevel#INFO INFO} and below to
     * standard output, and messages with level above {@link LogLevel#INFO INFO} to standard error.
     * @return A logger that prints to the standard channels.
     */
    @API
    public static ILogger getStdLogger() {
        if (STD_LOGGER == null) STD_LOGGER = new OSLogger(System.out, System.err, "");
        return STD_LOGGER;
    }

    /**
     * Returns a logger with the given name that will print messages with level {@link LogLevel#INFO INFO} and below to
     * standard output, and messages with level above {@link LogLevel#INFO INFO} to standard error.
     * Guaranteed to create a new logger each time.
     * @param name The name of the logger.
     * @return A logger that prints to the standard channels.
     */
    @API
    public static ILogger getStdLogger(String name) {
        return new OSLogger(System.out, System.err, name);
    }

    /**
     * Returns a cached logger that prints all the messages to standard output.
     * @return a logger that prints to standard output.
     */
    @API
    public static ILogger getSysoutLogger() {
        if (OUT_LOGGER == null) OUT_LOGGER = new OSLogger(System.out, "");
        return OUT_LOGGER;
    }

    /**
     * Returns a logger with the given name that prints all the messages to standard output.
     * Guaranteed to create a new logger each time.
     * @param name The name of the logger.
     * @return a logger that prints to standard output.
     */
    @API
    public static ILogger getSysoutLogger(String name) {
        return new OSLogger(System.out, name);
    }

    /**
     * Returns a cached logger that prints all the messages to standard error.
     * @return a logger that prints to standard error.
     */
    @API
    public static ILogger getSyserrLogger() {
        if (ERR_LOGGER == null) ERR_LOGGER = new OSLogger(System.err, "");
        return ERR_LOGGER;
    }

    /**
     * Returns a logger with the given name that prints all the messages to standard error.
     * Guaranteed to create a new logger each time.
     * @param name The name of the logger.
     * @return a logger that prints to standard error.
     */
    @API
    public static ILogger getSyserrLogger(String name) {
        return new OSLogger(System.err, name);
    }

    /**
     * Sets the level of the current logger. All messages below that level will be ignored.
     * @param level The level.
     * @return {@code this}, for using the builder pattern.
     */
    @API
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

    /**
     * @return whether the underlying streams are closed.
     * Note: the streams are closed automatically with a shutdown hook, so there is no need to do that manually.
     */
    public boolean isClosed() {
        return this.outputStream == null || this.errorStream == null || this.outputStream.checkError() || this.errorStream.checkError();
    }

    /**
     * Closes the underlying output stream.
     */
    @API
    public void close() {
        this.close(true);
    }

    private void close(boolean remove) {
        this.outputStream.close();
        this.errorStream.close();
        if (remove) OS_LOGGERS.remove(this);
    }

    /**
     * Builder for {@link OSLogger OSLoggers}.
     */
    public static class Builder {
        private final OutputStream outputStream;
        private final OutputStream errorStream;
        private final String loggerName;
        private String format = null;
        private LogLevel level = null;

        /**
         * Creates a builder for a OSLogger with the given name that prints to the file with the given path.
         * @param path The path of the file the logger will output to.
         * @param loggerName The name of the logger.
         * @throws FileNotFoundException If the file could not be found.
         */
        @API
        public Builder(String path, String loggerName) throws FileNotFoundException {
            this(new File(path), loggerName);
        }

        /**
         * Creates a builder for a OSLogger with the given name that prints messages with level
         * {@link LogLevel#INFO INFO} and below to the file with the first path, and the rest to the file
         * with the second path.
         * @param outputFilePath The path to the file to log messages with level {@link LogLevel#INFO INFO} and below.
         * @param errorFilePath The path to the file to log messages with level above {@link LogLevel#INFO INFO}.
         * @param loggerName The name of the logger
         * @throws FileNotFoundException If any file could not be found.
         */
        @API
        public Builder(String outputFilePath, String errorFilePath, String loggerName) throws FileNotFoundException {
            this(new File(outputFilePath), new File(errorFilePath), loggerName);
        }

        /**
         * Creates a builder for a OSLogger with the given name that prints to the given file.
         * @param f The file the logger will output to.
         * @param loggerName The name of the logger
         * @throws FileNotFoundException If the file could not be found.
         */
        public Builder(File f, String loggerName) throws FileNotFoundException {
            this(new FileOutputStream(f), loggerName);
        }

        /**
         * Creates a builder for a OSLogger with the given name that prints messages with level
         * {@link LogLevel#INFO INFO} and below to the first file, and the rest to the second file.
         * @param outputFile The file to log messages with level {@link LogLevel#INFO INFO} and below.
         * @param errorFile The file to log messages with level above {@link LogLevel#INFO INFO}.
         * @param loggerName The name of the logger.
         * @throws FileNotFoundException If any of the file could not be found.
         */
        public Builder(File outputFile, File errorFile, String loggerName) throws FileNotFoundException {
            this(new FileOutputStream(outputFile), new FileOutputStream(errorFile), loggerName);
        }

        /**
         * Creates a builder for a OSLogger with the given name that prints to the given output stream.
         * @param outputStream The file the logger will output to.
         * @param loggerName The name of the logger
         */
        public Builder(OutputStream outputStream, String loggerName) {
            this(outputStream, outputStream, loggerName);
        }

        /**
         * Creates a builder for a OSLogger with the given name that prints messages with level
         * {@link LogLevel#INFO INFO} and below to the first output stream, and the rest to the second output stream.
         * @param outputStream The output stream messages with level {@link LogLevel#INFO INFO} and below will be logged to.
         * @param errorStream The output stream messages with level above {@link LogLevel#INFO INFO} will be logged to.
         * @param loggerName The name of the logger.
         */
        public Builder(OutputStream outputStream, OutputStream errorStream, String loggerName) {
            this.outputStream = outputStream;
            this.errorStream = errorStream;
            this.loggerName = loggerName;
        }

        /**
         * Sets the format of the logger.
         * @param format The format string
         * @return {@code this}, for the builder pattern.
         * @see LogFormatter
         */
        public Builder setFormat(String format) {
            this.format = format;
            return this;
        }

        /**
         * Sets the level of the logger. Messages with level below the given level will not be logged.
         * @param level The level.
         * @return {@code this}, for the builder pattern.
         */
        public Builder setLevel(LogLevel level) {
            this.level = level;
            return this;
        }

        /**
         * @return An OSLogger with the given parameters.
         */
        public OSLogger build() {
            return new OSLogger(outputStream, errorStream, loggerName, level, format);
        }
    }
}
