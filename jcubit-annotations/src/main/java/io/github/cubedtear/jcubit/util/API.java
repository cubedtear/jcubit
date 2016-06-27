package io.github.cubedtear.jcubit.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to suppress warnings by smart IDEs ;)
 * @author Aritz Lopez
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface API {
}
