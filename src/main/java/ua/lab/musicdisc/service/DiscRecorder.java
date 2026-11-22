package ua.lab.musicdisc.service;

import java.time.Duration;
import ua.lab.musicdisc.disc.Disc;
import ua.lab.musicdisc.io.CompositionSource;

/**
 * Records compositions from a source onto a new disc.
 */
public class DiscRecorder {

    private final CompositionSource source;

    /**
     * Creates a recorder.
     *
     * @param source provider of compositions, not null
     * @throws IllegalArgumentException if the source is null
     */
    public DiscRecorder(CompositionSource source) {
        if (source == null) {
            throw new IllegalArgumentException("Source must not be null");
        }
        this.source = source;
    }

    /**
     * Records all compositions onto a disc of the default capacity.
     *
     * @param title disc title
     * @return recorded disc
     */
    public Disc record(String title) {
        return record(title, Disc.DEFAULT_CAPACITY);
    }

    /**
     * Records all compositions onto a disc of the given capacity.
     *
     * @param title    disc title
     * @param capacity disc capacity
     * @return recorded disc
     * @throws ua.lab.musicdisc.disc.DiscFullException if they do not fit
     */
    public Disc record(String title, Duration capacity) {
        Disc disc = new Disc(title, capacity);
        disc.addAll(source.load());
        return disc;
    }
}