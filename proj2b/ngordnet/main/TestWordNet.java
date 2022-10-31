package ngordnet.main;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class TestWordNet {
    @Test
    public void testHyponymsSimple() {
        WordNet wn = new WordNet("./data/wordnet/synsets11.txt", "./data/wordnet/hyponyms11.txt");
        assertEquals(Set.of("antihistamine", "actifed"), wn.hyponyms("antihistamine"));
    }

    @Test
    public void testHyponymsdouble() {
        WordNet wn = new WordNet("./data/wordnet/synsets16.txt", "./data/wordnet/hyponyms16.txt");
        assertEquals(Set.of("alteration", "change", "demotion", "increase", "jump", "leap", "modification", "saltation", "transition", "variation"), wn.hyponyms("change"));
    }
    @Test
    public void testHyponymslist() {
        WordNet wn = new WordNet("./data/wordnet/synsets16.txt", "./data/wordnet/hyponyms16.txt");
        List<String> ChangeandOccurrence = new ArrayList<>();
        ChangeandOccurrence.add("change");
        ChangeandOccurrence.add("occurrence");
        assertEquals(Set.of("alteration", "change", "increase", "jump", "leap", "modification", "saltation", "transition"), wn.hyponyms(ChangeandOccurrence));
    }
}
