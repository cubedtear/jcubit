package io.github.aritzhack.aritzh.logging.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.aritzhack.aritzh.util.Nullable;

import java.util.Map;

public enum LogLevel implements Comparable<LogLevel> {
	NONE(0, "n"), TRACE(1, "t"), DEBUG(2, "d"), INFO(3, "i"), WARN(4, "w"), ERROR(5, "e"), ALL(6, "a");

	public static final int maxNameLength;
	private static final Map<String, LogLevel> levels;

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

	/**
	 * Returns whether a message with level {@code log} should be logged if
	 * the logger is set with {@code this} level.
	 * @param log The level of the message.
	 * @return {@code true} iff a message with level log should be logged with this level.
	 */
	public boolean shouldBeLogged(LogLevel log) {
		return this.level <= log.level;
	}

	public String getTag() {
		return this.tag;
	}

	public String getShorthand() {
		return shorthand;
	}

}
