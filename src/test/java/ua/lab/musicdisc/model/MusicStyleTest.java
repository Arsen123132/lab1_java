package ua.lab.musicdisc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MusicStyleTest {

    @Test
    void fromStringIgnoresCaseAndSurroundingSpaces() {
        assertEquals(MusicStyle.BREAKCORE,
                MusicStyle.fromString("  breakcore "));
        assertEquals(MusicStyle.POP, MusicStyle.fromString("POP"));
    }

    @Test
    void fromStringAcceptsHyphensAndInnerSpaces() {
        assertEquals(MusicStyle.POST_PUNK,
                MusicStyle.fromString("post-punk"));
        assertEquals(MusicStyle.DRUM_AND_BASS,
                MusicStyle.fromString("drum and bass"));
        assertEquals(MusicStyle.SYNTH_POP,
                MusicStyle.fromString("Synth pop"));
    }

    @Test
    void fromStringRejectsUnknownStyle() {
        assertThrows(IllegalArgumentException.class,
                () -> MusicStyle.fromString("polka"));
    }

    @Test
    void fromStringRejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> MusicStyle.fromString(null));
    }

    @Test
    void displayNameIsReadable() {
        assertEquals("Drum & Bass", MusicStyle.DRUM_AND_BASS.getDisplayName());
        assertEquals("Post-punk", MusicStyle.POST_PUNK.getDisplayName());
    }

    @Test
    void everyStyleHasDisplayName() {
        for (MusicStyle style : MusicStyle.values()) {
            assertFalse(style.getDisplayName().isBlank());
        }
    }
}