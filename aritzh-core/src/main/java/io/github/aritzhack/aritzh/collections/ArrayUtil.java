package io.github.aritzhack.aritzh.collections;

import com.google.common.base.Preconditions;

/**
 * @author Aritz Lopez
 */
public enum ArrayUtil {
	;

	/**
	 * Append one or more elements to an array (Add them to the end).
	 *
	 * @param arr          The array the elements will be appended to.
	 * @param lastElements The elements to append, in order (first to last).
	 * @param <T>          The type of the elements in the array.
	 * @return The array with all elements (The same as the {@code arr} argument).
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <T> T[] append(T[] arr, T... lastElements) {
		T[] ret = (T[]) new Object[arr.length + lastElements.length];
		System.arraycopy(lastElements, 0, ret, arr.length, lastElements.length);
		System.arraycopy(arr, 0, ret, 0, arr.length);
		return ret;
	}

	/**
	 * Prepend one or more elements to an array (Add them to the beginning).
	 *
	 * @param arr           The array the elements will be appended to.
	 * @param firstElements The elements to prepend, in order (first to last).
	 * @param <T>           The type of the elements in the array.
	 * @return The array with all elements (The same as the {@code arr} argument).
	 */
	@SafeVarargs
	@SuppressWarnings("unchecked")
	public static <T> T[] prepend(T[] arr, T... firstElements) {
		T[] ret = (T[]) new Object[arr.length + firstElements.length];
		System.arraycopy(firstElements, 0, ret, 0, firstElements.length);
		System.arraycopy(arr, 0, ret, firstElements.length, arr.length);
		return ret;
	}

	/**
	 * Convert a {@link Byte} array to a primitive {@code byte} array.
	 *
	 * @param byteArray The array to convert to primitive.
	 * @return The primitive array the argument represents.
	 * @throws NullPointerException If a null was found, thrown when unboxing.
	 */
	public static byte[] toPrimitiveArray(Byte[] byteArray) throws NullPointerException {
		Preconditions.checkArgument(byteArray != null);
		byte[] ret = new byte[byteArray.length];
		for (int i = 0; i < byteArray.length; i++) ret[i] = byteArray[i];
		return ret;
	}

	/**
	 * Convert a {@link Integer} array to a primitive {@code int} array.
	 *
	 * @param integerArray The array to convert to primitive.
	 * @return The primitive array the argument represents.
	 * @throws NullPointerException If a null was found, thrown when unboxing.
	 */
	public static int[] toPrimitiveArray(Integer[] integerArray) throws NullPointerException {
		Preconditions.checkArgument(integerArray != null);
		int[] ret = new int[integerArray.length];
		for (int i = 0; i < integerArray.length; i++) ret[i] = integerArray[i];
		return ret;
	}

	/**
	 * Convert a {@link Short} array to a primitive {@code short} array.
	 *
	 * @param shortArray The array to convert to primitive.
	 * @return The primitive array the argument represents.
	 * @throws NullPointerException If a null was found, thrown when unboxing.
	 */
	public static short[] toPrimitiveArray(Short[] shortArray) throws NullPointerException {
		Preconditions.checkArgument(shortArray != null);
		short[] ret = new short[shortArray.length];
		for (int i = 0; i < shortArray.length; i++) ret[i] = shortArray[i];
		return ret;
	}

	/**
	 * Convert a {@link Long} array to a primitive {@code long} array.
	 *
	 * @param longArray The array to convert to primitive.
	 * @return The primitive array the argument represents.
	 * @throws NullPointerException If a null was found, thrown when unboxing.
	 */
	public static long[] toPrimitiveArray(Long[] longArray) throws NullPointerException {
		Preconditions.checkArgument(longArray != null);
		long[] ret = new long[longArray.length];
		for (int i = 0; i < longArray.length; i++) ret[i] = longArray[i];
		return ret;
	}

	/**
	 * Convert a {@link Float} array to a primitive {@code float} array.
	 *
	 * @param floatArray The array to convert to primitive.
	 * @return The primitive array the argument represents.
	 * @throws NullPointerException If a null was found, thrown when unboxing.
	 */
	public static float[] toPrimitiveArray(Float[] floatArray) throws NullPointerException {
		Preconditions.checkArgument(floatArray != null);
		float[] ret = new float[floatArray.length];
		for (int i = 0; i < floatArray.length; i++) ret[i] = floatArray[i];
		return ret;
	}

	/**
	 * Convert a {@link Double} array to a primitive {@code double} array.
	 *
	 * @param doubleArray The array to convert to primitive.
	 * @return The primitive array the argument represents.
	 * @throws NullPointerException If a null was found, thrown when unboxing.
	 */
	public static double[] toPrimitiveArray(Double[] doubleArray) throws NullPointerException {
		Preconditions.checkArgument(doubleArray != null);
		double[] ret = new double[doubleArray.length];
		for (int i = 0; i < doubleArray.length; i++) ret[i] = doubleArray[i];
		return ret;
	}

