package ua.lab.musicdisc.model;

import java.time.Duration;

/**
 * A classical instrumental work. Its style is always
 * {@link MusicStyle#CLASSICAL}.
 */
public class ClassicalPiece extends InstrumentalPiece {

    private final String composer;

    /**
     * Creates a classical piece.
     *
     * @param title          piece title
     * @param performer      performer or orchestra name
     * @param duration       track length
     * @param mainInstrument leading instrument
     * @param composer       author of the work, not blank
     */
    public ClassicalPiece(String title, String performer, Duration duration,
            String mainInstrument, String composer) {
        super(title, performer, duration, MusicStyle.CLASSICAL,
                mainInstrument);
        this.composer = requireText(composer, "Composer");
    }

    public String getComposer() {
        return composer;
    }

    @Override
    public String getDescription() {
        return "Classical, composer: " + composer + ", instrument: "
                + getMainInstrument();
    }
}