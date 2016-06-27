package io.github.cubedtear.jcubit.collections;

/**
 * @author Aritz Lopez
 */
public interface Hasher<T> {
	int hashCode(T t);

	boolean equals(T t, Object o);
}
