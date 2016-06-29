package io.github.cubedtear.jcubit.collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.Set;

/**
 * Bi-directional multimap based on hashCodes. This means there is a many-many relationship betweem keys and values,
 * so for each key, there may be several values, and the map can be inverted so that for each value (now keys) there
 * may be several keys (now values).
 * @author Aritz Lopez
 */
public class HashMultiBiMap<K, V> {

	private final SetMultimap<K, V> keysToValues;
	private final SetMultimap<V, K> valuesToKeys;

	private HashMultiBiMap() {
		keysToValues = HashMultimap.create();
		valuesToKeys = HashMultimap.create();
	}

	private HashMultiBiMap(SetMultimap<K, V> keysToValues, SetMultimap<V, K> valuesToKeys) {
		this.keysToValues = keysToValues;
		this.valuesToKeys = valuesToKeys;
	}

	/**
	 * Creates an empty HashMultiBiMap.
	 * @param <K> The type of the keys of the created map.
	 * @param <V> The type of the values of the created map.
     * @return A new empty HashMultiBiMap.
     */
	public static <K, V> HashMultiBiMap<K, V> create() {
		return new HashMultiBiMap<>();
	}

	/**
	 * Returns the inverse of this map. All changes to the inverse are also reflected in this map.
	 * The keys in this map will be the values in the inverse, and the values in this map will be the keys
	 * in the inverse. All key-value relationships will be maintained, although the will be accessed the other way around
	 * in the inverse.
	 * @return The inverse of this map.
	 */
	public HashMultiBiMap<V, K> inverse() {
		return new HashMultiBiMap<>(valuesToKeys, keysToValues);
	}

	/**
	 * Adds a relationship between {@code k} and {@code v} in this map (and the inverse). This implies that if
	 * this method returns true, and nothing else is changed, a subsequent call to {@link HashMultiBiMap#get(Object) this.get(k)}
	 * will return a Set containing v.
	 * @param k The key
	 * @param v The value
     * @return true if and only if the relationship was added successfully.
     */
	public boolean put(K k, V v) {
		return keysToValues.put(k, v) && valuesToKeys.put(v, k);
	}

	/**
	 * Returns the value related to the key k.
	 * @param k The key for which the values have to be retrieved.
	 * @return A set containing the values related to k.
     */
	public Set<V> get(K k) {
		return keysToValues.get(k);
	}

	/**
	 * Creates an exact but totally independent copy of this map.
	 * @return an exact but totally independent copy of this map.
     */
	public HashMultiBiMap<K, V> copy() {
		return new HashMultiBiMap<>(HashMultimap.create(keysToValues), HashMultimap.create(valuesToKeys));
	}

	/**
	 * The set of keys of this map.
	 * For the set of values, use {@code this.inverse().keys()}.
	 * @return the set of keys of this map.
     */
	public Set<K> keys() {
		return keysToValues.keySet();
	}
}
