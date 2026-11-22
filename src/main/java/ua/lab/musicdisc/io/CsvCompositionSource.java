package ua.lab.musicdisc.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import ua.lab.musicdisc.model.Composition;

/**
 * Reads compositions from a UTF-8 text file with one composition per
 * line. Blank lines and lines starting with '#' are skipped.
 */
public class CsvCompositionSource implements CompositionSource {

    private static final String COMMENT_PREFIX = "#";

    private final Path path;
    private final CompositionLineParser parser = new CompositionLineParser();

    /**
     * Creates a source for the given file.
     *
     * @param path path to the file, not null
     * @throws IllegalArgumentException if the path is null
     */
    public CsvCompositionSource(Path path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        this.path = path;
    }

    @Override
    public List<Composition> load() {
        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CompositionLoadException(
                    "Cannot read file " + path, e);
        }
        List<Composition> compositions = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty() || line.startsWith(COMMENT_PREFIX)) {
                continue;
            }
            compositions.add(parser.parse(line, i + 1));
        }
        return compositions;
    }
}