package ua.lab.musicdisc.app;

import java.nio.file.Path;

/**
 * Entry point. An optional first argument is the path to the
 * configuration file.
 */
public final class Main {

    private static final Path DEFAULT_CONFIG =
            Path.of("data", "app.properties");

    private Main() {
    }

    /**
     * Starts the application.
     *
     * @param args optional path to the configuration file
     */
    public static void main(String[] args) {
        Path configPath = args.length > 0 ? Path.of(args[0]) : DEFAULT_CONFIG;
        new DiscApplication(System.out).run(configPath);
    }
}