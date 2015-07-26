package io.github.aritzhack.aritzh.math;

/**
 * @author Aritz Lopez
 */
public class Vec4i {

	public final int x, y, z, w;

	public Vec4i() {
		this.x = this.y = this.z = this.w = 0;
	}

	public Vec4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getW() {
		return w;
	}

	public Vec4i add(Vec4i other) {
		return new Vec4i(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
	}

	public int dot(Vec4i other) {
		return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
	}

	public double length() {
		return Math.sqrt(this.dot(this));
	}

	public Vec4i times(double scalar) {
		return new Vec4i((int) (this.x * scalar), (int) (this.y * scalar), (int) (this.z * scalar), (int) (this.w * scalar));
	}

	public Vec4i negate() {
		return new Vec4i(-this.x, -this.y, -this.z, -this.w);
	}

}
