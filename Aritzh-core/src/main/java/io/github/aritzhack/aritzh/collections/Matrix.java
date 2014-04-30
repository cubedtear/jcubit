/*
 * Copyright 2014 Aritz Lopez
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.aritzhack.aritzh.collections;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Bidimensional resizable array (Internally {@code ArrayList<ArrayList<E>>}) <br>
 * which also adds some useful methods e.g. {@link Matrix#runForEach(ParametrizedFunction, Object...)}
 *
 * @author Aritz Lopez
 */
@SuppressWarnings({"NullableProblems", "SuspiciousMethodCalls"})
public class Matrix<E> implements Collection<ArrayList<E>> {

    protected E defaultElement = null;
    protected List<ArrayList<E>> columns;

    /**
     * Creates a new matrix, with the default size of 5x5 <br>
     * <b>Note: this will initialize all elements to null!</b>
     */
    public Matrix() {
        this(5, 5);
    }

    private Matrix(int startWidth, int startHeight) {
        this(startWidth, startHeight, null);
    }

    /**
     * Creates a new empty matrix, with the specified starting size
     *
     * @param startWidth     The amount of columns
     * @param startHeight    The amount of rows
     * @param defaultElement The object to set all elements to
     */
    public <D extends E> Matrix(int startWidth, int startHeight, D defaultElement) {
        this.defaultElement = defaultElement;
        this.init(startWidth, startHeight);
    }

    private void init(int startWidth, int startHeight) {
        this.columns = new ArrayList<>();
        for (int x = 0; x < startWidth; x++) {
            this.columns.add(new ArrayList<E>(startHeight));
            for (int y = 0; y < startHeight; y++) {
                this.columns.get(x).add(this.defaultElement);
            }
        }
    }

    /**
     * Runs a {@link ParametrizedFunction} for each element of the matrix, using these arguments:
     * <ol>
     * <li>X Coordinate in the matrix</li>
     * <li>Y Coordinate in the matrix</li>
     * <li>{@code args}, at the end</li>
     * </ol>
     *
     * @param function The ParametrizedFunction to run for each element
     * @param args     The arguments to add at the end of the function's params
     * @param <R>      The return value for the function. Must be the same as the return value of {@code function}
     * @return A list containing all the return values
     */
    public <R> Matrix<R> runForEach(ParametrizedFunction<MatrixElement<E>, R> function, Object... args) {
        Matrix<R> ret;
        try {
            ret = new Matrix<>(this.columns.size(), this.columns.get(0).size());
        } catch (Exception ignored) {
            ret = new Matrix<>();
        }
        for (int x = 0; x < this.columns.size(); x++) {
            ArrayList<E> col = this.columns.get(x);
            for (int y = 0; y < col.size(); y++) {
                if (col.get(y) == null) continue;
                R obj = function.apply(this.getElement(x, y), args);
                ret.set(obj, x, y);
            }
        }
        return ret;
    }

    private MatrixElement<E> getElement(int x, int y) {
        return new MatrixElement<>(this.get(x, y), x, y);
    }

    /**
     * Sets the element {@code element} to the {@code (x, y)} position of the matrix
     *
     * @param element The element to be set
     * @param x       The row
     * @param y       The column
     * @return {@code this}
     */
    public Matrix<E> set(E element, int x, int y) {
        if (this.columns.size() < x) {
            columns.set(x, new ArrayList<E>());
        }
        this.columns.get(x).set(y, element);
        return this;
    }

    /**
     * Returns the element in column {@code x} and y {@code y}
     *
     * @param x The column of the element
     * @param y The row of the element
     * @return the element at (x, y)
     */
    public E get(int x, int y) {
        if (columns.size() < x) {
            columns.set(x, new ArrayList<E>());
        }
        ArrayList<E> columnList = columns.get(x);
        if (columnList.size() < y) {
            columnList.set(y, this.defaultElement);
        }
        return columnList.get(y);
    }

    @Override
    public int size() {
        try {
            return this.columns.size() * this.columns.get(0).size();
        } catch (Exception ignored) {
            return 0;
        }
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    /**
     * Returns an iterator for this matrix.
     * It returns a iterator of {@code ArrayList<E>},
     * each ArrayList being a column
     *
     * @return an Iterator of {@code ArrayList<E>}s
     */
    @Override
    public Iterator<ArrayList<E>> iterator() {
        return new MatrixIterator(this);
    }

    @Override
    public Object[] toArray() {
        return this.toFlatArrayList().toArray();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        try {
            Object[] oa = this.toArray();
            T[] ret = (a.length > oa.length ? a : (T[]) Array.newInstance(a.getClass().getComponentType(), oa.length));
            for (int i = 0; i < oa.length; i++) {
                ret[i] = (T) oa[i];
            }
            return ret;
        } catch (Exception ignored) {
        }
        return a;
    }

    /**
     * Flattens the matrix, column-after-column, so that it can
     * be used as a common collection
     */
    public ArrayList<E> toFlatArrayList() {
        ArrayList<E> ret = new ArrayList<>();
        this.columns.forEach(ret::addAll);
        return ret;
    }

    @Override
    public boolean add(ArrayList<E> es) {
        return this.columns.add(es);
    }

    @Override
    public boolean remove(Object o) {
        if (this.columns.remove(o)) return true;

        for (ArrayList<E> list : this) {
            if (list.remove(o)) return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        List<?> missing = new ArrayList<>(c);

        A:
        for (Object o : missing) {
            if (columns.contains(o)) {
                missing.remove(o);
                continue;
            }
            for (ArrayList<E> list : this) {
                if (list.contains(o)) {
                    missing.remove(o);
                    continue A;
                }
            }
        }
        return missing.isEmpty();
    }

    @Override
    public boolean addAll(Collection<? extends ArrayList<E>> c) {
        return this.columns.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        List<?> missing = new ArrayList<>(c);
        A:
        for (Object o : missing) {
            if (columns.contains(o)) {
                columns.remove(o);
                missing.remove(o);
                continue;
            }
            for (ArrayList<E> list : this) {
                if (list.contains(o)) {
                    list.remove(o);
                    missing.remove(o);
                    continue A;
                }
            }
        }
        return missing.isEmpty();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean ret = false;
        for (ArrayList<E> list : this) {
            if (!c.contains(list)) {
                this.columns.remove(list);
                ret = true;
                continue;
            }
            for (E e : list) {
                if (!c.contains(e)) {
                    list.remove(e);
                    ret = true;
                }
            }
        }
        return ret;
    }

    /**
     * Sets this matrix to a 1x1 matrix with the {@code defaultElement} as the only element
     */
    @Override
    public void clear() {
        this.init(1, 1);
    }

    private class MatrixIterator implements Iterator<ArrayList<E>> {

        private final Matrix<E> matrix;
        int current = 0;

        public MatrixIterator(Matrix<E> matrix) {
            this.matrix = matrix;
        }

        @Override
        public boolean hasNext() {
            return current >= this.matrix.columns.size();
        }

        @Override
        public ArrayList<E> next() {
            if (current == this.matrix.columns.size())
                throw new NoSuchElementException();
            current++;
            return this.matrix.columns.get(current - 1);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public class MatrixElement<T extends E> {
        private final T element;
        private final int x, y;

        public MatrixElement(T element, int x, int y) {
            this.element = element;
            this.x = x;
            this.y = y;
        }

        public T getE() {
            return element;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Matrix<E> getMatrix() {
            return Matrix.this;
        }
    }
}
