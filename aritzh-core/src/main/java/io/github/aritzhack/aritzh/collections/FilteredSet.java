package io.github.aritzhack.aritzh.collections;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import io.github.aritzhack.aritzh.util.NotNull;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
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

    public static <T> FilteredSet<T> newFilteredSetOf(Set<T> other, Predicate<T> filter) {
        return new FilteredSet<>(other, filter);
    }

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
    public <T> T[] toArray(T[] a) {
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
