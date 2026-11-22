package ua.lab.musicdisc.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import ua.lab.musicdisc.model.ClassicalPiece;
import ua.lab.musicdisc.model.Composition;
import ua.lab.musicdisc.model.InstrumentalPiece;
import ua.lab.musicdisc.model.MusicStyle;
import ua.lab.musicdisc.model.Song;

class CompositionLineParserTest {

    private final CompositionLineParser parser = new CompositionLineParser();

    @Test
    void parsesSong() {
        Composition composition = parser.parse(
                "SONG;Небо;SadSvit;3:39;post-punk;Ukrainian;", 1);

        Song song = assertInstanceOf(Song.class, composition);
        assertEquals("Небо", song.getTitle());
        assertEquals("SadSvit", song.getArtist());
        assertEquals(Duration.ofSeconds(219), song.getDuration());
        assertEquals(MusicStyle.POST_PUNK, song.getStyle());
        assertEquals("Ukrainian", song.getLyricsLanguage());
    }

    @Test
    void parsesInstrumentalPiece() {
        Composition composition = parser.parse(
                "INSTRUMENTAL;Demo;Test Artist;2:00;electronic;Synthesizer;",
                1);

        InstrumentalPiece piece =
                assertInstanceOf(InstrumentalPiece.class, composition);
        assertEquals(MusicStyle.ELECTRONIC, piece.getStyle());
        assertEquals("Synthesizer", piece.getMainInstrument());
    }

    @Test
    void parsesClassicalPieceIgnoringStyleColumn() {
        Composition composition = parser.parse(
                "CLASSICAL;Moonlight Sonata;Test Pianist;4:00;pop;Piano;"
                        + "Beethoven", 1);

        ClassicalPiece piece =
                assertInstanceOf(ClassicalPiece.class, composition);
        assertEquals(MusicStyle.CLASSICAL, piece.getStyle());
        assertEquals("Piano", piece.getMainInstrument());
        assertEquals("Beethoven", piece.getComposer());
    }

    @Test
    void typeIsCaseInsensitive() {
        Composition composition = parser.parse(
                "song;A;B;3:00;pop;English;", 1);

        assertInstanceOf(Song.class, composition);
    }

    @Test
    void rejectsUnknownType() {
        CompositionLoadException exception = assertThrows(
                CompositionLoadException.class, () -> parser.parse(
                        "VIDEO;A;B;3:00;pop;English;", 7));

        assertTrue(exception.getMessage().contains("Line 7"));
        assertTrue(exception.getMessage().contains("VIDEO"));
    }

    @Test
    void rejectsWrongColumnCount() {
        CompositionLoadException exception = assertThrows(
                CompositionLoadException.class,
                () -> parser.parse("SONG;Небо;SadSvit", 4));

        assertTrue(exception.getMessage().contains("Line 4"));
        assertTrue(exception.getMessage().contains("7 columns"));
    }

    @Test
    void rejectsInvalidDuration() {
        assertThrows(CompositionLoadException.class, () -> parser.parse(
                "SONG;A;B;abc;pop;English;", 2));
    }

    @Test
    void rejectsInvalidStyle() {
        assertThrows(CompositionLoadException.class, () -> parser.parse(
                "SONG;A;B;3:00;polka;English;", 2));
    }

    @Test
    void rejectsBlankTitle() {
        assertThrows(CompositionLoadException.class, () -> parser.parse(
                "SONG; ;B;3:00;pop;English;", 2));
    }
}