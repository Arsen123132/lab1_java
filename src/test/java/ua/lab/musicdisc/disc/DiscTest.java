package ua.lab.musicdisc.disc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import ua.lab.musicdisc.model.Composition;
import ua.lab.musicdisc.model.MusicStyle;
import ua.lab.musicdisc.model.Song;

class DiscTest {

    private static final Composition NEBO = song("Небо", "SadSvit", 219,
            MusicStyle.POST_PUNK, "Ukrainian");
    private static final Composition INHUMAN = song("inhuman", "usedcvnt",
            127, MusicStyle.BREAKCORE, "English");
    private static final Composition SILHOUETTES = song("Силуети", "SadSvit",
            177, MusicStyle.SYNTH_POP, "Ukrainian");
    private static final Composition WAYS_TO_LOSE = song(
            "143 ways to lose urself", "usedcvnt", 147,
            MusicStyle.BREAKCORE, "English");
    private static final Composition CASSETTE = song("Касета", "SadSvit",
            144, MusicStyle.INDIE_POP, "Ukrainian");

    private static Song song(String title, String artist, int seconds,
            MusicStyle style, String language) {
        return new Song(title, artist, Duration.ofSeconds(seconds), style,
                language);
    }

    private static Disc filledDisc() {
        Disc disc = new Disc("Mix");
        disc.addAll(List.of(NEBO, INHUMAN, SILHOUETTES, WAYS_TO_LOSE,
                CASSETTE));
        return disc;
    }

    private static List<String> titles(List<Composition> compositions) {
        return compositions.stream().map(Composition::getTitle).toList();
    }

    @Test
    void constructorUsesDefaultCapacityAndTrimsTitle() {
        Disc disc = new Disc(" Mix ");

        assertEquals("Mix", disc.getTitle());
        assertEquals(Duration.ofMinutes(80), disc.getCapacity());
    }

    @Test
    void constructorRejectsNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Disc(null));
    }

    @Test
    void constructorRejectsBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> new Disc("  "));
    }

    @Test
    void constructorRejectsNullCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disc("Mix", null));
    }

    @Test
    void constructorRejectsZeroCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disc("Mix", Duration.ZERO));
    }

    @Test
    void constructorRejectsNegativeCapacity() {
        assertThrows(IllegalArgumentException.class,
                () -> new Disc("Mix", Duration.ofMinutes(-1)));
    }

    @Test
    void newDiscIsEmpty() {
        Disc disc = new Disc("Mix");

        assertTrue(disc.getCompositions().isEmpty());
        assertEquals(Duration.ZERO, disc.getTotalDuration());
        assertEquals(Duration.ofMinutes(80), disc.getRemainingTime());
    }

    @Test
    void addCompositionAppendsTrack() {
        Disc disc = new Disc("Mix");
        disc.addComposition(NEBO);

        assertEquals(List.of(NEBO), disc.getCompositions());
        assertEquals(Duration.ofSeconds(219), disc.getTotalDuration());
    }

    @Test
    void addCompositionRejectsNull() {
        Disc disc = new Disc("Mix");

        assertThrows(IllegalArgumentException.class,
                () -> disc.addComposition(null));
    }

    @Test
    void addCompositionRejectsOverflow() {
        Disc disc = new Disc("Small", Duration.ofSeconds(300));
        disc.addComposition(WAYS_TO_LOSE);
        disc.addComposition(INHUMAN);

        DiscFullException exception = assertThrows(DiscFullException.class,
                () -> disc.addComposition(CASSETTE));

        assertEquals(2, disc.getCompositions().size());
        assertTrue(exception.getMessage().contains("Small"));
    }

    @Test
    void addCompositionAllowsExactFit() {
        Disc disc = new Disc("Exact", Duration.ofSeconds(274));
        disc.addComposition(WAYS_TO_LOSE);
        disc.addComposition(INHUMAN);

        assertEquals(Duration.ZERO, disc.getRemainingTime());
    }

    @Test
    void addAllAddsEveryCompositionAndCountsTime() {
        Disc disc = filledDisc();

        assertEquals(5, disc.getCompositions().size());
        assertEquals(Duration.ofSeconds(814), disc.getTotalDuration());
        assertEquals(Duration.ofSeconds(3986), disc.getRemainingTime());
        assertEquals("13:34", disc.getFormattedTotalDuration());
    }

    @Test
    void addAllRejectsNullCollection() {
        Disc disc = new Disc("Mix");

        assertThrows(IllegalArgumentException.class,
                () -> disc.addAll(null));
    }

    @Test
    void addAllRejectsNullElement() {
        Disc disc = new Disc("Mix");

        assertThrows(IllegalArgumentException.class,
                () -> disc.addAll(Arrays.asList(NEBO, null)));
        assertTrue(disc.getCompositions().isEmpty());
    }

    @Test
    void addAllIsAtomicWhenOverflow() {
        Disc disc = new Disc("Small", Duration.ofSeconds(300));

        assertThrows(DiscFullException.class, () -> disc.addAll(
                List.of(WAYS_TO_LOSE, INHUMAN, CASSETTE)));
        assertTrue(disc.getCompositions().isEmpty());
    }

    @Test
    void compositionsListIsUnmodifiable() {
        Disc disc = filledDisc();

        assertThrows(UnsupportedOperationException.class,
                () -> disc.getCompositions().add(NEBO));
    }

    @Test
    void arrangeByStyleGroupsStylesInEnumOrderAndKeepsOrderInsideStyle() {
        Disc disc = filledDisc();

        disc.arrangeByStyle();

        assertEquals(List.of("Небо", "Касета", "Силуети", "inhuman",
                "143 ways to lose urself"), titles(disc.getCompositions()));
    }

    @Test
    void findByDurationRangeIsInclusiveAndKeepsDiscOrder() {
        Disc disc = filledDisc();

        List<Composition> found = disc.findByDurationRange(
                Duration.ofSeconds(144), Duration.ofSeconds(177));

        assertEquals(List.of("Силуети", "143 ways to lose urself",
                "Касета"), titles(found));
    }

    @Test
    void findByDurationRangeReturnsEmptyWhenNothingMatches() {
        Disc disc = filledDisc();

        assertTrue(disc.findByDurationRange(Duration.ofSeconds(300),
                Duration.ofSeconds(400)).isEmpty());
    }

    @Test
    void findByDurationRangeWorksWithEqualBounds() {
        Disc disc = filledDisc();

        List<Composition> found = disc.findByDurationRange(
                Duration.ofSeconds(127), Duration.ofSeconds(127));

        assertEquals(List.of(INHUMAN), found);
    }

    @Test
    void findByDurationRangeRejectsNullMinimum() {
        Disc disc = filledDisc();

        assertThrows(IllegalArgumentException.class,
                () -> disc.findByDurationRange(null, Duration.ofSeconds(10)));
    }

    @Test
    void findByDurationRangeRejectsNullMaximum() {
        Disc disc = filledDisc();

        assertThrows(IllegalArgumentException.class,
                () -> disc.findByDurationRange(Duration.ofSeconds(10), null));
    }

    @Test
    void findByDurationRangeRejectsMinimumGreaterThanMaximum() {
        Disc disc = filledDisc();

        assertThrows(IllegalArgumentException.class,
                () -> disc.findByDurationRange(Duration.ofSeconds(200),
                        Duration.ofSeconds(100)));
    }
}