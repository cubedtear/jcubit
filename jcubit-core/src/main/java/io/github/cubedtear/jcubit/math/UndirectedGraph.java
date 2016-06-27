package io.github.cubedtear.jcubit.math;

import io.github.cubedtear.jcubit.collections.HashMultiBiMap;

import java.util.Set;

/**
 * @author Aritz Lopez
 */
@Deprecated
public class UndirectedGraph<T> {

	private final HashMultiBiMap<T, T> graph = HashMultiBiMap.create();

	private UndirectedGraph() {

	}

	public static <T> UndirectedGraph<T> create() {
		return new UndirectedGraph<>();
	}

	public void addEdge(T t1, T t2) {
		this.graph.put(t1, t2);
	}

	public Set<T> adjacents(T t) {
		return graph.get(t);
	}

	public int degree(T t) {
		return adjacents(t).size();
	}

	public Set<T> getVertices() {
		return graph.keys();
	}
}
