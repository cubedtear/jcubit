package io.github.cubedtear.jcubit.bds;

import io.github.cubedtear.jcubit.util.API;

/**
 * Used when a problem happens when serializing or deserializing an object.
 * @author Aritz Lopez
 */
@SuppressWarnings("JavaDoc")
public class SerializationException extends Exception {

    @API
    public SerializationException() {
        super();
    }

    @API
    public SerializationException(String message) {
        super(message);
    }

    @API
    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    @API
    public SerializationException(Throwable cause) {
        super(cause);
    }

}
