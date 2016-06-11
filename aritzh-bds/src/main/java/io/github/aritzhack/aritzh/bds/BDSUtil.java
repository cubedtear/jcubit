package io.github.aritzhack.aritzh.bds;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.github.aritzhack.aritzh.logging.OSLogger;
import io.github.aritzhack.aritzh.logging.core.ILogger;
import io.github.aritzhack.aritzh.logging.core.LogLevel;
import io.github.aritzhack.aritzh.util.API;
import io.github.aritzhack.aritzh.util.ReflectionUtil;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author aritzh
 */
public class BDSUtil {

    private static final String CLASS_NAME_TAG = "0__className";
    private static final String ARRAY_TYPE_TAG = "0__arrayType";
    private static final String ARRAY_LENGTH_TAG = "0__arrayLength";
    private static final String PRIMITIVE_VALUE_TAG = "0__value";
    private static final String ROOT_OBJ_TAG = "0__root";
    private static final String BACKREF_PREFIX = "0__back_";
    private static final String IDX_TAG = "0__ID";
    private static final String NULL_TAG = "0__null__";

    public static final BDS NULL_BDS = BDS.createEmpty(NULL_TAG);

    private static ILogger LOG = new OSLogger.Builder(System.out, "SAWS-Serialization").setLevel(LogLevel.INFO).build();

    @API
    public static BDS serialize(Object instance) throws SerializationException {
        return instance == null ? NULL_BDS : serializeInternal2(ROOT_OBJ_TAG, instance, HashMultimap.<Map.Entry<String, Integer>, Map.Entry<Integer, Object>>create(), new IntRef(-1));
    }

    @API
    public static <T> T deserialize(BDS data) throws SerializationException {
        return deserializeInternal(data, Maps.<Integer, Object>newHashMap(), Sets.<UnresolvedReference>newHashSet());
    }

    public static boolean isNull(BDS bds) {
        return bds == null || NULL_TAG.equals(bds.getName());
    }

