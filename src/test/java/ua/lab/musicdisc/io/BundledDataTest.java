package ua.lab.musicdisc.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.lab.musicdisc.disc.Disc;
import ua.lab.musicdisc.model.Composition;

/**
 * Checks that the real data file shipped with the project is valid.
 */
class BundledDataTest {

    private static final Path DATA_FILE = Path.of("data", "compositions.csv");

    @Test
    void bundledFileLoadsAndFitsOnDefaultDisc() {
        List<Composition> loaded = new CsvCompositionSource(DATA_FILE).load();

        Disc disc = new Disc("Check");
        disc.addAll(loaded);

        assertEquals(26, loaded.size());
        assertEquals(Duration.ofSeconds(4544), disc.getTotalDuration());
    }
}