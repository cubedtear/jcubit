package io.github.cubedtear.jcubit.math;

/**
 * @author Aritz Lopez
 */
public class Vec4i {

	public final int x, y, z, w;

	/**
	 * Creates a 0-vector. Equivalent to calling {@code new Vec4i(0, 0, 0, 0)}.
	 */
	public Vec4i() {
		this.x = this.y = this.z = this.w = 0;
	}

	/**
	 * Creates a vector with the given coordinates.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 * @param w the w coordinate.
	 */
	public Vec4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	/**
	 * Returns the x coordinate of this vector.
	 *
	 * @return the x coordinate of this vector.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y coordinate of this vector.
	 *
	 * @return the y coordinate of this vector.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Returns the z coordinate of this vector.
	 *
	 * @return the z coordinate of this vector.
	 */
	public int getZ() {
		return z;
	}

	/**
	 * Returns the w coordinate of this vector.
	 *
	 * @return the w coordinate of this vector.
	 */
	public int getW() {
		return w;
	}

	/**
	 * Returns the result of adding two vectors.
	 * The result is a vector with each coordinate being the sum of the coordinates of {@code this} vector and {@code other}.
	 * @param other The other vector to sum with.
	 * @return The result of adding {@code this} with {@code other}
	 */
	public Vec4i add(Vec4i other) {
		return new Vec4i(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
	}

	/**
	 * Returns the result of the dot-product between {@code this} and {@code other}.
	 * The dot-product of two vectors is the sum of the products of their coordinates.
	 *
	 * @param other The other vector.
	 * @return The dot-product of {@code this} and {@code other}.
	 */
	public int dot(Vec4i other) {
		return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
	}

	/**
	 * Returns the length of this vector, calculated as the square root of the sum of the coordinates squared.
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
	public Vec4i times(double scalar) {
		return new Vec4i((int) (this.x * scalar), (int) (this.y * scalar), (int) (this.z * scalar), (int) (this.w * scalar));
	}

	/**
	 * Returns the vector with the negative coordinates. Also called the <i>opposite</i>.
	 * @return the opposite vector.
	 */
	public Vec4i negate() {
		return new Vec4i(-this.x, -this.y, -this.z, -this.w);
	}

}