    private static BDS serializeInternal2(String name, Object instance, Multimap<Map.Entry<String, Integer>, Map.Entry<Integer, Object>> alreadyWritten, IntRef biggest) throws SerializationException {
        if (instance == null) return NULL_BDS;
        Class<?> type = instance.getClass();
        BDS bds = BDS.createEmpty(name);

        bds.addInt(IDX_TAG, ++biggest.value);
        if (Byte.class.equals(type) || byte.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "Byte");
            bds.addByte(PRIMITIVE_VALUE_TAG, (Byte) instance);
        } else if (Character.class.equals(type) || byte.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "Char");
            bds.addChar(PRIMITIVE_VALUE_TAG, (Character) instance);
        } else if (Short.class.equals(type) || short.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "Short");
            bds.addShort(PRIMITIVE_VALUE_TAG, (Short) instance);
        } else if (Integer.class.equals(type) || int.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "Int");
            bds.addInt(PRIMITIVE_VALUE_TAG, (Integer) instance);
        } else if (Long.class.equals(type) || long.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "Long");
            bds.addLong(PRIMITIVE_VALUE_TAG, (Long) instance);
        } else if (Float.class.equals(type) || float.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "Float");
            bds.addFloat(PRIMITIVE_VALUE_TAG, (Float) instance);
        } else if (Double.class.equals(type) || double.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "Double");
            bds.addDouble(PRIMITIVE_VALUE_TAG, (Double) instance);
        } else if (String.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "String");
            bds.addString(PRIMITIVE_VALUE_TAG, (String) instance);
        } else if (File.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "File");
            bds.addString(PRIMITIVE_VALUE_TAG, ((File) instance).getAbsolutePath());
        } else {
            int idx = findCycle(instance, type, alreadyWritten);
            if (idx != -1) bds.addInt(BACKREF_PREFIX + type.getName(), idx);
            else {
                alreadyWritten.put(Maps.immutableEntry(type.getName(), instance.hashCode()), Maps.immutableEntry(biggest.value, instance));

                if (type.isArray()) {
                    bds.addString(CLASS_NAME_TAG, "Array");
                    bds.addString(ARRAY_TYPE_TAG, type.getComponentType().getName());
                    int length = Array.getLength(instance);
                    bds.addInt(ARRAY_LENGTH_TAG, length);
                    for (int i = 0; i < length; i++) {
                        Object o = Array.get(instance, i);
                        BDS element = serializeInternal2("Item" + i, o, alreadyWritten, biggest);
                        bds.addBDS(element);
                    }
                } else if (Collection.class.isAssignableFrom(type)) {
                    Collection c = (Collection) instance;
                    bds.addString(CLASS_NAME_TAG, "Collection");
                    bds.addString(ARRAY_TYPE_TAG, type.getName());
                    bds.addInt(ARRAY_LENGTH_TAG, c.size());
                    Iterator<?> iter = c.iterator();
                    int i = 0;
                    while (iter.hasNext()) {
                        bds.addBDS(serializeInternal2("Item" + i++, iter.next(), alreadyWritten, biggest));
                    }
                } else {
                    for (Field f : ReflectionUtil.getAllFields(type)) {
                        if (f.isAnnotationPresent(Transient.class)) break;
                        if (!f.isAccessible()) {
                            try {
                                f.setAccessible(true);
                            } catch (SecurityException e) {
                                throw new SerializationException("Error serializing: Cannot access field " + f.getName() + " of class " + type, e);
                            }
                        }
                        try {
                            try {
                                bds.addBDS(serializeInternal2(f.getName(), f.get(instance), alreadyWritten, biggest));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } catch (StackOverflowError e) {
                            throw new SerializationException("Most probably a cycle exists in the object structure, and it could not be fixed. Provide a good hashCode and equals to your classes to fix it", e);
                        }
                    }
                }
            }
        }
        return bds;
    }

    private static int findCycle(Object instance, Class<?> type, Multimap<Map.Entry<String, Integer>, Map.Entry<Integer, Object>> alreadyWritten) {
        Integer idx = null;
        Collection<Map.Entry<Integer, Object>> entries = alreadyWritten.get(Maps.immutableEntry(type.getName(), instance.hashCode()));
        for (Map.Entry<Integer, Object> entry : entries) {
            if (entry.getValue().equals(instance)) {
                idx = entry.getKey();
                break;
            }
        }
        if (idx != null) {
            LOG.d("A cycle has been detected! Object of class {} has already been serialized!", type.getName());
            return idx;
        }
        return -1;
    }

    private static <T> T deserializeInternal(BDS bds, Map<Integer, Object> pastInstances, Set<UnresolvedReference> unresolvedReferences) throws SerializationException {
        if (isNull(bds)) return null;

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
            if (f.isAnnotationPresent(Transient.class)) continue;
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

    public static void debug(BDS bds, PrintStream out) {
        debug(bds, out, 0);
    }

    private static void debug(BDS bds, PrintStream out, int level) {
        out.println(Strings.repeat("\t", level) + "BDS: " + bds.getName());
        if (bds.getAllBytes().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Bytes:");
            for (String s : bds.getAllBytes()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + bds.getByte(s));
            }
        }

        if (bds.getAllChars().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Chars:");
            for (String s : bds.getAllChars()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + (int) bds.getChar(s));
            }
        }

        if (bds.getAllShorts().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Shorts:");
            for (String s : bds.getAllShorts()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + bds.getShort(s));
            }
        }

        if (bds.getAllInts().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Ints:");
            for (String s : bds.getAllInts()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + bds.getInt(s));
            }
        }

        if (bds.getAllLongs().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Longs:");
            for (String s : bds.getAllLongs()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + bds.getLong(s));
            }
        }

        if (bds.getAllFloats().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Floats:");
            for (String s : bds.getAllFloats()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + bds.getFloat(s));
            }
        }

        if (bds.getAllDoubles().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Doubles:");
            for (String s : bds.getAllDoubles()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + bds.getDouble(s));
            }
        }

        if (bds.getAllStrings().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Strings:");
            for (String s : bds.getAllStrings()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + bds.getString(s));
            }
        }

        if (bds.getAllByteArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Byte Arrays:");
            for (String s : bds.getAllByteArrays()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + Arrays.toString(bds.getByteArray(s)));
            }
        }

        if (bds.getAllCharArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Char Arrays:");
            for (String s : bds.getAllCharArrays()) {
                out.print(Strings.repeat("\t", level + 2) + s + ": [");
                char[] charArray = bds.getCharArray(s);
                for (int i = 0; i < charArray.length; i++) {
                    char c = charArray[i];
                    out.print((int) c);
                    if (i != charArray.length - 1) out.print(", ");
                }
                out.println("]");
            }
        }

        if (bds.getAllShortArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Short Arrays:");
            for (String s : bds.getAllShortArrays()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + Arrays.toString(bds.getShortArray(s)));
            }
        }

        if (bds.getAllIntArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Int Arrays:");
            for (String s : bds.getAllIntArrays()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + Arrays.toString(bds.getIntArray(s)));
            }
        }

        if (bds.getAllLongArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Long Arrays:");
            for (String s : bds.getAllLongArrays()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + Arrays.toString(bds.getLongArray(s)));
            }
        }

        if (bds.getAllFloatArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Float Arrays:");
            for (String s : bds.getAllFloatArrays()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + Arrays.toString(bds.getFloatArray(s)));
            }
        }

        if (bds.getAllDoubleArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "Double Arrays:");
            for (String s : bds.getAllDoubleArrays()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + Arrays.toString(bds.getDoubleArray(s)));
            }
        }

        if (bds.getAllStringArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "String:");
            for (String s : bds.getAllStringArrays()) {
                out.println(Strings.repeat("\t", level + 2) + s + ": " + Arrays.toString(bds.getStringArray(s)));
            }
        }

        if (bds.getAllBDSs().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "BDSs:");
            for (String s : bds.getAllBDSs()) {
                debug(bds.getBDS(s), out, level + 2);
            }
        }

        if (bds.getAllBDSArrays().size() != 0) {
            out.println(Strings.repeat("\t", level + 1) + "BDS Arrays:");
            for (String s : bds.getAllBDSArrays()) {
                out.println(Strings.repeat("\t", level + 2) + s + " = [");
                for (BDS nBds : bds.getBDSArray(s)) {
                    debug(nBds, out, level + 3);
                }
                out.print(Strings.repeat("\t", level + 1) + "]");
            }
        }
        out.println();
    }

    private static class IntRef {
        int value;
        public IntRef(int value) {
            this.value = value;
        }
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
