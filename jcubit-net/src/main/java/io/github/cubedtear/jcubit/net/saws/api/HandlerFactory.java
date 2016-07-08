package io.github.cubedtear.jcubit.net.saws.api;

/**
 * @author Aritz Lopez
 */
public interface HandlerFactory<T> {

    T create(String address, int port);
}

