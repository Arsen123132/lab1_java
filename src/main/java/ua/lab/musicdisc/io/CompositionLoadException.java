package ua.lab.musicdisc.io;

/**
 * Thrown when compositions cannot be read or contain invalid data.
 */
public class CompositionLoadException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param message description of the problem
     */
    public CompositionLoadException(String message) {
        super(message);
    }

    /**
     * Creates the exception with an underlying cause.
     *
     * @param message description of the problem
     * @param cause   original error
     */
    public CompositionLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}