package io.github.aritzhack.aritzh.util.math;

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
}
