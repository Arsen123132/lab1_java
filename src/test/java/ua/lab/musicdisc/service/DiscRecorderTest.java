package ua.lab.musicdisc.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.lab.musicdisc.disc.Disc;
import ua.lab.musicdisc.disc.DiscFullException;
import ua.lab.musicdisc.io.CompositionLoadException;
import ua.lab.musicdisc.io.CompositionSource;
import ua.lab.musicdisc.model.Composition;
import ua.lab.musicdisc.model.MusicStyle;
import ua.lab.musicdisc.model.Song;

@ExtendWith(MockitoExtension.class)
class DiscRecorderTest {

    private static final Composition NEBO = new Song("Небо", "SadSvit",
            Duration.ofSeconds(219), MusicStyle.POST_PUNK, "Ukrainian");
    private static final Composition INHUMAN = new Song("inhuman",
            "usedcvnt", Duration.ofSeconds(127), MusicStyle.DRUM_AND_BASS,
            "English");

    @Mock
    private CompositionSource source;

    @Test
    void recordPutsLoadedCompositionsOnDiscOfDefaultCapacity() {
        when(source.load()).thenReturn(List.of(NEBO, INHUMAN));

        Disc disc = new DiscRecorder(source).record("Mix");

        assertEquals("Mix", disc.getTitle());
        assertEquals(List.of(NEBO, INHUMAN), disc.getCompositions());
        assertEquals(Disc.DEFAULT_CAPACITY, disc.getCapacity());
        verify(source).load();
    }

    @Test
    void recordUsesGivenCapacity() {
        when(source.load()).thenReturn(List.of(NEBO));

        Disc disc = new DiscRecorder(source)
                .record("Mix", Duration.ofMinutes(10));

        assertEquals(Duration.ofMinutes(10), disc.getCapacity());
        assertEquals(1, disc.getCompositions().size());
    }

    @Test
    void recordFailsWhenCompositionsDoNotFit() {
        when(source.load()).thenReturn(List.of(NEBO));
        DiscRecorder recorder = new DiscRecorder(source);

        assertThrows(DiscFullException.class,
                () -> recorder.record("Small", Duration.ofSeconds(200)));
    }

    @Test
    void recordPropagatesLoadFailure() {
        when(source.load()).thenThrow(new CompositionLoadException("boom"));
        DiscRecorder recorder = new DiscRecorder(source);

        assertThrows(CompositionLoadException.class,
                () -> recorder.record("Mix"));
    }

    @Test
    void constructorRejectsNullSource() {
        assertThrows(IllegalArgumentException.class,
                () -> new DiscRecorder(null));
    }
}