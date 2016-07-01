package io.github.cubedtear.jcubit.util;

/**
 * Generic set of two objects. Useful to return two (or more) objects as a result of a function.
 * @author Aritz Lopez
 */
public class Set2<T, U> {

	private final T t;
	private final U u;

	/**
	 * Creates a set from the given two objects.
	 * @param t The first object.
	 * @param u The second object.
     */
	public Set2(@Nullable T t, @Nullable U u) {
		this.t = t;
		this.u = u;
	}

	/**
	 * Factory method equivalent to the constructor, for better generic inference.
	 * @param v The first object.
	 * @param w The second object.
	 * @param <V> The type of the first objet.
	 * @param <W> The type of the second object.
     * @return a set containing the two given objects.
     */
	public static <V, W> Set2<V, W> of(V v, W w) {
		return new Set2<>(v, w);
	}

	/**
	 * @return the first object of the set.
     */
	@Nullable
	public T getT() {
		return t;
	}

	/**
	 * @return the second object.
	 */
	@Nullable
	public U getU() {
		return u;
	}

	/**
	 * @return a set where the first and second objects of this set are reversed.
	 */
	public Set2<U, T> inverse() {
		return new Set2<>(u, t);
	}
}
