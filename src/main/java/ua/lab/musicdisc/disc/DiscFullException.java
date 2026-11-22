package ua.lab.musicdisc.disc;

/**
 * Thrown when compositions do not fit into the remaining space of a disc.
 */
public class DiscFullException extends IllegalStateException {

    /**
     * Creates the exception.
     *
     * @param message explanation of what did not fit
     */
    public DiscFullException(String message) {
        super(message);
    }
}