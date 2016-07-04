package io.github.cubedtear.jcubit.logging.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import io.github.cubedtear.jcubit.util.API;
import io.github.cubedtear.jcubit.util.Nullable;

import java.util.Map;

/**
 * Logging levels. Used to know when a message should be logged, and when not.
 * @author Aritz Lopez
 */
public enum LogLevel {
	/**
	 * Loggers with this level will log all messages. Cannot be the level of a message.
	 */
	ALL(0, "a"),
	/**
	 * Loggers with this level will also log all levels. Messages of this level should be of
	 * the lowest significance, and are usually fine debug messages.
	 */
	TRACE(1, "t"),
	/**
	 * Loggers with this level will log all but {@link LogLevel#TRACE TRACE} messages.
	 * Messages of this level should not be printed in production code, and should be used
	 * to help debug the code.
	 */
	DEBUG(2, "d"),
	/**
	 * Loggers with this level will log all messages except {@link LogLevel#TRACE TRACE} and
	 * {@link LogLevel#DEBUG DEBUG}.
	 * Messages of this level will get to production code, but should be used only for informational messages,
	 * not for warnings, potential errors or errors.
	 */
	INFO(3, "i"),
	/**
	 * Loggers at this level will log only messages with this level and {@link LogLevel#ERROR ERROR}.
	 * Messages of this level should be used for potential errors, or errors that are not very important and
	 * can be fixed.
	 */
	WARN(4, "w"),
	/**
	 * Loggers at this level will log only messages at this level.
	 * Messages of this level should be used for very serious errors, or unrecoverable ones.
	 */
	ERROR(5, "e"),
	/**
	 * Loggers at this level will not print anything.
	 * Cannot be the level of a message.
	 */
	@API
	NONE(6, "n");

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

	/**
	 * Returns the level that corresponds to the given string.
	 * The string is taken as case insensitive, and may be the whole name of the level,
	 * or the first letter.
	 * @param level The level to parse.
	 * @return The level equivalent to the given string, or null, if none is found.
     */
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

	/**
	 * @return the tag of this level, which is usually what is logged along with the message.
	 * Usually is the name of the level inside square brackets.
     */
	public String getTag() {
		return this.tag;
	}

	/**
	 * @return The shorthand for this level. Usually the first letter of its name.
     */
	public String getShorthand() {
		return shorthand;
	}



}
