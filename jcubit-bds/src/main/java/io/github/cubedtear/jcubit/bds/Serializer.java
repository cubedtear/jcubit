package io.github.cubedtear.jcubit.bds;

/**
 * Serializes and deserializes (at least) one type of object into BDS.
 * Useful to simplify the default behavior of {@link BDSUtil#serialize(Object)}.
 * @author Aritz Lopez
 */
public interface Serializer {

    /**
     * Serializes the given object into the given BDS.
     * The BackrefFixer should be used if serializing the object requires serializing another object,
     * and therefore {@link BDSUtil#serialize(Object, BDSUtil.BackrefFixer)} should be used.
     * @param obj The object to serialize.
     * @param bds The BDS to which the object must be written.
     * @param fixer Used internally to fix circular references.
     */
    void serialize(Object obj, BDS bds, BDSUtil.BackrefFixer fixer);

    /**
     * Deserializes the given BDS as an Object.
     * @param data The BDS from which the object has to be deserialized.
     * @return The deserialized object.
     */
    Object deserialize(BDS data);
}
