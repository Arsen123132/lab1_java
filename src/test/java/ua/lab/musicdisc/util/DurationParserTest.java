package ua.lab.musicdisc.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DurationParserTest {

    @Test
    void parsesMinutesAndSeconds() {
        assertEquals(Duration.ofSeconds(219), DurationParser.parse("3:39"));
    }

    @Test
    void allowsMoreThan59Minutes() {
        assertEquals(Duration.ofMinutes(80), DurationParser.parse("80:00"));
    }

    @Test
    void ignoresSurroundingSpaces() {
        assertEquals(Duration.ofSeconds(115), DurationParser.parse(" 1:55 "));
    }

    @Test
    void rejectsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> DurationParser.parse(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abc", "1:5", "1:60", "-1:30", "3.30",
            "1:234"})
    void rejectsInvalidFormat(String text) {
        assertThrows(IllegalArgumentException.class,
                () -> DurationParser.parse(text));
    }
}