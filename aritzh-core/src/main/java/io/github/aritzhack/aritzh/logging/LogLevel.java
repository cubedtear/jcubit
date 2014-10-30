package io.github.aritzhack.aritzh.logging;

public enum LogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR;

    private final String tag;

    LogLevel() {
        this.tag = "[" + this.name() + "]";
    }

    public String getTag() {
        return this.tag;
    }
}
