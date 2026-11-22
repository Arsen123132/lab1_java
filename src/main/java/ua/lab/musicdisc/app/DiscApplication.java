package ua.lab.musicdisc.app;

import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import ua.lab.musicdisc.config.AppConfig;
import ua.lab.musicdisc.config.ConfigException;
import ua.lab.musicdisc.disc.Disc;
import ua.lab.musicdisc.disc.DiscFullException;
import ua.lab.musicdisc.io.CompositionLoadException;
import ua.lab.musicdisc.io.CsvCompositionSource;
import ua.lab.musicdisc.model.Composition;
import ua.lab.musicdisc.service.DiscRecorder;
import ua.lab.musicdisc.util.DurationFormatter;

/**
 * Runs the whole scenario: records a disc, rearranges it by style and
 * searches it by track length, printing every step.
 */
public class DiscApplication {

    private final PrintStream out;

    /**
     * Creates the application.
     *
     * @param out stream for all output
     */
    public DiscApplication(PrintStream out) {
        this.out = out;
    }

    /**
     * Runs the scenario using the given configuration file. Problems
     * with the configuration or the data are reported as a message.
     *
     * @param configPath path to the properties file
     */
    public void run(Path configPath) {
        try {
            AppConfig config = AppConfig.load(configPath);
            Disc disc = new DiscRecorder(
                    new CsvCompositionSource(config.compositionsFile()))
                    .record(config.discTitle(), config.capacity());

            out.printf("=== %s (capacity %s) ===%n", disc.getTitle(),
                    DurationFormatter.format(disc.getCapacity()));
            printTracks("Recorded order:", disc.getCompositions());
            out.printf("Total duration: %s, free: %s%n",
                    disc.getFormattedTotalDuration(),
                    DurationFormatter.format(disc.getRemainingTime()));

            disc.arrangeByStyle();
            out.println();
            printTracks("After arranging by style:", disc.getCompositions());

            out.println();
            out.printf("Compositions from %s to %s:%n",
                    DurationFormatter.format(config.searchMin()),
                    DurationFormatter.format(config.searchMax()));
            printList(disc.findByDurationRange(config.searchMin(),
                    config.searchMax()));
        } catch (ConfigException | CompositionLoadException
                | DiscFullException | IllegalArgumentException e) {
            out.println("Error: " + e.getMessage());
        }
    }

    private void printTracks(String heading, List<Composition> tracks) {
        out.println(heading);
        printList(tracks);
    }

    private void printList(List<Composition> tracks) {
        if (tracks.isEmpty()) {
            out.println("  none");
            return;
        }
        for (int i = 0; i < tracks.size(); i++) {
            out.printf("%3d. %s%n", i + 1, tracks.get(i));
        }
    }
}