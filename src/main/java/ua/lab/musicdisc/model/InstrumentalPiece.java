package ua.lab.musicdisc.model;

import java.time.Duration;

/**
 * A composition without vocals, performed on a main instrument.
 */
public class InstrumentalPiece extends Composition {

    private final String mainInstrument;

    /**
     * Creates an instrumental piece.
     *
     * @param title          piece title
     * @param artist         performer name
     * @param duration       track length
     * @param style          music style
     * @param mainInstrument leading instrument, not blank
     */
    public InstrumentalPiece(String title, String artist, Duration duration,
            MusicStyle style, String mainInstrument) {
        super(title, artist, duration, style);
        this.mainInstrument = requireText(mainInstrument, "Instrument");
    }

    public String getMainInstrument() {
        return mainInstrument;
    }

    @Override
    public String getDescription() {
        return "Instrumental, main instrument: " + mainInstrument;
    }
}