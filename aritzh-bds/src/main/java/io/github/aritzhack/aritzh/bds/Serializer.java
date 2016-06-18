package io.github.aritzhack.aritzh.bds;

/**
 * @author aritzh
 */
public interface Serializer {

    void serialize(Object obj, BDS bds, BDSUtil.BackrefFixer fixer);
    Object deserialize(BDS data);
}
