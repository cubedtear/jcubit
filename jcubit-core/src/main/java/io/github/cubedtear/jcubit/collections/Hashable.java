package io.github.cubedtear.jcubit.collections;

/**
 * Wrapper for objects that do not implement a good hashCode method, so that they can still
 * be added to collections such as HashMaps.
 * @author Aritz Lopez
 */
public class Hashable<T> {

	private final T value;
	private final Hasher<T> hasher;

	private Hashable(T value, Hasher<T> hasher) {
		this.value = value;
		this.hasher = hasher;
	}

	/**
	 * Factory method that creates a Hashable for the given object. The hash code is calculated
	 * each time it is asked for by using the given Hasher.
	 * @param value The object wrapped by this.
	 * @param hasher What calculates the hashCode for the object.
	 * @param <T> The type of the object, and the type accepted by the hasher.
     * @return A Hashable that contains the given object, and uses the given hasher to calculate the hashCode.
     */
	public static <T> Hashable<T> of(T value, Hasher<T> hasher) {
		return new Hashable<>(value, hasher);
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {
		return this == o || o instanceof Hashable && this.hasher.equals(value, ((Hashable)o).get());
	}

	@Override
	public int hashCode() {
		return hasher.hashCode(value);
	}

	/**
	 * Gets the wrapped object.
	 * @return the wrapped object.
     */
	public T get() {
		return value;
	}
}
