package ua.lab.musicdisc.app;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MainTest {

    private static String runMain(String... args) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try {
            Main.main(args);
        } finally {
            System.setOut(original);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    @Test
    void usesBundledConfigWhenNoArguments() {
        String output = runMain();

        assertTrue(output.contains("Recorded order:"));
        assertTrue(output.contains("Total duration: 75:44, free: 4:16"));
    }

    @Test
    void usesConfigGivenAsFirstArgument() {
        String output = runMain("missing.properties");

        assertTrue(output.contains("Error: Cannot read config"));
    }
}