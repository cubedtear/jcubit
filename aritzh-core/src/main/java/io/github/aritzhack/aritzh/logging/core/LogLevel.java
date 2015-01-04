package io.github.aritzhack.aritzh.logging.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.aritzhack.aritzh.util.Nullable;

import java.util.Map;

public enum LogLevel {
    TRACE("t"), DEBUG("d"), INFO("i"), WARN("w"), ERROR("e");

    private final String tag;
    private final String shorthand;
    private static final Map<String, LogLevel> levels;
    public static final int maxNameLength;

    static {
        Map<String, LogLevel> map = Maps.newHashMap();
        int length = 0;
        for(LogLevel l : LogLevel.values()) {
            map.put(l.name(), l);
            map.put(l.getTag(), l);
            map.put(l.getShorthand(), l);
            length = Math.max(length, l.name().length());
        }
        levels = ImmutableMap.copyOf(map);
        maxNameLength = length;
    }

    LogLevel(String s) {
        this.tag = "[" + this.name() + "]";
        this.shorthand = s;
    }

    public String getTag() {
        return this.tag;
    }

    public String getShorthand() {
        return shorthand;
    }

    @Nullable
    public static LogLevel getLevel(String level) {
        return levels.containsKey(level.toLowerCase()) ? levels.get(level.toLowerCase()) : null;
    }

}
