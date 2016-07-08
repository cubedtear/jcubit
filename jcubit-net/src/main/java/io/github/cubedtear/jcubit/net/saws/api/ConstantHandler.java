package io.github.cubedtear.jcubit.net.saws.api;

/**
 * @author Aritz Lopez
 */
public class ConstantHandler<T> implements HandlerFactory<T> {

    private final T t;

    public ConstantHandler(T t) {
        this.t = t;
    }

    @Override
    public T create(String address, int port) {
        return t;
    }
}
