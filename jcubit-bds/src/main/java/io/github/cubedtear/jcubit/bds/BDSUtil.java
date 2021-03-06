package io.github.cubedtear.jcubit.bds;

import com.google.common.base.Strings;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import io.github.cubedtear.jcubit.util.API;
import io.github.cubedtear.jcubit.util.Nullable;
import io.github.cubedtear.jcubit.util.ReflectionUtil;

import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Collection of utility methods using BDS.
 *
 * @author Aritz Lopez
 * @see BDS
 */
public class BDSUtil {

    private static final String CLASS_NAME_TAG = "0__className";
    private static final String ARRAY_TYPE_TAG = "0__arrayType";
    private static final String ARRAY_LENGTH_TAG = "0__arrayLength";
    private static final String PRIMITIVE_VALUE_TAG = "0__value";
    private static final String ROOT_OBJ_TAG = "0__root";
    private static final String BACKREF_TAG = "0__back_";
    private static final String IDX_TAG = "0__ID";
    private static final String NULL_TAG = "0__null__";
    private static final String PRIMITIVE_ARRAY_VALUE_TAG = "0__primVal";
    private static final String DATE_VALUE_TAG = "0__date__";

    private static final Map<String, Serializer> serializers = Maps.newHashMap();

    /**
     * Registers a serializer to be used when (de)serializing the specified type.
     *
     * @param type       The type that has to be serialized by the given serializer.
     * @param serializer The serializer.
     */
    @API
    public static void registerSerializer(Class<?> type, Serializer serializer) {
        serializers.put(type.getName(), serializer);
    }

    /**
     * Serializes the given object into a BDS.
     * <p>
     * The following types have a special treatment:
     * <ul>
     * <li>Primitive types</li>
     * <li>Wrapper types</li>
     * <li>{@link Date java.util.Date}</li>
     * <li>{@link File java.io.File}</li>
     * <li>All subclasses of {@link Collection java.util.Collection&lt;E&gt;} (See below)</li>
     * </ul>
     * <p>
     * All other types will be serialized by writing each field (private fields included), provided they are not
     * annotated with {@link Transient @Transient}.
     * <p>
     * In order for subclasses of collections to serialize correctly with the default serialization, the class implementing the
     * collection must have a public no-argument constructor. Moreover, if the order is important, the collection must implement
     * the {@link List List&lt;E&gt;} interface. In case this is not possible or it does not work, a custom serialization must be
     * provided.
     * <p>
     * When a type has to be specially serialized, both to minimize the resulting BDS size (because maybe some fields are redundant)
     * or because the default serialization does not work, a {@link Serializer} must be written for the type, and registered through
     * {@link BDSUtil#registerSerializer(Class, Serializer)}.
     * <p>
     * Note: If this method is run from a {@link Serializer}, the usage of the method {@link BDSUtil#serialize(Object, BackrefFixer)} is highly recommended
     * to reduce the size of the result, if there are any cycles in the references.
     *
     * @param instance The object to serialize.
     * @return The BDS version of the given object.
     * @throws SerializationException If an exception occurs when serializing (e.g. There is a cycle that cannot be solved, or a field cannot be accessed).
     * @see BDSUtil#serialize(Object, BackrefFixer)
     * @see BDSUtil#registerSerializer(Class, Serializer)
     */
    @API
    public static BDS serialize(Object instance) throws SerializationException {
        return serialize(instance, null);
    }

    /**
     * Serializes the given object into a BDS. This method may only be run from a {@link Serializer}.
     * Uses the given {@link BackrefFixer} to fix future cycles. Useful when deserializing a custom object requires deserializing
     * another child object.
     * <p>
     * For a full explanation of how serialization works, see {@link BDSUtil#serialize(Object)}
     *
     * @param instance The object to serialize.
     * @param fixer    Reference used internally to fix past references.
     * @return The BDS version of the given object.
     * @throws SerializationException If an exception occurs when serializing (e.g. there is a cycle that cannot be solved, or a field cannot be accessed).
     * @see BDSUtil#serialize(Object)
     */
    @API
    public static BDS serialize(Object instance, @Nullable BackrefFixer fixer) throws SerializationException {
        if (instance == null) {
            BDS result = new BDS(NULL_TAG);
            result.addString(CLASS_NAME_TAG, NULL_TAG);
            return result;
        } else
            return serializeInternal2(ROOT_OBJ_TAG, instance, fixer != null ? fixer.alreadyWritten : HashMultimap.<Map.Entry<String, Integer>, Map.Entry<Integer, Object>>create(), fixer != null ? fixer.biggest : new IntRef(-1));
    }

