package ua.lab.musicdisc.config;

/**
 * Thrown when the configuration file is missing or contains
 * invalid values.
 */
public class ConfigException extends RuntimeException {

    /**
     * Creates the exception.
     *
     * @param message description of the problem
     */
    public ConfigException(String message) {
        super(message);
    }

    /**
     * Creates the exception with an underlying cause.
     *
     * @param message description of the problem
     * @param cause   original error
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}