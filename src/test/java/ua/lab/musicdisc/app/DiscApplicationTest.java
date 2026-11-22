package ua.lab.musicdisc.app;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DiscApplicationTest {

    private static final String CSV = """
            SONG;143 ways to lose urself;usedcvnt;2:27;breakcore;English;
            SONG;Небо;SadSvit;3:39;post-punk;Ukrainian;
            """;

    @TempDir
    private Path tempDir;

    private Path config(String csvPath, String capacity, String min,
            String max) throws IOException {
        String text = String.join("\n",
                "disc.title=Test disc",
                "disc.capacity=" + capacity,
                "compositions.file=" + csvPath,
                "search.min=" + min,
                "search.max=" + max);
        return Files.writeString(tempDir.resolve("app.properties"), text,
                StandardCharsets.UTF_8);
    }

    private Path config(String capacity, String min, String max)
            throws IOException {
        Path csv = Files.writeString(tempDir.resolve("tracks.csv"), CSV,
                StandardCharsets.UTF_8);
        return config(csv.toString().replace('\\', '/'), capacity, min, max);
    }

    private String run(Path configPath) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(buffer, true,
                StandardCharsets.UTF_8);
        new DiscApplication(out).run(configPath);
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void recordsArrangesAndSearches() throws IOException {
        String output = run(config("80:00", "2:00", "2:30"));

        assertTrue(output.contains("=== Test disc (capacity 80:00) ==="));
        assertTrue(output.contains("Total duration: 6:06, free: 73:54"));

        String afterArranging =
                output.substring(output.indexOf("After arranging by style:"));
        assertTrue(afterArranging.indexOf("Небо")
                < afterArranging.indexOf("143 ways"));

        String found = output.substring(
                output.indexOf("Compositions from 2:00 to 2:30:"));
        assertTrue(found.contains("143 ways"));
        assertFalse(found.contains("Небо"));
    }

    @Test
    void reportsWhenNothingMatchesRange() throws IOException {
        String output = run(config("80:00", "5:00", "6:00"));

        assertTrue(output.contains("Compositions from 5:00 to 6:00:"));
        assertTrue(output.contains("  none"));
    }

    @Test
    void reportsMissingConfigFile() {
        String output = run(tempDir.resolve("missing.properties"));

        assertTrue(output.contains("Error: Cannot read config"));
    }

    @Test
    void reportsMissingCompositionsFile() throws IOException {
        String output = run(config(
                tempDir.resolve("nothing.csv").toString().replace('\\', '/'),
                "80:00", "2:00", "2:30"));

        assertTrue(output.contains("Error: Cannot read file"));
    }

    @Test
    void reportsDiscOverflow() throws IOException {
        String output = run(config("1:00", "2:00", "2:30"));

        assertTrue(output.contains("Error:"));
        assertTrue(output.contains("Disc 'Test disc'"));
    }

    @Test
    void reportsInvertedSearchRange() throws IOException {
        String output = run(config("80:00", "3:00", "2:00"));

        assertTrue(output.contains(
                "Error: Minimum must not be greater than maximum"));
    }
}