	/**
	 * Convert a {@link Character} array to a primitive {@code char} array.
	 *
	 * @param characterArray The array to convert to primitive.
	 * @return The primitive array the argument represents.
	 * @throws NullPointerException If a null was found, thrown when unboxing.
	 */
	public static char[] toPrimitiveArray(Character[] characterArray) throws NullPointerException {
		Preconditions.checkArgument(characterArray != null);
		char[] ret = new char[characterArray.length];
		for (int i = 0; i < characterArray.length; i++) ret[i] = characterArray[i];
		return ret;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static boolean equals(byte[] a1, byte[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i] != a2[i]) return false;
		return true;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static boolean equals(short[] a1, short[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i] != a2[i]) return false;
		return true;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static boolean equals(char[] a1, char[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i] != a2[i]) return false;
		return true;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static boolean equals(int[] a1, int[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i] != a2[i]) return false;
		return true;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static boolean equals(long[] a1, long[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i] != a2[i]) return false;
		return true;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static boolean equals(float[] a1, float[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i] != a2[i]) return false;
		return true;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static boolean equals(double[] a1, double[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i] != a2[i]) return false;
		return true;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static boolean equals(boolean[] a1, boolean[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i] != a2[i]) return false;
		return true;
	}

	/**
	 * Checks if two arrays are equal, up to the provided length.
	 * Checked positions are [0, {@code length})
	 *
	 * @param a1     The first array
	 * @param a2     The second array
	 * @param length The length up to which to check if {@code a1} and {@code a2} are equal
	 * @param <T>    The type of the array contents
	 * @return Whether {@code a1} and {@code a2} are equal up to {@code length}
	 */
	public static <T> boolean equals(T[] a1, T[] a2, int length) {
		if (a1.length < length || a2.length < length) return false;
		for (int i = 0; i < length; i++) if (a1[i].equals(a2[i])) return false;
		return true;
	}

	public static Byte[] box(byte[] bytes) {
		if (bytes == null) return null;
		Byte[] nBytes = new Byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) nBytes[i] = bytes[i];
		return nBytes;
	}

	public static Short[] box(short[] shorts) {
		if (shorts == null) return null;
		Short[] nShorts = new Short[shorts.length];
		for (int i = 0; i < shorts.length; i++) nShorts[i] = shorts[i];
		return nShorts;
	}

	public static Integer[] box(int[] ints) {
		if (ints == null) return null;
		Integer[] nInts = new Integer[ints.length];
		for (int i = 0; i < ints.length; i++) nInts[i] = ints[i];
		return nInts;
	}

	public static Long[] box(long[] longs) {
		if (longs == null) return null;
		Long[] nLongs = new Long[longs.length];
		for (int i = 0; i < longs.length; i++) nLongs[i] = longs[i];
		return nLongs;
	}

	public static Float[] box(float[] floats) {
		if (floats == null) return null;
		Float[] nFloats = new Float[floats.length];
		for (int i = 0; i < floats.length; i++) nFloats[i] = floats[i];
		return nFloats;
	}

	public static Double[] box(double[] doubles) {
		if (doubles == null) return null;
		Double[] nDoubles = new Double[doubles.length];
		for (int i = 0; i < doubles.length; i++) nDoubles[i] = doubles[i];
		return nDoubles;
	}

	public static byte[] unbox(Byte[] bytes) {
		if (bytes == null) return null;
		byte[] nBytes = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) nBytes[i] = bytes[i];
		return nBytes;
	}

	public static short[] unbox(Short[] shorts) {
		if (shorts == null) return null;
		short[] nShorts = new short[shorts.length];
		for (int i = 0; i < shorts.length; i++) nShorts[i] = shorts[i];
		return nShorts;
	}

	public static int[] unbox(Integer[] ints) {
		if (ints == null) return null;
		int[] nInts = new int[ints.length];
		for (int i = 0; i < ints.length; i++) nInts[i] = ints[i];
		return nInts;
	}

	public static long[] unbox(Long[] longs) {
		if (longs == null) return null;
		long[] nLongs = new long[longs.length];
		for (int i = 0; i < longs.length; i++) nLongs[i] = longs[i];
		return nLongs;
	}

	public static float[] unbox(Float[] floats) {
		if (floats == null) return null;
		float[] nFloats = new float[floats.length];
		for (int i = 0; i < floats.length; i++) nFloats[i] = floats[i];
		return nFloats;
	}

	public static double[] unbox(Double[] doubles) {
		if (doubles == null) return null;
		double[] nDoubles = new double[doubles.length];
		for (int i = 0; i < doubles.length; i++) nDoubles[i] = doubles[i];
		return nDoubles;
	}
}
