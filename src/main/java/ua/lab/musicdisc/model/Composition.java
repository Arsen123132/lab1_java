package ua.lab.musicdisc.model;

import java.time.Duration;
import ua.lab.musicdisc.util.DurationFormatter;

/**
 * Base class for any music composition that can be recorded on a disc.
 */
public abstract class Composition {

    private final String title;
    private final String artist;
    private final Duration duration;
    private final MusicStyle style;

    /**
     * Creates a composition.
     *
     * @param title    composition title, not blank
     * @param artist   performer name, not blank
     * @param duration track length, must be positive
     * @param style    music style, not null
     * @throws IllegalArgumentException if any argument is invalid
     */
    protected Composition(String title, String artist, Duration duration,
            MusicStyle style) {
        this.title = requireText(title, "Title");
        this.artist = requireText(artist, "Artist");
        if (duration == null || duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException(
                    "Duration must be positive");
        }
        if (style == null) {
            throw new IllegalArgumentException("Style must not be null");
        }
        this.duration = duration;
        this.style = style;
    }

    /**
     * Validates that a text value is neither null nor blank.
     *
     * @param value value to check
     * @param field field name used in the error message
     * @return trimmed value
     */
    protected static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    field + " must not be blank");
        }
        return value.trim();
    }

    /**
     * Returns type-specific details of the composition.
     *
     * @return short description of the composition kind
     */
    public abstract String getDescription();

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public Duration getDuration() {
        return duration;
    }

    public MusicStyle getStyle() {
        return style;
    }

        public String getFormattedDuration() {
        return DurationFormatter.format(duration);
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s, %s) [%s]", artist, title,
                style.getDisplayName(), getFormattedDuration(),
                getDescription());
    }
}