package io.github.cubedtear.jcubit.bds;

/**
 * Used when a problem happens when serializing or deserializing an object.
 * @author Aritz Lopez
 */
public class SerializationException extends Exception {

    public SerializationException() {
        super();
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }

}
