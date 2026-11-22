package ua.lab.musicdisc.util;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses durations written as m:ss.
 */
public final class DurationParser {

    private static final Pattern FORMAT = Pattern.compile("(\\d+):([0-5]\\d)");

    private DurationParser() {
    }

    /**
     * Parses text such as "3:39" or "80:00". Minutes are not limited
     * to 59, seconds must have two digits.
     *
     * @param text text to parse, surrounding spaces are ignored
     * @return parsed duration
     * @throws IllegalArgumentException if the text is null or malformed
     */
    public static Duration parse(String text) {
        if (text == null) {
            throw new IllegalArgumentException(
                    "Duration text must not be null");
        }
        Matcher matcher = FORMAT.matcher(text.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "Invalid duration '" + text + "', expected m:ss");
        }
        long minutes = Long.parseLong(matcher.group(1));
        long seconds = Long.parseLong(matcher.group(2));
        return Duration.ofMinutes(minutes).plusSeconds(seconds);
    }
}