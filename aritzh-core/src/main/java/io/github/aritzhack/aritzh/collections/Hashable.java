package io.github.aritzhack.aritzh.collections;

/**
 * @author Aritz Lopez
 */
public class Hashable<T> {

	private final T value;
	private final Hasher<T> hasher;

	public Hashable(T value, Hasher<T> hasher) {
		this.value = value;
		this.hasher = hasher;
	}

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
