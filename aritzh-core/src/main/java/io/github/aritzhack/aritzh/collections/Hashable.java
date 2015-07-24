package io.github.aritzhack.aritzh.collections;

/**
 * @author Aritz Lopez
 */
public class Hashable<T> {

	private final T value;
	private final Hasher<T> hasher;

	private Hashable(T value, Hasher<T> hasher) {
		this.value = value;
		this.hasher = hasher;
	}

	public static <T> Hashable<T> of(T value, Hasher<T> hasher) {
		return new Hashable<>(value, hasher);
	}

	@SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
	@Override
	public boolean equals(Object o) {
		return this.hasher.equals(value, o);
	}

	@Override
	public int hashCode() {
		return hasher.hashCode(value);
	}

	public T get() {
		return value;
	}
}
