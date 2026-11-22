package ua.lab.musicdisc.model;

import java.time.Duration;

/**
 * A composition with vocals.
 */
public class Song extends Composition {

    private final String lyricsLanguage;

    /**
     * Creates a song.
     *
     * @param title          song title
     * @param artist         performer name
     * @param duration       track length
     * @param style          music style
     * @param lyricsLanguage language of the lyrics, not blank
     */
    public Song(String title, String artist, Duration duration,
            MusicStyle style, String lyricsLanguage) {
        super(title, artist, duration, style);
        this.lyricsLanguage = requireText(lyricsLanguage, "Language");
    }

    public String getLyricsLanguage() {
        return lyricsLanguage;
    }

    @Override
    public String getDescription() {
        return "Song, lyrics: " + lyricsLanguage;
    }
}