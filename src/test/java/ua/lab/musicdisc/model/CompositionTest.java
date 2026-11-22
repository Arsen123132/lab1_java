package ua.lab.musicdisc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class CompositionTest {

    private static final Duration LENGTH = Duration.ofSeconds(147);

    private static Song createSong() {
        return new Song("143 ways to lose urself", "usedcvnt", LENGTH,
                MusicStyle.BREAKCORE, "English");
    }

    @Test
    void storesBaseFieldsAndTrimsText() {
        Song song = new Song(" 143 ways to lose urself ", " usedcvnt ",
                LENGTH, MusicStyle.BREAKCORE, "English");

        assertEquals("143 ways to lose urself", song.getTitle());
        assertEquals("usedcvnt", song.getArtist());
        assertEquals(LENGTH, song.getDuration());
        assertEquals(MusicStyle.BREAKCORE, song.getStyle());
    }

    @Test
    void formatsDurationAsMinutesAndSeconds() {
        assertEquals("2:27", createSong().getFormattedDuration());
    }

    @Test
    void formatsShortDurationWithLeadingZero() {
        Song song = new Song("A", "B", Duration.ofSeconds(65),
                MusicStyle.POP, "English");
        assertEquals("1:05", song.getFormattedDuration());
    }

    @Test
    void rejectsBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Song(
                "  ", "usedcvnt", LENGTH, MusicStyle.BREAKCORE, "English"));
    }

    @Test
    void rejectsNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Song(
                null, "usedcvnt", LENGTH, MusicStyle.BREAKCORE, "English"));
    }

    @Test
    void rejectsBlankArtist() {
        assertThrows(IllegalArgumentException.class, () -> new Song(
                "Title", "", LENGTH, MusicStyle.BREAKCORE, "English"));
    }

    @Test
    void rejectsNullDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Song(
                "Title", "usedcvnt", null, MusicStyle.BREAKCORE,
                "English"));
    }

    @Test
    void rejectsZeroDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Song(
                "Title", "usedcvnt", Duration.ZERO, MusicStyle.BREAKCORE,
                "English"));
    }

    @Test
    void rejectsNegativeDuration() {
        assertThrows(IllegalArgumentException.class, () -> new Song(
                "Title", "usedcvnt", Duration.ofSeconds(-5),
                MusicStyle.BREAKCORE, "English"));
    }

    @Test
    void rejectsNullStyle() {
        assertThrows(IllegalArgumentException.class, () -> new Song(
                "Title", "usedcvnt", LENGTH, null, "English"));
    }

    @Test
    void toStringContainsKeyInformation() {
        String text = createSong().toString();

        assertTrue(text.contains("usedcvnt"));
        assertTrue(text.contains("143 ways to lose urself"));
        assertTrue(text.contains("2:27"));
        assertTrue(text.contains("Breakcore"));
    }
}