package io.github.aritzhack.aritzh.bds;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.github.aritzhack.aritzh.logging.OSLogger;
import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.core.LogLevel;
import io.github.aritzhack.aritzh.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author aritzh
 */
public class BDSUtil {

    private static final String CLASS_NAME_TAG = "__className";
    private static final String ROOT_OBJ_TAG = "__root";
    private static final String BACKREF_PREFIX = "__back_";
    private static final String IDX_TAG = "__idref";

    private static ILogger LOG = new OSLogger.Builder(System.out, "SAWS-Serialization").setLevel(LogLevel.INFO).build();

    private static BDS serializeInternal(String objName, Object instance, Multimap<Map.Entry<String, Integer>, Map.Entry<Integer, Object>> alreadyWritten, int biggest) throws SerializationException {
        BDS bds = BDS.createEmpty(objName);

        Class<?> c = instance.getClass();
        bds.addString(CLASS_NAME_TAG, c.getName());

        alreadyWritten.put(Maps.immutableEntry(c.getName(), instance.hashCode()), Maps.immutableEntry(biggest, instance));
        bds.addInt(IDX_TAG, biggest++);

        for (Field f : ReflectionUtil.getAllFields(c)) {
            if (f.isAnnotationPresent(Transient.class)) continue;
            if (!f.isAccessible()) {
                try {
                    f.setAccessible(true);
                } catch (SecurityException e) {
                    throw new SerializationException("Error serializing: Cannot access field " + f.getName() + " of class " + c.getName(), e);
                }
            }
            Class<?> fType = f.getType();
            try {
                Object fValue = f.get(instance);
                String fName = f.getName();
                if (Byte.class.equals(fType) || byte.class.equals(fType)) {
                    bds.addByte(fName, (Byte) fValue);
                } else if (Character.class.equals(fType) || byte.class.equals(fType)) {
                    bds.addChar(fName, (Character) fValue);
                } else if (Short.class.equals(fType) || short.class.equals(fType)) {
                    bds.addShort(fName, (Short) fValue);
                } else if (Integer.class.equals(fType) || int.class.equals(fType)) {
                    bds.addInt(fName, (Integer) fValue);
                } else if (Long.class.equals(fType) || long.class.equals(fType)) {
                    bds.addLong(fName, (Long) fValue);
                } else if (Float.class.equals(fType) || float.class.equals(fType)) {
                    bds.addFloat(fName, (Float) fValue);
                } else if (Double.class.equals(fType) || double.class.equals(fType)) {
                    bds.addDouble(fName, (Double) fValue);
                } else if (String.class.equals(fType)) {
                    bds.addString(fName, (String) fValue);
                } else {
                    Integer idx = null;
                    Collection<Map.Entry<Integer, Object>> entries = alreadyWritten.get(Maps.immutableEntry(fType.getName(), fValue.hashCode()));
                    for (Map.Entry<Integer, Object> entry : entries) {
                        if (entry.getValue().equals(fValue)) {
                            idx = entry.getKey();
                            break;
                        }
                    }
                    if (idx != null) {
                        LOG.d("A cycle has been detected! Object of class {} has field {} which has already been serialized!", c.getName(), fName);
                        bds.addInt(BACKREF_PREFIX + c.getName() + fName, idx);
                    } else {
                        try {
                            BDS child = serializeInternal(fName, fValue, alreadyWritten, biggest);
                            bds.addBDS(child);
                        } catch (StackOverflowError e) {
                            throw new SerializationException("Most probably a cycle exits in the object structure, and it could not be fixed. Provide a good hashCode to your classes to fix it", e);
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                throw new SerializationException("Error serializing: Field " + f.getName() + "of class " + c.getName() + " could not be accessed", e);
            }
        }
        return bds;
    }

    private static <T> T deserializeInternal(BDS bds, Map<Integer, Object> pastInstances, Set<UnresolvedReference> unresolvedReferences) throws SerializationException {
        String className = bds.getString(CLASS_NAME_TAG);
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new SerializationException("Error deserializing: Class " + className + " could not be found", e);
        }
        T instance;
        try {
            instance = (T) clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new SerializationException("Error deserializing: Class " + className + " could not be instantiated. Add a public empty constructor", e);
        } catch (ClassCastException e) {
            throw new SerializationException("Error deserializing: Tried to cast class " + className + ", but failed!", e);
        }
        for (Field f : ReflectionUtil.getAllFields(instance.getClass())) {
            if (f.getAnnotation(Transient.class) != null) continue;
            Class<?> fType = f.getType();
            String fName = f.getName();
            if (!f.isAccessible()) {
                try {
                    f.setAccessible(true);
                } catch (SecurityException e) {
                    throw new SerializationException("Error deserializing: Cannot access field " + f.getName() + " from class " + className, e);
                }
            }
            Object value;
            if (Byte.class.equals(fType) || byte.class.equals(fType)) {
                value = bds.getByte(fName);
            } else if (Character.class.equals(fType) || byte.class.equals(fType)) {
                value = bds.getChar(fName);
            } else if (Short.class.equals(fType) || short.class.equals(fType)) {
                value = bds.getShort(fName);
            } else if (Integer.class.equals(fType) || int.class.equals(fType)) {
                value = bds.getInt(fName);
            } else if (Long.class.equals(fType) || long.class.equals(fType)) {
                value = bds.getLong(fName);
            } else if (Float.class.equals(fType) || float.class.equals(fType)) {
                value = bds.getFloat(fName);
            } else if (Double.class.equals(fType) || double.class.equals(fType)) {
                value = bds.getDouble(fName);
            } else if (String.class.equals(fType)) {
                value = bds.getString(fName);
            } else {
                BDS child = bds.getBDS(fName);
                if (child == null) {
                    Integer ref = bds.getInt(BACKREF_PREFIX + className + fName);
                    if (ref == null) {
                        throw new SerializationException("Error deserializing: Field " + fName + " of class " + className + " cannot be deserialized!");
                    } else if (pastInstances.containsKey(ref)) {
                        value = pastInstances.get(ref);
                    } else {
                        value = null;
                        unresolvedReferences.add(new UnresolvedReference(instance, f, ref));
                    }
                } else {
                    value = deserializeInternal(child, pastInstances, unresolvedReferences);
                }
            }
            try {
                f.set(instance, value);
            } catch (IllegalAccessException e) {
                throw new SerializationException("Error deserializing: Could not set value of the field " + fName + " of the class " + className, e);
            }
        }
        if (ROOT_OBJ_TAG.equals(bds.getName())) {
            Set<UnresolvedReference> toReSolve = new HashSet<>(unresolvedReferences);
            while (!toReSolve.isEmpty()) {
                unresolvedReferences = toReSolve;
                toReSolve = Sets.newHashSet();
                for (UnresolvedReference ur : unresolvedReferences) {
                    if (pastInstances.containsKey(ur.idx)) {
                        try {
                            ur.f.set(ur.instance, pastInstances.get(ur.idx));
                        } catch (IllegalAccessException e) {
                            assert false; // Should never happen, as we have made it accessible before
                        }
                    } else if (ur.idx == 0) {
                        try {
                            ur.f.set(ur.instance, instance);
                        } catch (IllegalAccessException e) {
                            assert false; // Should never happen, as we have made it accessible before
                        }
                    } else toReSolve.add(ur);
                }
            }
        }
        pastInstances.put(bds.getInt(IDX_TAG), instance);
        return instance;
    }

    public static <T> T deserialize(byte[] data) throws SerializationException {
        return deserializeInternal(BDS.load(data), Maps.<Integer, Object>newHashMap(), Sets.<UnresolvedReference>newHashSet());
    }

    public static byte[] serialize(Object instance) throws SerializationException {
        return serializeInternal(ROOT_OBJ_TAG, instance, HashMultimap.<Map.Entry<String, Integer>, Map.Entry<Integer, Object>>create(), 0).write();
    }

    private static class UnresolvedReference {
        final Object instance;
        final Field f;
        final int idx;

        public UnresolvedReference(Object instance, Field f, int idx) {
            this.instance = instance;
            this.f = f;
            this.idx = idx;
        }
    }
}
