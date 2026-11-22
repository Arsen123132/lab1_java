package ua.lab.musicdisc.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class CompositionTypesTest {

    private static final Duration LENGTH = Duration.ofSeconds(219);

    @Test
    void songExposesLanguageAndDescription() {
        Song song = new Song("Небо", "SadSvit", LENGTH,
                MusicStyle.SYNTH_POP, "Ukrainian");

        assertEquals("Небо", song.getTitle());
        assertEquals("Ukrainian", song.getLyricsLanguage());
        assertTrue(song.getDescription().contains("Ukrainian"));
    }

    @Test
    void songRejectsBlankLanguage() {
        assertThrows(IllegalArgumentException.class, () -> new Song(
                "Небо", "SadSvit", LENGTH, MusicStyle.SYNTH_POP, " "));
    }

    @Test
    void instrumentalPieceExposesInstrumentAndDescription() {
        InstrumentalPiece piece = new InstrumentalPiece("Demo Track",
                "Test Artist", LENGTH, MusicStyle.ELECTRONIC, "Synthesizer");

        assertEquals("Synthesizer", piece.getMainInstrument());
        assertTrue(piece.getDescription().contains("Synthesizer"));
    }

    @Test
    void instrumentalPieceRejectsBlankInstrument() {
        assertThrows(IllegalArgumentException.class,
                () -> new InstrumentalPiece("Demo Track", "Test Artist",
                        LENGTH, MusicStyle.ELECTRONIC, null));
    }

    @Test
    void classicalPieceHasClassicalStyleAndComposer() {
        ClassicalPiece piece = new ClassicalPiece("Moonlight Sonata",
                "Test Pianist", LENGTH, "Piano", "Beethoven");

        assertEquals(MusicStyle.CLASSICAL, piece.getStyle());
        assertEquals("Beethoven", piece.getComposer());
        assertEquals("Piano", piece.getMainInstrument());
    }

    @Test
    void classicalPieceDescriptionMentionsComposerAndInstrument() {
        ClassicalPiece piece = new ClassicalPiece("Moonlight Sonata",
                "Test Pianist", LENGTH, "Piano", "Beethoven");

        assertTrue(piece.getDescription().contains("Beethoven"));
        assertTrue(piece.getDescription().contains("Piano"));
    }

    @Test
    void classicalPieceRejectsBlankComposer() {
        assertThrows(IllegalArgumentException.class,
                () -> new ClassicalPiece("T", "A", LENGTH, "Piano", ""));
    }

    @Test
    void classicalPieceIsInstrumentalComposition() {
        Composition piece = new ClassicalPiece("T", "A", LENGTH, "Violin",
                "Bach");

        assertInstanceOf(InstrumentalPiece.class, piece);
    }
}