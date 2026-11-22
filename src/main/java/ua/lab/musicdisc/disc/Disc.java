package ua.lab.musicdisc.disc;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import ua.lab.musicdisc.model.Composition;
import ua.lab.musicdisc.util.DurationFormatter;

/**
 * An audio disc with limited capacity that holds a collection of
 * compositions in a fixed order.
 */
public class Disc {

    /** Capacity of a standard audio CD. */
    public static final Duration DEFAULT_CAPACITY = Duration.ofMinutes(80);

    private final String title;
    private final Duration capacity;
    private final List<Composition> compositions = new ArrayList<>();

    /**
     * Creates a disc with the default capacity of 80 minutes.
     *
     * @param title disc title, not blank
     */
    public Disc(String title) {
        this(title, DEFAULT_CAPACITY);
    }

    /**
     * Creates a disc with the given capacity.
     *
     * @param title    disc title, not blank
     * @param capacity maximum total duration, must be positive
     * @throws IllegalArgumentException if any argument is invalid
     */
    public Disc(String title, Duration capacity) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        if (capacity == null || capacity.isZero() || capacity.isNegative()) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.title = title.trim();
        this.capacity = capacity;
    }

    /**
     * Records one composition at the end of the disc.
     *
     * @param composition composition to record, not null
     * @throws IllegalArgumentException if the composition is null
     * @throws DiscFullException        if there is not enough free space
     */
    public void addComposition(Composition composition) {
        addAll(Collections.singletonList(composition));
    }

    /**
     * Records several compositions at the end of the disc. The operation
     * is atomic: if they do not fit together, nothing is recorded.
     *
     * @param newCompositions compositions to record, none of them null
     * @throws IllegalArgumentException if the collection or any element
     *                                  is null
     * @throws DiscFullException        if there is not enough free space
     */
    public void addAll(Collection<? extends Composition> newCompositions) {
        if (newCompositions == null) {
            throw new IllegalArgumentException(
                    "Compositions must not be null");
        }
        Duration required = Duration.ZERO;
        for (Composition composition : newCompositions) {
            if (composition == null) {
                throw new IllegalArgumentException(
                        "Composition must not be null");
            }
            required = required.plus(composition.getDuration());
        }
        Duration remaining = getRemainingTime();
        if (required.compareTo(remaining) > 0) {
            throw new DiscFullException(String.format(
                    "Disc '%s' has %s left, but %s is required", title,
                    DurationFormatter.format(remaining),
                    DurationFormatter.format(required)));
        }
        compositions.addAll(newCompositions);
    }

    /**
     * Returns the total duration of all recorded compositions.
     *
     * @return total duration, zero for an empty disc
     */
    public Duration getTotalDuration() {
        return compositions.stream()
                .map(Composition::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

    /**
     * Returns the free time left on the disc.
     *
     * @return remaining duration
     */
    public Duration getRemainingTime() {
        return capacity.minus(getTotalDuration());
    }

    /**
     * Returns the total duration formatted as m:ss.
     *
     * @return formatted total duration
     */
    public String getFormattedTotalDuration() {
        return DurationFormatter.format(getTotalDuration());
    }

    /**
     * Rearranges compositions so that tracks of the same style go
     * together. Styles follow the declaration order of
     * {@link ua.lab.musicdisc.model.MusicStyle}; the order of tracks
     * inside one style is preserved.
     */
    public void arrangeByStyle() {
        compositions.sort(Comparator.comparing(Composition::getStyle));
    }

    /**
     * Finds compositions whose length lies within the given range.
     *
     * @param min lower bound, inclusive
     * @param max upper bound, inclusive
     * @return matching compositions in disc order, possibly empty
     * @throws IllegalArgumentException if a bound is null or min is
     *                                  greater than max
     */
    public List<Composition> findByDurationRange(Duration min,
            Duration max) {
        if (min == null || max == null) {
            throw new IllegalArgumentException("Bounds must not be null");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException(
                    "Minimum must not be greater than maximum");
        }
        return compositions.stream()
                .filter(c -> c.getDuration().compareTo(min) >= 0
                        && c.getDuration().compareTo(max) <= 0)
                .toList();
    }

    public String getTitle() {
        return title;
    }

    public Duration getCapacity() {
        return capacity;
    }

    /**
     * Returns a read-only view of the recorded compositions.
     *
     * @return compositions in disc order
     */
    public List<Composition> getCompositions() {
        return Collections.unmodifiableList(compositions);
    }
}