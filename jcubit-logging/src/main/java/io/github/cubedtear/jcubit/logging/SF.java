package io.github.cubedtear.jcubit.logging;

import io.github.cubedtear.jcubit.util.NotNull;
import io.github.cubedtear.jcubit.util.Nullable;

/**
 * Formats strings, replacing {} by arguments.
 * @author Aritz Lopez
 */
public class SF {
    private static final char TOKEN_START = '{';
    private static final char TOKEN_END = '}';
    private static final String NULL_STRING = "{NULL}";

    /**
     * Formats the given string by replacing <i>{}</i> by the given arguments.
     * Order is preserved: the first occurrence of <i>{}</i> will be replaced by {@code args[0]}, and so on.
     * If there are more <i>{}</i> tokens than arguments given, the token will be replaced by <i>{NULL}</i>.
     * @param format The format string.
     * @param args The arguments to replace in the string.
     * @return The formatted string.
     * */
    @NotNull
    public static String f(@Nullable String format, @Nullable Object... args) {
        if (format == null || format.length() == 0 || "".equals(format)) return "";
        char[] chars = format.toCharArray();

        StringBuilder b = new StringBuilder(format.length());

        int token = 0;

        for (int i = 0; i < chars.length; i++) {
            if (i < chars.length - 1 && chars[i] == TOKEN_START && chars[i + 1] == TOKEN_END) {
                if (args == null || token >= args.length) b.append(NULL_STRING);
                else b.append(args[token++]);
                i++;
            } else {
                b.append(chars[i]);
            }
        }

        return b.toString();
    }
}
