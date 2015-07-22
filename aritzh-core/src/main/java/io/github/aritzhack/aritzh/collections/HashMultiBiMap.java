package io.github.aritzhack.aritzh.collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import java.util.Set;

/**
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

	public static <K, V> HashMultiBiMap<K, V> create() {
		return new HashMultiBiMap<>();
	}

	public HashMultiBiMap<V, K> inverse() {
		return new HashMultiBiMap<>(valuesToKeys, keysToValues);
	}

	public boolean put(K k, V v) {
		return keysToValues.put(k, v) && valuesToKeys.put(v, k);
	}

	public Set<V> get(K k) {
		return keysToValues.get(k);
	}

	public HashMultiBiMap<K, V> copy() {
		return new HashMultiBiMap<>(HashMultimap.create(keysToValues), HashMultimap.create(valuesToKeys));
	}

	public Set<K> keys() {
		return keysToValues.keySet();
	}
}
