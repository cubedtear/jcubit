package io.github.cubedtear.jcubit.collections;

/**
 * Used by the {@link Hashable} wrapper to calculate hashCodes of objects.
 * @author Aritz Lopez
 */
public interface Hasher<T> {
	/**
	 * Calculates the hashCode of the given object. This method should meet the contract of {@link Object#hashCode()}
	 * @param t The object for which to calculate the hashCode.
	 * @return The hashCode of the given object.
     */
	int hashCode(T t);

	/**
	 * Checks whether {@code t} and {@code o} are equal. This method should meet the contract of {@link Object#equals(Object)}
	 * @param t The first object.
	 * @param o The second object.
     * @return Whether t and o are said to be equal.
     */
	boolean equals(T t, Object o);
}
