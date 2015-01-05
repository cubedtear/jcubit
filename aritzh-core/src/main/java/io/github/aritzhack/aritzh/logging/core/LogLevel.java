package io.github.aritzhack.aritzh.logging.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.aritzhack.aritzh.util.Nullable;

import java.util.Map;

public enum LogLevel implements Comparable<LogLevel> {
    NONE(0, "n"), TRACE(1, "t"), DEBUG(2, "d"), INFO(3, "i"), WARN(4, "w"), ERROR(5, "e"), ALL(6, "a");

    public static final int maxNameLength;

    static {
        Map<String, LogLevel> map = Maps.newHashMap();
        int length = 0;
        for (LogLevel l : LogLevel.values()) {
            map.put(l.name(), l);
            map.put(l.getTag(), l);
            map.put(l.getShorthand(), l);
            length = Math.max(length, l.name().length());
        }
        levels = ImmutableMap.copyOf(map);
        maxNameLength = length;
    }

    private static final Map<String, LogLevel> levels;
    private final String tag;
    private final String shorthand;
    private final int level;

    LogLevel(int level, String s) {
        this.tag = "[" + this.name() + "]";
        this.shorthand = s;
        this.level = level;
    }

    @Nullable
    public static LogLevel getLevel(String level) {
        return levels.containsKey(level.toLowerCase()) ? levels.get(level.toLowerCase()) : null;
    }

    public boolean shouldBeLogged(LogLevel log) {
        return this.level >= log.level;
    }

    public String getTag() {
        return this.tag;
    }

    public String getShorthand() {
        return shorthand;
    }

}
