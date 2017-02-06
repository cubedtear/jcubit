package io.github.cubedtear.jcubit.math;

import io.github.cubedtear.jcubit.util.API;

/**
 * Integer rectangle class with some useful methods. Immutable.
 *
 * @author Aritz Lopez
 */
@API
public class Rectangle {
    /**
     * Empty rectangle. If a method of this class will return an empty (width or height equal to 0), it will return this instance instead.
     */
    public static final Rectangle EMPTY = new Rectangle(0, 0, 0, 0);

    /**
     * The x coordinate of the top-left corner.
     */
    public final int x;

    /**
     * The y coordinate of the top-left corner.
     */
    public final int y;

    /**
     * The width of the rectangle.
     */
    public final int width;

    /**
     * The height of the rectangle.
     */
    public final int height;

    // Lazily calculated.
    private Vec2i topLeft = null;
    private Vec2i topRight = null;
    private Vec2i bottomLeft = null;
    private Vec2i bottomRight = null;

    /**
     * Creates a new Rectangle from the given parameters.
     *
     * @param x      The x coordinate of the top-left corner.
     * @param y      The y coordinate of the top-left corner.
     * @param width  The width.
     * @param height The height.
     */
    @API
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a Rectangle from two opposing corners (i.e. top-left and bottom-right, or top-right and bottom-left).
     *
     * @param p1 One corner.
     * @param p2 The opposite corner.
     */
    @API
    public Rectangle(Vec2i p1, Vec2i p2) {
        this.x = Math.min(p1.x, p2.x);
        this.y = Math.min(p1.y, p2.y);
        this.width = Math.abs(p2.x - p1.x);
        this.height = Math.abs(p2.y - p1.y);
    }

    /**
     * @return The x coordinate of the right side of the rectangle.
     */
    @API
    public int getX2() {
        return this.x + this.width;
    }

    /**
     * @return The y coordinate of the bottom side of the rectangle..
     */
    @API
    public int getY2() {
        return this.y + this.height;
    }

    /**
     * @return The area of this rectangle (width * height).
     */
    @API
    public int getArea() {
        return width * height;
    }

    /**
     * @return The perimeter of this rectangle (2*width + 2*height).
     */
    @API
    public int getPerimeter() {
        return 2 * width + 2 * height;
    }

    /**
     * Checks if the given point is inside the bounds of this rectangle (including edges).
     *
     * @param point The point to check if it is inside the bounds.
     * @return True if and only if the point is inside this rectangle.
     */
    @API
    public boolean contains(Vec2i point) {
        return point.x >= this.x && point.y >= this.y && point.x <= this.x + this.width && point.y <= this.y + this.height;
    }

    /**
     * Calculates the intersection of this rectangle with another one.
     * A point will be {@link Rectangle#contains(Vec2i) contained} by the result, if and only if the point is contained
     * by {@code this} <b>and</b> by {@code other}.
     *
     * @param other The other rectangle.
     * @return A new rectangle which contains the area that both {@code this} and {@code other} contain.
     */
    @API
    public Rectangle intersection(Rectangle other) {
        int x_min = Math.max(this.x, other.x);
        int y_min = Math.max(this.y, other.y);
        int x_max = Math.min(this.x + this.width, other.x + other.width);
        int y_max = Math.min(this.y + this.height, other.y + other.height);

        if (x_min > x_max || y_min > y_max) return EMPTY;

        return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
    }

    /**
     * Calculates the smalles rectangle that encloses both {@code this} and {@code other}.
     *
     * @param other The other rectangle.
     * @return The smallest Rectangle enclosing both {@code this} and {@code other}.
     */
    @API
    public Rectangle extendToContain(Rectangle other) {
        int x_min = Math.min(this.x, other.x);
        int y_min = Math.min(this.y, other.y);
        int x_max = Math.max(this.x + this.width, other.x + other.width);
        int y_max = Math.max(this.y + this.height, other.y + other.height);
        return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
    }

    /**
     * Moves the whole rectangle by the given amount on each coordinate.
     *
     * @param dx The amount to move on the x axis.
     * @param dy The amount to move on the y axis.
     * @return A new rectangle, same size as {@code this}, but moved the given amount.
     */
    @API
    public Rectangle move(int dx, int dy) {
        return new Rectangle(this.x + dx, this.y + dy, this.width, this.height);
    }

    /**
     * Moves the whole rectangle by the given vector.
     *
     * @param delta The amount to move on the x and y axes.
     * @return A new rectangle, same size as {@code this}, but moved the given vector.
     */
    @API
    public Rectangle move(Vec2i delta) {
        return this.move(delta.x, delta.y);
    }

    @Override
    public String toString() {
        return "Rectangle={x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "}";
    }

    /**
     * @return A vector containing the coordinates of the top-left corner of this rectangle.
     */
    @API
    public Vec2i getTopLeft() {
        if (this.topLeft == null) {
            this.topLeft = new Vec2i(x, y);
        }
        return this.topLeft;
    }

    /**
     * @return A vector containing the coordinates of the top-right corner of this rectangle.
     */
    @API
    public Vec2i getTopRight() {
        if (this.topRight == null) {
            this.topRight = new Vec2i(x + width, y);
        }
        return this.topRight;
    }

    /**
     * @return A vector containing the coordinates of the bottom-left corner of this rectangle.
     */
    @API
    public Vec2i getBottomLeft() {
        if (this.bottomLeft == null) {
            this.bottomLeft = new Vec2i(x, y + height);
        }
        return this.bottomLeft;
    }

    /**
     * @return A vector containing the coordinates of the bottom-right corner of this rectangle.
     */
    @API
    public Vec2i getBottomRight() {
        if (this.bottomRight == null) {
            this.bottomRight = new Vec2i(x + width, y + height);
        }
        return this.bottomRight;
    }

}
