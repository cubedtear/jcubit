package io.github.cubedtear.jcubit.collections;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import io.github.cubedtear.jcubit.util.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * {@link Set Set&lt;E&gt;} whrapper that only adds the elements if the predicate is true for that element.
 * In addition, if the given set is not empty, all elements that do not meet the predicate are removed.
 * @author Aritz Lopez
 */
public class FilteredSet<E> implements Set<E> {

	@NotNull
	private final Set<E> set;
	private final Predicate<E> filter;

	private FilteredSet(@NotNull Set<E> set, @NotNull Predicate<E> filter) {
		this.set = set;
		this.filter = filter;
		if (!set.isEmpty()) {
			Iterator<E> iter = set.iterator();
			while (iter.hasNext()) {
				if (!filter.apply(iter.next())) iter.remove();
			}
		}
	}

	/**
	 * Removes all elements of the set that do not meet the predicate, and returns a FilteredSet that will
	 * only add elements if the predicate is met for it.
	 * @param other The set to filter.
	 * @param filter The predicate to apply to each element.
	 * @param <T> The type of the elements of the set, and the type accepted by the filter.
     * @return A filtered set wrapped around the given set, and filtered by the given predicate.
     */
	public static <T> FilteredSet<T> newFilteredSetOf(Set<T> other, Predicate<T> filter) {
		return new FilteredSet<>(other, filter);
	}

	/**
	 * Equivalent to calling {@link FilteredSet#newFilteredSetOf(Set, Predicate)} with the first parameter
	 * being the result of {@link Sets#newHashSet()}.
	 * @param filter The filter to apply to the new hash set.
	 * @param <T> The type of the contents of the set, and the type accepted by the filter.
     * @return A new filtered set wrapped around an empty hash set, filtered by the given predicate.
     */
	public static <T> FilteredSet<T> newFilteredHashSet(Predicate<T> filter) {
		return new FilteredSet<>(Sets.<T>newHashSet(), filter);
	}

	@Override
	public int size() {
		return this.set.size();
	}

	@Override
	public boolean isEmpty() {
		return this.set.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.set.contains(o);
	}

	@Override
	@NotNull
	public Iterator<E> iterator() {
		return this.set.iterator();
	}

	@Override
	@NotNull
	public Object[] toArray() {
		return this.set.toArray();
	}

	@Override
	@NotNull
	@SuppressWarnings("SuspiciousToArrayCall")
	public <T> T[] toArray(@NotNull T[] a) {
		return this.set.toArray(a);
	}

	@Override
	public boolean add(E e) {
		return this.filter.apply(e) && this.set.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return this.set.remove(o);
	}

	@Override
	public boolean containsAll(@NotNull Collection<?> c) {
		return this.set.containsAll(c);
	}

	@Override
	public boolean addAll(@NotNull Collection<? extends E> c) {
		return this.set.addAll(c);
	}

	@Override
	public boolean retainAll(@NotNull Collection<?> c) {
		return this.set.retainAll(c);
	}

	@Override
	public boolean removeAll(@NotNull Collection<?> c) {
		return this.set.removeAll(c);
	}

	@Override
	public void clear() {
		this.set.clear();
	}
}