    /**
     * Deserializes an object serialized with {@link BDSUtil#serialize(Object)}.
     *
     * @param data The serialized object.
     * @return The deserialized object.
     * @throws SerializationException If an exception occurs when deserializing (e.g. there is a cycle that cannot be solved, or a field cannot be accessed).
     */
    @Nullable
    @API
    public static Object deserialize(BDS data) throws SerializationException {
        try {
            String oldName = data.getName();
            data.setName(ROOT_OBJ_TAG);
            Object o = deserializeInternal(data, Maps.<Integer, Object>newHashMap(), Sets.<UnresolvedReference>newHashSet());
            data.setName(oldName);
            return o;
        } catch (CannotDeserializeYet cannotDeserializeYet) {
            throw new AssertionError("Should never happen");
        }
    }

    /**
     * Checks whether the given BDS corresponds to what this class uses to represent null values.
     *
     * @param bds The BDS to check if is the <i>null BDS</i>.
     * @return {@code true} if it is the <i>null BDS</i>.
     */
    public static boolean isNull(BDS bds) {
        return bds == null || NULL_TAG.equals(bds.getString(CLASS_NAME_TAG));
    }

    private static BDS serializeInternal2(String name, Object instance, Multimap<Map.Entry<String, Integer>, Map.Entry<Integer, Object>> alreadyWritten, IntRef biggest) throws SerializationException {
        BDS bds = new BDS(name);
        if (instance == null) {
            bds.addString(CLASS_NAME_TAG, NULL_TAG);
            return bds;
        }

        Class<?> type = instance.getClass();

        bds.addInt(IDX_TAG, ++biggest.value);

        if (serializers.containsKey(type.getName())) {
            int idx = findCycle(instance, type, alreadyWritten);
            if (idx != -1) bds.addInt(BACKREF_TAG, idx);
            else {
                alreadyWritten.put(Maps.immutableEntry(type.getName(), instance.hashCode()), Maps.immutableEntry(biggest.value, instance));
                Serializer serializer = serializers.get(type.getName());
                serializer.serialize(instance, bds, new BackrefFixer(alreadyWritten, biggest));
                if (!bds.addString(CLASS_NAME_TAG, type.getName())) {
                    throw new IllegalStateException("BDS String \"" + CLASS_NAME_TAG + "\" cannot be used when serializing!");
                }
            }
        } else if (Boolean.class.equals(type) || boolean.class.equals(type)) {
            bds.addString(CLASS_NAME_TAG, "Boolean");
            bds.addInt(PRIMITIVE_VALUE_TAG, (Boolean) instance ? 1 : 0);
        } else if (Byte.class.equals(type) || byte.class.equals(type)) {
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
        } else if (instance instanceof Date) {
            long time = ((Date) instance).getTime();
            bds.addString(CLASS_NAME_TAG, "Date");
            bds.addLong(DATE_VALUE_TAG, time);
        } else {
            int idx = findCycle(instance, type, alreadyWritten);
            if (idx != -1) bds.addInt(BACKREF_TAG, idx);
            else {
                alreadyWritten.put(Maps.immutableEntry(type.getName(), instance.hashCode()), Maps.immutableEntry(biggest.value, instance));

                if (type.isArray()) {
                    bds.addString(CLASS_NAME_TAG, "Array");
                    String componentType = type.getComponentType().getName();
                    bds.addString(ARRAY_TYPE_TAG, componentType);
                    int length = Array.getLength(instance);
                    bds.addInt(ARRAY_LENGTH_TAG, length);
                    if (type.getComponentType().isPrimitive()) {
                        if ("boolean".equals(componentType)) {
                            boolean[] bArray = (boolean[]) instance;
                            int[] b2i = new int[bArray.length];
                            for (int i = 0; i < bArray.length; i++) {
                                b2i[i] = bArray[i] ? 1 : 0;
                            }
                            bds.addInts(PRIMITIVE_ARRAY_VALUE_TAG, b2i);
                        } else if ("byte".equals(componentType)) {
                            bds.addBytes(PRIMITIVE_ARRAY_VALUE_TAG, (byte[]) instance);
                        } else if ("short".equals(componentType)) {
                            bds.addShorts(PRIMITIVE_ARRAY_VALUE_TAG, (short[]) instance);
                        } else if ("char".equals(componentType)) {
                            bds.addChars(PRIMITIVE_ARRAY_VALUE_TAG, (char[]) instance);
                        } else if ("int".equals(componentType)) {
                            bds.addInts(PRIMITIVE_ARRAY_VALUE_TAG, (int[]) instance);
                        } else if ("long".equals(componentType)) {
                            bds.addLongs(PRIMITIVE_ARRAY_VALUE_TAG, (long[]) instance);
                        } else if ("float".equals(componentType)) {
                            bds.addFloats(PRIMITIVE_ARRAY_VALUE_TAG, (float[]) instance);
                        } else if ("double".equals(componentType)) {
                            bds.addDoubles(PRIMITIVE_ARRAY_VALUE_TAG, (double[]) instance);
                        } else {
                            throw new AssertionError("Unknown primitive type: " + componentType);
                        }
                    } else {
                        for (int i = 0; i < length; i++) {
                            Object o = Array.get(instance, i);
                            BDS element = serializeInternal2("Item" + i, o, alreadyWritten, biggest);
                            bds.addBDS(element);
                        }
                    }
                } else if (Collection.class.isAssignableFrom(type)) {
                    Collection c = (Collection) instance;
                    bds.addString(CLASS_NAME_TAG, "Collection");
                    bds.addString(ARRAY_TYPE_TAG, type.getName());
                    bds.addInt(ARRAY_LENGTH_TAG, c.size());
                    Iterator<?> iter = c.iterator();
                    int i = 0;
                    while (iter.hasNext()) {
                        Object next = iter.next();
                        bds.addBDS(serializeInternal2("Item" + i++, next, alreadyWritten, biggest));
                    }
                } else {
                    bds.addString(CLASS_NAME_TAG, type.getName());
                    for (Field f : ReflectionUtil.getAllFields(type)) {
                        if (f.isAnnotationPresent(Transient.class) || Modifier.isStatic(f.getModifiers())) continue;
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
                                throw new AssertionError("This should never happen");
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
        return idx != null ? idx : -1;
    }

    @Nullable
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private static Object deserializeInternal(BDS bds, Map<Integer, Object> pastInstances, Set<UnresolvedReference> unresolvedReferences) throws SerializationException, CannotDeserializeYet {
        if (isNull(bds)) return null;

        Object result;

        String type = bds.getString(CLASS_NAME_TAG);

        Integer backRef = bds.getInt(BACKREF_TAG);
        if (type == null) {
            if (backRef == null) {
                throw new SerializationException("Error deserializing: BDS was not in the correct format.");
            } else {
                throw new CannotDeserializeYet(backRef);
            }
        }

        int refId = bds.getInt(IDX_TAG);

        if (serializers.containsKey(type)) {
            result = serializers.get(type).deserialize(bds);
            pastInstances.put(refId, result);
        } else if ("Boolean".equals(type)) {
            result = bds.getInt(PRIMITIVE_VALUE_TAG) == 1;
            pastInstances.put(refId, result);
        } else if ("Byte".equals(type)) {
            result = bds.getByte(PRIMITIVE_VALUE_TAG);
            pastInstances.put(refId, result);
        } else if ("Char".equals(type)) {
            result = bds.getChar(PRIMITIVE_VALUE_TAG);
            pastInstances.put(refId, result);
        } else if ("Short".equals(type)) {
            result = bds.getShort(PRIMITIVE_VALUE_TAG);
            pastInstances.put(refId, result);
        } else if ("Int".equals(type)) {
            result = bds.getInt(PRIMITIVE_VALUE_TAG);
            pastInstances.put(refId, result);
        } else if ("Long".equals(type)) {
            result = bds.getLong(PRIMITIVE_VALUE_TAG);
            pastInstances.put(refId, result);
        } else if ("Float".equals(type)) {
            result = bds.getFloat(PRIMITIVE_VALUE_TAG);
            pastInstances.put(refId, result);
        } else if ("Double".equals(type)) {
            result = bds.getDouble(PRIMITIVE_VALUE_TAG);
            pastInstances.put(refId, result);
        } else if ("String".equals(type)) {
            result = bds.getString(PRIMITIVE_VALUE_TAG);
            pastInstances.put(refId, result);
        } else if ("File".equals(type)) {
            result = new File(bds.getString(PRIMITIVE_VALUE_TAG));
            pastInstances.put(refId, result);
        } else if ("Date".equals(type)) {
            result = new Date();
            ((Date) result).setTime(bds.getLong(DATE_VALUE_TAG));
            pastInstances.put(refId, result);
        } else if ("Array".equals(type)) {
            String componentType = bds.getString(ARRAY_TYPE_TAG);
            if (componentType != null) {
                int length = bds.getInt(ARRAY_LENGTH_TAG);

                if ("boolean".equals(componentType)) {
                    result = new boolean[length];
                    int[] ints = bds.getIntArray(PRIMITIVE_ARRAY_VALUE_TAG);
                    for (int i = 0; i < ints.length; i++) {
                        ((boolean[]) result)[i] = ints[i] == 1;
                    }
                } else if ("byte".equals(componentType)) {
                    result = bds.getByteArray(PRIMITIVE_ARRAY_VALUE_TAG);
                } else if ("short".equals(componentType)) {
                    result = bds.getShortArray(PRIMITIVE_ARRAY_VALUE_TAG);
                } else if ("char".equals(componentType)) {
                    result = bds.getCharArray(PRIMITIVE_ARRAY_VALUE_TAG);
                } else if ("int".equals(componentType)) {
                    result = bds.getIntArray(PRIMITIVE_ARRAY_VALUE_TAG);
                } else if ("long".equals(componentType)) {
                    result = bds.getLongArray(PRIMITIVE_ARRAY_VALUE_TAG);
                } else if ("float".equals(componentType)) {
                    result = bds.getFloatArray(PRIMITIVE_ARRAY_VALUE_TAG);
                } else if ("double".equals(componentType)) {
                    result = bds.getDoubleArray(PRIMITIVE_ARRAY_VALUE_TAG);
                } else {
                    try {
                        Class<?> cType = Class.forName(componentType);
                        result = Array.newInstance(cType, length);
                        pastInstances.put(refId, result);
                        for (int i = 0; i < length; i++) {
                            try {
                                Object element = deserializeInternal(bds.getBDS("Item" + i), pastInstances, unresolvedReferences);
                                Array.set(result, i, element);
                            } catch (CannotDeserializeYet e) {
                                unresolvedReferences.add(new UnresolvedArray(result, i, e.refId));
                            }
                        }
                    } catch (ClassNotFoundException e) {
                        throw new SerializationException("Error deserializing: Class " + componentType + " could not be found.", e);
                    }
                }
            } else {
                int backref = backRef;
                if (pastInstances.containsKey(backref)) {
                    result = pastInstances.get(backref);
                } else throw new CannotDeserializeYet(backref);
            }
        } else if ("Collection".equals(type)) {
            String collectionType = bds.getString(ARRAY_TYPE_TAG);
            if (collectionType != null) {
                int length = bds.getInt(ARRAY_LENGTH_TAG);
                try {
                    Class<?> cType = Class.forName(collectionType);
                    try {
                        Collection collection = (Collection) cType.newInstance();
                        pastInstances.put(refId, collection);
                        for (int i = 0; i < length; i++) {
                            try {
                                Object element = deserializeInternal(bds.getBDS("Item" + i), pastInstances, unresolvedReferences);
                                collection.add(element);
                            } catch (CannotDeserializeYet e) {
                                unresolvedReferences.add(new UnresolvedCol(collection, i, e.refId));
                            }
                        }
                        result = collection;
                    } catch (InstantiationException e) {
                        throw new SerializationException("Error deserializing: Class " + type + " cannot be instantiated. Add a default constructor.", e);
                    } catch (IllegalAccessException e) {
                        throw new SerializationException("Error deserializing: Empty constructor for class " + type + " is not public.", e);
                    }
                } catch (ClassNotFoundException e) {
                    throw new SerializationException("Error deserializing: Class " + type + " could not be found.", e);
                }
            } else {
                int backref = backRef;
                if (pastInstances.containsKey(backref)) {
                    result = pastInstances.get(backref);
                } else throw new CannotDeserializeYet(backref);
            }
        } else {
            try {
                Class<?> clazz = Class.forName(type);
                try {
                    result = clazz.newInstance();
                    pastInstances.put(refId, result);

                    for (Field f : ReflectionUtil.getAllFields(clazz)) {
                        if (f.isAnnotationPresent(Transient.class) || Modifier.isStatic(f.getModifiers())) continue;
                        if (!f.isAccessible()) {
                            try {
                                f.setAccessible(true);
                            } catch (SecurityException e) {
                                throw new SerializationException("Field " + f.getName() + " of class " + type + " could not be made accessible!", e);
                            }
                        }
                        BDS fieldBDS = bds.getBDS(f.getName());
                        try {
                            Object value = deserializeInternal(fieldBDS, pastInstances, unresolvedReferences);
                            f.set(result, value);
                        } catch (CannotDeserializeYet e) {
                            unresolvedReferences.add(new UnresolvedField(result, f, e.refId));
                        }
                    }
                } catch (InstantiationException e) {
                    throw new SerializationException("Error deserializing: Class " + type + " cannot be instantiated. Add a default constructor.", e);
                } catch (IllegalAccessException e) {
                    throw new SerializationException("Error deserializing: Empty constructor for class " + type + " is not public.", e);
                }
            } catch (ClassNotFoundException e) {
                throw new SerializationException("Error deserializing: Class " + type + " could not be found.", e);
            }
        }

        if (ROOT_OBJ_TAG.equals(bds.getName())) {
            Set<UnresolvedReference> toReSolve = new HashSet<>(unresolvedReferences);
            while (!toReSolve.isEmpty()) {
                unresolvedReferences = toReSolve;
                toReSolve = Sets.newHashSet();
                for (UnresolvedReference ur : unresolvedReferences) {
                    if (pastInstances.containsKey(ur.refId) || ur.refId == 0) {
                        Object value = pastInstances.get(ur.refId);
                        if (ur instanceof UnresolvedField) {
                            UnresolvedField urf = (UnresolvedField) ur;
                            try {
                                urf.f.set(urf.instance, value);
                            } catch (IllegalAccessException e) {
                                throw new AssertionError("How did this even happen?");
                            }
                        } else if (ur instanceof UnresolvedArray) {
                            UnresolvedArray ura = (UnresolvedArray) ur;
                            Array.set(ura.array, ura.idx, value);
                        } else if (ur instanceof UnresolvedCol) {
                            UnresolvedCol urc = ((UnresolvedCol) ur);
                            if (urc.collection instanceof List) {
                                ((List) urc.collection).add(urc.idx, value);
                            } else {
                                urc.collection.add(value);
                            }
                        }
                    } else toReSolve.add(ur);
                }
            }
        }
        return result;
    }

    /**
     * Pretty-prints the given BDS into the provided PrintStream.
     *
     * @param bds The BDS to pretty-print.
     * @param out The stream to which the BDS should be printed.
     */
    @API
    public static void debug(BDS bds, PrintStream out) {
        debug(bds, out, 0);
    }

    @SuppressWarnings("ConstantConditions")
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

    /**
     * This class is used in {@link Serializer Serializers} in order to fix cycles in compound objects (made of several others).
     * The only way to get an instance of this class is through the {@link Serializer#serialize(Object, BDS, BackrefFixer)} method.
     * The instance given to the Serializer should be used to call {@link BDSUtil#serialize(Object, BackrefFixer)}, and not the method without
     * the last parameter.
     *
     * @author Aritz Lopez
     */
    public static class BackrefFixer {

        private final Multimap<Map.Entry<String, Integer>, Map.Entry<Integer, Object>> alreadyWritten;
        private final IntRef biggest;

        private BackrefFixer(Multimap<Map.Entry<String, Integer>, Map.Entry<Integer, Object>> alreadyWritten, IntRef biggest) {
            this.alreadyWritten = alreadyWritten;
            this.biggest = biggest;
        }
    }

    private static class IntRef {
        int value;

        public IntRef(int value) {
            this.value = value;
        }
    }

    protected abstract static class UnresolvedReference {
        protected int refId;
    }

    private static class UnresolvedField extends UnresolvedReference {
        final Object instance;
        final Field f;

        public UnresolvedField(Object instance, Field f, int refId) {
            this.instance = instance;
            this.f = f;
            this.refId = refId;
        }
    }

    private static class UnresolvedCol extends UnresolvedReference {
        final Collection collection;
        final int idx;

        private UnresolvedCol(Collection collection, int idx, int refId) {
            this.collection = collection;
            this.idx = idx;
            this.refId = refId;
        }
    }

    private static class UnresolvedArray extends UnresolvedReference {
        final Object array;
        final int idx;

        private UnresolvedArray(Object array, int idx, int refId) {
            this.array = array;
            this.idx = idx;
            this.refId = refId;
        }
    }

    private static class CannotDeserializeYet extends Exception {
        final int refId;

        public CannotDeserializeYet(int refId) {
            this.refId = refId;
        }
    }
}
