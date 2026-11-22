package ua.lab.musicdisc.util;

import java.time.Duration;

/**
 * Formats durations for display.
 */
public final class DurationFormatter {

    private DurationFormatter() {
    }

    /**
     * Formats a duration as m:ss, where minutes are not limited to 59.
     *
     * @param duration duration to format, not negative
     * @return text such as "3:05" or "80:00"
     */
    public static String format(Duration duration) {
        return String.format("%d:%02d", duration.toMinutes(),
                duration.toSecondsPart());
    }
}