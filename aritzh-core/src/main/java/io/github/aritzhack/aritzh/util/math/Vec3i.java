package io.github.aritzhack.aritzh.util.math;

/**
 * @author Aritz Lopez
 */
public class Vec3i {

	public final int x, y, z;

	public Vec3i() {
		this.x = this.y = this.z = 0;
	}

	public Vec3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	public Vec3i add(Vec3i other) {
		return new Vec3i(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	public int dot(Vec3i other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	public double length() {
		return Math.sqrt(this.dot(this));
	}

	public Vec3i times(double scalar) {
		return new Vec3i((int) (this.x * scalar), (int) (this.y * scalar), (int) (this.z * scalar));
	}

	public Vec3i negate() {
		return new Vec3i(-this.x, -this.y, -this.z);
	}

	public Vec3i crossProd(Vec3i other) {
		return new Vec3i(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x);
	}

}
