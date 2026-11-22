package ua.lab.musicdisc.config;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Properties;
import ua.lab.musicdisc.util.DurationParser;

/**
 * Application settings read from a UTF-8 properties file.
 *
 * @param discTitle        title of the disc to record
 * @param capacity         capacity of the disc
 * @param compositionsFile file with the compositions to record
 * @param searchMin        lower bound of the track length search
 * @param searchMax        upper bound of the track length search
 */
public record AppConfig(String discTitle, Duration capacity,
        Path compositionsFile, Duration searchMin, Duration searchMax) {

    /**
     * Loads settings from a properties file. Durations are written as
     * m:ss.
     *
     * @param path path to the properties file
     * @return loaded settings
     * @throws ConfigException if the file cannot be read or a property
     *                         is missing or invalid
     */
    public static AppConfig load(Path path) {
        Properties properties = new Properties();
        try (Reader reader =
                Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new ConfigException("Cannot read config " + path, e);
        }
        return new AppConfig(
                required(properties, "disc.title"),
                duration(properties, "disc.capacity"),
                Path.of(required(properties, "compositions.file")),
                duration(properties, "search.min"),
                duration(properties, "search.max"));
    }

    private static String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new ConfigException("Missing property '" + key + "'");
        }
        return value.trim();
    }

    private static Duration duration(Properties properties, String key) {
        try {
            return DurationParser.parse(required(properties, key));
        } catch (IllegalArgumentException e) {
            throw new ConfigException(
                    "Invalid property '" + key + "': " + e.getMessage(), e);
        }
    }
}