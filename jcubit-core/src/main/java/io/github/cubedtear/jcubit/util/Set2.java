package io.github.cubedtear.jcubit.util;

/**
 * @author Aritz Lopez
 */
public class Set2<T, U> {

	private final T t;
	private final U u;

	public Set2(T t, U u) {
		this.t = t;
		this.u = u;
	}

	public static <V, W> Set2<V, W> of(V v, W w) {
		return new Set2<>(v, w);
	}

	public T getT() {
		return t;
	}

	public U getU() {
		return u;
	}

	public Set2<U, T> inverse() {
		return new Set2<>(u, t);
	}
}
