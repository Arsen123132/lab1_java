package ua.lab.musicdisc.io;

import java.time.Duration;
import java.util.Locale;
import ua.lab.musicdisc.model.ClassicalPiece;
import ua.lab.musicdisc.model.Composition;
import ua.lab.musicdisc.model.InstrumentalPiece;
import ua.lab.musicdisc.model.MusicStyle;
import ua.lab.musicdisc.model.Song;
import ua.lab.musicdisc.util.DurationParser;

/**
 * Converts one line of the form
 * {@code type;title;artist;duration;style;detail;composer}
 * into a composition.
 *
 * <p>Detail is the lyrics language for SONG and the main instrument for
 * INSTRUMENTAL and CLASSICAL. Composer is used only by CLASSICAL, which
 * ignores the style column because its style is always classical.
 */
public class CompositionLineParser {

    private static final String SEPARATOR = ";";
    private static final int COLUMN_COUNT = 7;

    /**
     * Parses one line.
     *
     * @param line       line to parse
     * @param lineNumber line number in the file, used in error messages
     * @return created composition
     * @throws CompositionLoadException if the line is invalid
     */
    public Composition parse(String line, int lineNumber) {
        String[] columns = line.split(SEPARATOR, -1);
        if (columns.length != COLUMN_COUNT) {
            throw new CompositionLoadException(String.format(
                    "Line %d: expected %d columns but found %d",
                    lineNumber, COLUMN_COUNT, columns.length));
        }
        try {
            return create(columns);
        } catch (IllegalArgumentException e) {
            throw new CompositionLoadException(String.format(
                    "Line %d: %s", lineNumber, e.getMessage()), e);
        }
    }

    private Composition create(String[] columns) {
        String type = columns[0].trim().toUpperCase(Locale.ROOT);
        String title = columns[1];
        String artist = columns[2];
        Duration duration = DurationParser.parse(columns[3]);
        String detail = columns[5];
        return switch (type) {
            case "SONG" -> new Song(title, artist, duration,
                    MusicStyle.fromString(columns[4]), detail);
            case "INSTRUMENTAL" -> new InstrumentalPiece(title, artist,
                    duration, MusicStyle.fromString(columns[4]), detail);
            case "CLASSICAL" -> new ClassicalPiece(title, artist, duration,
                    detail, columns[6]);
            default -> throw new IllegalArgumentException(
                    "Unknown composition type '" + columns[0].trim() + "'");
        };
    }
}