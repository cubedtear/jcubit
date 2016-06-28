package io.github.cubedtear.jcubit.bds;

/**
 * Serializes and deserializes (at least) one type of object into BDS.
 * Useful to simplify the default behavior of {@link BDSUtil#serialize(Object)}.
 * @author Aritz Lopez
 */
public interface Serializer {

    void serialize(Object obj, BDS bds, BDSUtil.BackrefFixer fixer);
    Object deserialize(BDS data);
}
