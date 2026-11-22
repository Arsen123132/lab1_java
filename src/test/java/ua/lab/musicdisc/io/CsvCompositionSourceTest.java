package ua.lab.musicdisc.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ua.lab.musicdisc.model.Composition;
import ua.lab.musicdisc.model.InstrumentalPiece;
import ua.lab.musicdisc.model.Song;

class CsvCompositionSourceTest {

    @TempDir
    private Path tempDir;

    private Path write(String content) throws IOException {
        return Files.writeString(tempDir.resolve("test.csv"), content,
                StandardCharsets.UTF_8);
    }

    @Test
    void loadsCompositionsSkippingCommentsAndBlankLines() throws IOException {
        Path file = write("""
                # type;title;artist;duration;style;detail;composer

                SONG;Небо;SadSvit;3:39;post-punk;Ukrainian;
                   
                INSTRUMENTAL;Demo;Test Artist;2:00;electronic;Synthesizer;
                """);

        List<Composition> loaded = new CsvCompositionSource(file).load();

        assertEquals(2, loaded.size());
        assertInstanceOf(Song.class, loaded.get(0));
        assertEquals("Небо", loaded.get(0).getTitle());
        assertInstanceOf(InstrumentalPiece.class, loaded.get(1));
    }

    @Test
    void returnsEmptyListForEmptyFile() throws IOException {
        Path file = write("");

        assertTrue(new CsvCompositionSource(file).load().isEmpty());
    }

    @Test
    void reportsLineNumberOfInvalidRecord() throws IOException {
        Path file = write("""
                # header
                SONG;Небо;SadSvit;3:39;post-punk;Ukrainian;
                SONG;Bad;Artist;abc;pop;English;
                """);
        CsvCompositionSource source = new CsvCompositionSource(file);

        CompositionLoadException exception = assertThrows(
                CompositionLoadException.class, source::load);

        assertTrue(exception.getMessage().contains("Line 3"));
    }

    @Test
    void failsWhenFileIsMissing() {
        CsvCompositionSource source =
                new CsvCompositionSource(tempDir.resolve("missing.csv"));

        assertThrows(CompositionLoadException.class, source::load);
    }

    @Test
    void rejectsNullPath() {
        assertThrows(IllegalArgumentException.class,
                () -> new CsvCompositionSource(null));
    }
}