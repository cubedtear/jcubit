package io.github.cubedtear.jcubit.bds;

/**
 * @author Aritz Lopez
 */
public interface Serializer {

    void serialize(Object obj, BDS bds, BDSUtil.BackrefFixer fixer);
    Object deserialize(BDS data);
}
