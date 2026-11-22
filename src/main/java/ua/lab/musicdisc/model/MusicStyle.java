package ua.lab.musicdisc.model;

import java.util.Locale;

/**
 * Music styles. The declaration order defines the order in which
 * styles are arranged on a disc.
 */
public enum MusicStyle {
    CLASSICAL("Classical"),
    ROCK("Rock"),
    POST_PUNK("Post-punk"),
    INDIE_POP("Indie pop"),
    SYNTH_POP("Synth-pop"),
    POP("Pop"),
    ELECTRONIC("Electronic"),
    JUNGLE("Jungle"),
    DRUM_AND_BASS("Drum & Bass"),
    BREAKCORE("Breakcore");

    private final String displayName;

    MusicStyle(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Returns a human-readable style name.
     *
     * @return display name of the style
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Finds a style by its name. Case, surrounding spaces, hyphens and
     * inner spaces are tolerated, so "post-punk" and "drum and bass"
     * are valid.
     *
     * @param name style name, for example "synth pop"
     * @return matching style
     * @throws IllegalArgumentException if there is no such style
     */
    public static MusicStyle fromString(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Style name must not be null");
        }
        String normalized = name.trim()
                .toUpperCase(Locale.ROOT)
                .replace('-', '_')
                .replace(' ', '_');
        return MusicStyle.valueOf(normalized);
    }
}