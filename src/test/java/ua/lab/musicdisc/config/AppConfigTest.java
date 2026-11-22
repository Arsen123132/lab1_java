package ua.lab.musicdisc.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AppConfigTest {

    private static final String VALID = """
            disc.title=  Мій диск
            disc.capacity=80:00
            compositions.file=data/compositions.csv
            search.min=2:00
            search.max=2:30
            """;

    @TempDir
    private Path tempDir;

    private Path write(String content) throws IOException {
        return Files.writeString(tempDir.resolve("app.properties"), content,
                StandardCharsets.UTF_8);
    }

    @Test
    void loadsAllValues() throws IOException {
        AppConfig config = AppConfig.load(write(VALID));

        assertEquals("Мій диск", config.discTitle());
        assertEquals(Duration.ofMinutes(80), config.capacity());
        assertEquals(Path.of("data/compositions.csv"),
                config.compositionsFile());
        assertEquals(Duration.ofMinutes(2), config.searchMin());
        assertEquals(Duration.ofSeconds(150), config.searchMax());
    }

    @Test
    void failsWhenFileIsMissing() {
        ConfigException exception = assertThrows(ConfigException.class,
                () -> AppConfig.load(tempDir.resolve("missing.properties")));

        assertTrue(exception.getMessage().contains("Cannot read config"));
    }

    @Test
    void failsWhenPropertyIsMissing() throws IOException {
        Path file = write(VALID.replace("disc.title=  Мій диск\n", ""));

        ConfigException exception = assertThrows(ConfigException.class,
                () -> AppConfig.load(file));

        assertTrue(exception.getMessage().contains("disc.title"));
    }

    @Test
    void failsWhenPropertyIsBlank() throws IOException {
        Path file = write(VALID.replace("search.min=2:00", "search.min="));

        ConfigException exception = assertThrows(ConfigException.class,
                () -> AppConfig.load(file));

        assertTrue(exception.getMessage().contains("search.min"));
    }

    @Test
    void failsWhenDurationIsInvalid() throws IOException {
        Path file = write(
                VALID.replace("disc.capacity=80:00", "disc.capacity=eighty"));

        ConfigException exception = assertThrows(ConfigException.class,
                () -> AppConfig.load(file));

        assertTrue(exception.getMessage().contains("disc.capacity"));
    }
}