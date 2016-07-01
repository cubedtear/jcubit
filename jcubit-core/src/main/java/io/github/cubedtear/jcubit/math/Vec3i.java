package io.github.cubedtear.jcubit.math;

import io.github.cubedtear.jcubit.util.API;

/**
 * 3D Vector of integers (x, y). Immutable.
 *
 * @author Aritz Lopez
 */
@API
public class Vec3i {

	public final int x, y, z;

	/**
	 * Creates a 0-vector. Equivalent to calling {@code new Vec3i(0, 0, 0)}.
	 */
	@API
	public Vec3i() {
		this.x = this.y = this.z = 0;
	}

	/**
	 * Creates a vector with the given coordinates.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
	@API
	public Vec3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
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
	 * Returns the z coordinate of this vector.
	 *
	 * @return the z coordinate of this vector.
	 */
	@API
	public int getZ() {
		return z;
	}

	/**
	 * Returns the result of adding two vectors.
	 * The result is a vector with each coordinate being the sum of the coordinates of {@code this} vector and {@code other}.
	 * @param other The other vector to sum with.
	 * @return The result of adding {@code this} with {@code other}
	 */
	@API
	public Vec3i add(Vec3i other) {
		return new Vec3i(this.x + other.x, this.y + other.y, this.z + other.z);
	}

	/**
	 * Returns the result of the dot-product between {@code this} and {@code other}.
	 * The dot-product of two vectors is the sum of the products of their coordinates.
	 *
	 * @param other The other vector.
	 * @return The dot-product of {@code this} and {@code other}.
	 */
	@API
	public int dot(Vec3i other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	/**
	 * Returns the length of this vector, calculated as the square root of the sum of the coordinates squared.
	 * @return the length of this vector.
	 */
	@API
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
	public Vec3i times(double scalar) {
		return new Vec3i((int) (this.x * scalar), (int) (this.y * scalar), (int) (this.z * scalar));
	}

	/**
	 * Returns the vector with the negative coordinates. Also called the <i>opposite</i>.
	 * @return the opposite vector.
	 */
	@API
	public Vec3i negate() {
		return new Vec3i(-this.x, -this.y, -this.z);
	}

	/**
	 * Returns the cross-product of {@code this} and {@code other}.
	 *
	 * The cross-product of two vectors is a vector perpedicular to both of them.
	 * Two vectors are linearly independent if and only if the length of their cross-product is {@code zero}.
	 * @param other The other vector.
	 * @return the cross-product of {@code this} and {@code other}.
     */
	@API
	public Vec3i crossProd(Vec3i other) {
		return new Vec3i(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x);
	}

}
