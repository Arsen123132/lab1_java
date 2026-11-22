package ua.lab.musicdisc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class DurationFormatterTest {

    @Test
    void formatsZero() {
        assertEquals("0:00", DurationFormatter.format(Duration.ZERO));
    }

    @Test
    void padsSecondsWithLeadingZero() {
        assertEquals("1:05", DurationFormatter.format(Duration.ofSeconds(65)));
    }

    @Test
    void doesNotLimitMinutesTo59() {
        assertEquals("80:00", DurationFormatter.format(Duration.ofMinutes(80)));
    }

    @Test
    void formatsMixedValue() {
        assertEquals("13:34", DurationFormatter.format(Duration.ofSeconds(814)));
    }
}