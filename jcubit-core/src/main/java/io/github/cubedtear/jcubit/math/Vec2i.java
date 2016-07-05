package io.github.cubedtear.jcubit.math;

import io.github.cubedtear.jcubit.util.API;

import java.awt.*;

/**
 * 2D Vector of integers (x, y). Immutable.
 *
 * @author Aritz Lopez
 */
@API
public class Vec2i {

    public final int x;
    public final int y;

    /**
     * Creates a 0-vector. Equivalent to calling {@code new Vec2i(0, 0)}.
     */
    @API
    public Vec2i() {
        this.x = this.y = 0;
    }

    /**
     * Creates a vector with the given coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Vec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Converts a {@link Point} to a Vec2i.
     *
     * @param p the point to convert.
     * @return a Vec2i equivalent in coordinates to the given points.
     */
    @API
    public static Vec2i fromPoint(Point p) {
        return new Vec2i(p.x, p.y);
    }

    /**
     * Returns the x coordinate of this vector.
     *
     * @return the x coordinate of this vector.
     */
    @API
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of this vector.
     *
     * @return the y coordinate of this vector.
     */
    @API
    public int getY() {
        return y;
    }

    /**
     * Returns the result of adding two vectors.
     * The result is a vector with each coordinate being the sum of the coordinates of {@code this} vector and {@code other}.
     * @param other The other vector to sum with.
     * @return The result of adding {@code this} with {@code other}
     */
    public Vec2i add(Vec2i other) {
        return new Vec2i(this.x + other.x, this.y + other.y);
    }

    /**
     * Returns the result of the dot-product between {@code this} and {@code other}.
     * The dot-product of two vectors is the sum of the products of their coordinates
     * (i.e. {@code this.x*other.x + this.y*other.y}).
     * @param other The other vector.
     * @return The dot-product of {@code this} and {@code other}.
     */
    public int dot(Vec2i other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * Returns the length of this vector, calculated as the square root of the sum of the coordinates squared
     * (i.e. {@code Math.sqrt(x*x + y*y)}.
     * @return the length of this vector.
     */
    public double length() {
        return Math.sqrt(this.dot(this));
    }

    /**
     * Multiplies {@code this} vector by a scalar. Equivalent to the vector resulting of multiplying each coordinate
     * by the scalar. Be careful, the resulting coordinates are casted to ints.
     * @param scalar The scalar to multiply by.
     * @return {@code this} vector multiplied by {@code scalar}.
     */
    @API
    public Vec2i times(double scalar) {
        return new Vec2i((int) (this.x * scalar), (int) (this.y * scalar));
    }

    /**
     * Returns the vector with the negative coordinates (i.e. {@code new Vec2i(-x, -y)}). Also called the <i>opposite</i>.
     * @return the opposite vector.
     */
    @API
    public Vec2i negate() {
        return new Vec2i(-this.x, -this.y);
    }

    /**
     * Converts {@code this} vector to a {@link Point java.awt.Point}.
     * @return this vector as a {@code java.awt.Point}.1
     */
    @API
    public Point toPoint() {
        return new Point(this.x, this.y);
    }

    /**
     * Returns the maximum of the two coordinates.
     * @return max(x, y).
     */
    @API
    public int max() {
        return Math.max(this.x, this.y);
    }

    /**
     * Returns the minimum of the two coordinates.
     * @return min(x, y).
     */
    @API
    public int min() {
        return Math.min(this.x, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2i vec2i = (Vec2i) o;
        return x == vec2i.x && y == vec2i.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
