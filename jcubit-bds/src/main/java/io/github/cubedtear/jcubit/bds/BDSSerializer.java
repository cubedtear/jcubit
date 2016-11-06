package io.github.cubedtear.jcubit.bds;

/**
 * @author Aritz Lopez
 */
public class BDSSerializer {

    public BDS serialize(Object instance) {
        return serialize(new BDS(""), instance);
    }

    private BDS serialize(BDS bds, Object instance) {

        return bds;
    }
}
