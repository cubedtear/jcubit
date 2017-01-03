package io.github.cubedtear.jcubit.math;

/**
 * @author Aritz Lopez
 */
public class Rectangle {
    public static final Rectangle EMPTY = new Rectangle(0, 0, 0, 0);

    public final int x, y, width, height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX2() {
        return this.x + this.width;
    }

    public int getY2() {
        return this.y + this.height;
    }

    public int getArea() {
        return width * height;
    }

    public int getPerimeter() {
        return 2 * width + 2 * height;
    }

    public boolean contains(Vec2i point) {
        return point.x >= this.x && point.y >= this.y && point.x <= this.x + this.width && point.y <= this.y + this.height;
    }

    public Rectangle intersection(Rectangle other) {
        int x_min = Math.max(this.x, other.x);
        int y_min = Math.max(this.y, other.y);
        int x_max = Math.min(this.x + this.width, other.x + other.width);
        int y_max = Math.min(this.y + this.height, other.y + other.height);

        if (x_min > x_max || y_min > y_max) return EMPTY;

        return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
    }

    public Rectangle extendToContain(Rectangle other) {
        int x_min = Math.min(this.x, other.x);
        int y_min = Math.min(this.y, other.y);
        int x_max = Math.max(this.x + this.width, other.x + other.width);
        int y_max = Math.max(this.y + this.height, other.y + other.height);
        return new Rectangle(x_min, y_min, x_max - x_min, y_max - y_min);
    }

    public Rectangle move(int x, int y) {
        return new Rectangle(this.x + x, this.y + y, this.width, this.height);
    }

    public Rectangle move(Vec2i delta) {
        return this.move(delta.x, delta.y);
    }

    @Override
    public String toString() {
        return "Rectangle={x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "}";
    }
}
