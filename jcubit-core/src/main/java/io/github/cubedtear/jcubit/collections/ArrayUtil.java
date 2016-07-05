package io.github.cubedtear.jcubit.collections;

import io.github.cubedtear.jcubit.util.API;
import io.github.cubedtear.jcubit.util.Nullable;

/**
 * Collection of utility methods related to arrays.
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
	@API
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
	@API
	public static <T> T[] prepend(T[] arr, T... firstElements) {
		T[] ret = (T[]) new Object[arr.length + firstElements.length];
		System.arraycopy(firstElements, 0, ret, 0, firstElements.length);
		System.arraycopy(arr, 0, ret, firstElements.length, arr.length);
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

	/**
	 * Boxes a primitive array to a wrapper array.
	 * @param bytes The primitive array.
	 * @return the primitive array converted to a wrapper array, or {@code null} if the input was {@code null}.
	 */
	@Nullable
	public static Byte[] box(byte[] bytes) {
		if (bytes == null) return null;
		Byte[] nBytes = new Byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) nBytes[i] = bytes[i];
		return nBytes;
	}

	/**
	 * Boxes a primitive array to a wrapper array.
	 * @param chars The primitive array.
	 * @return the primitive array converted to a wrapper array, or {@code null} if the input was {@code null}.
	 */
	@Nullable
	public static Character[] box(char[] chars) {
		if (chars == null) return null;
		Character[] nChars = new Character[chars.length];
		for (int i = 0; i < chars.length; i++) nChars[i] = chars[i];
		return nChars;
	}

	/**
	 * Boxes a primitive array to a wrapper array.
	 * @param shorts The primitive array.
	 * @return the primitive array converted to a wrapper array, or {@code null} if the input was {@code null}.
	 */
	@Nullable
	public static Short[] box(short[] shorts) {
		if (shorts == null) return null;
		Short[] nShorts = new Short[shorts.length];
		for (int i = 0; i < shorts.length; i++) nShorts[i] = shorts[i];
		return nShorts;
	}

	/**
	 * Boxes a primitive array to a wrapper array.
	 * @param ints The primitive array.
	 * @return the primitive array converted to a wrapper array, or {@code null} if the input was {@code null}.
	 */
	@Nullable
	public static Integer[] box(int[] ints) {
		if (ints == null) return null;
		Integer[] nInts = new Integer[ints.length];
		for (int i = 0; i < ints.length; i++) nInts[i] = ints[i];
		return nInts;
	}

	/**
	 * Boxes a primitive array to a wrapper array.
	 * @param longs The primitive array.
	 * @return the primitive array converted to a wrapper array, or {@code null} if the input was {@code null}.
	 */
	@Nullable
	public static Long[] box(long[] longs) {
		if (longs == null) return null;
		Long[] nLongs = new Long[longs.length];
		for (int i = 0; i < longs.length; i++) nLongs[i] = longs[i];
		return nLongs;
	}

	/**
	 * Boxes a primitive array to a wrapper array.
	 * @param floats The primitive array.
	 * @return the primitive array converted to a wrapper array, or {@code null} if the input was {@code null}.
	 */
	@Nullable
	public static Float[] box(float[] floats) {
		if (floats == null) return null;
		Float[] nFloats = new Float[floats.length];
		for (int i = 0; i < floats.length; i++) nFloats[i] = floats[i];
		return nFloats;
	}

	/**
	 * Boxes a primitive array to a wrapper array.
	 * @param doubles The primitive array.
	 * @return the primitive array converted to a wrapper array, or {@code null} if the input was {@code null}.
	 */
	@Nullable
	public static Double[] box(double[] doubles) {
		if (doubles == null) return null;
		Double[] nDoubles = new Double[doubles.length];
		for (int i = 0; i < doubles.length; i++) nDoubles[i] = doubles[i];
		return nDoubles;
	}

	/**
	 * Unboxes a wrapper array to a primitive array.
	 * @param bytes The wrapper array.
	 * @return the wrapper array converted to a primitive array, or {@code null} if the input was {@code null}.
	 * @throws NullPointerException If one of the values is null.
	 */
	@Nullable
	public static byte[] unbox(Byte[] bytes) {
		if (bytes == null) return null;
		byte[] nBytes = new byte[bytes.length];
		for (int i = 0; i < bytes.length; i++) nBytes[i] = bytes[i];
		return nBytes;
	}

	/**
	 * Unboxes a wrapper array to a primitive array.
	 * @param chars The wrapper array.
	 * @return the wrapper array converted to a primitive array, or {@code null} if the input was {@code null}.
	 * @throws NullPointerException If one of the values is null.
	 */
	@Nullable
	public static char[] unbox(Character[] chars) {
		if (chars == null) return null;
		char[] nChars = new char[chars.length];
		for (int i = 0; i < chars.length; i++) nChars[i] = chars[i];
		return nChars;
	}

	/**
	 * Unboxes a wrapper array to a primitive array.
	 * @param shorts The wrapper array.
	 * @return the wrapper array converted to a primitive array, or {@code null} if the input was {@code null}.
	 * @throws NullPointerException If one of the values is null.
	 */
	@Nullable
	public static short[] unbox(Short[] shorts) {
		if (shorts == null) return null;
		short[] nShorts = new short[shorts.length];
		for (int i = 0; i < shorts.length; i++) nShorts[i] = shorts[i];
		return nShorts;
	}

	/**
	 * Unboxes a wrapper array to a primitive array.
	 * @param ints The wrapper array.
	 * @return the wrapper array converted to a primitive array, or {@code null} if the input was {@code null}.
	 * @throws NullPointerException If one of the values is null.
	 */
	@Nullable
	public static int[] unbox(Integer[] ints) {
		if (ints == null) return null;
		int[] nInts = new int[ints.length];
		for (int i = 0; i < ints.length; i++) nInts[i] = ints[i];
		return nInts;
	}

	/**
	 * Unboxes a wrapper array to a primitive array.
	 * @param longs The wrapper array.
	 * @return the wrapper array converted to a primitive array, or {@code null} if the input was {@code null}.
	 * @throws NullPointerException If one of the values is null.
	 */
	@Nullable
	public static long[] unbox(Long[] longs) {
		if (longs == null) return null;
		long[] nLongs = new long[longs.length];
		for (int i = 0; i < longs.length; i++) nLongs[i] = longs[i];
		return nLongs;
	}

	/**
	 * Unboxes a wrapper array to a primitive array.
	 * @param floats The wrapper array.
	 * @return the wrapper array converted to a primitive array, or {@code null} if the input was {@code null}.
	 * @throws NullPointerException If one of the values is null.
	 */
	@Nullable
	public static float[] unbox(Float[] floats) {
		if (floats == null) return null;
		float[] nFloats = new float[floats.length];
		for (int i = 0; i < floats.length; i++) nFloats[i] = floats[i];
		return nFloats;
	}

	/**
	 * Unboxes a wrapper array to a primitive array.
	 * @param doubles The wrapper array.
	 * @return the wrapper array converted to a primitive array, or {@code null} if the input was {@code null}.
	 * @throws NullPointerException If one of the values is null.
	 */
	@Nullable
	public static double[] unbox(Double[] doubles) {
		if (doubles == null) return null;
		double[] nDoubles = new double[doubles.length];
		for (int i = 0; i < doubles.length; i++) nDoubles[i] = doubles[i];
		return nDoubles;
	}
}
