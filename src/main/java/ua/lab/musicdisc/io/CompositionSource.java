package ua.lab.musicdisc.io;

import java.util.List;
import ua.lab.musicdisc.model.Composition;

/**
 * A provider of compositions, for example a file or a database.
 */
public interface CompositionSource {

    /**
     * Loads all available compositions.
     *
     * @return compositions in source order
     * @throws CompositionLoadException if loading fails
     */
    List<Composition> load();
}