package io.github.cubedtear.jcubit.math;

import java.awt.*;

/**
 * @author Aritz Lopez
 */
public class Vec2i {

	public final int x, y;

	public Vec2i() {
		this.x = this.y = 0;
	}

	public Vec2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Vec2i fromPoint(Point p) {
		return new Vec2i(p.x, p.y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Vec2i add(Vec2i other) {
		return new Vec2i(this.x + other.x, this.y + other.y);
	}

	public int dot(Vec2i other) {
		return this.x * other.x + this.y * other.y;
	}

	public double length() {
		return Math.sqrt(this.dot(this));
	}

	public Vec2i times(double scalar) {
		return new Vec2i((int) (this.x * scalar), (int) (this.y * scalar));
	}

	public Vec2i negate() {
		return new Vec2i(-this.x, -this.y);
	}

	public Point toPoint() {
		return new Point(this.x, this.y);
	}

	public int max() {
		return Math.max(this.x, this.y);
	}

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
