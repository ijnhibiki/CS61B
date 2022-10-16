package ngordnet.ngrams;

import edu.princeton.cs.algs4.In;


import java.util.Collection;
import java.util.HashMap;



/** An object that provides utility methods for making queries on the
 *  Google NGrams dataset (or a subset thereof).
 *
 *  An NGramMap stores pertinent data from a "words file" and a "counts
 *  file". It is not a map in the strict sense, but it does provide additional
 *  functionality.
 *
 *  @author Josh Hug
 */
public class NGramMap {
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
    private HashMap<String, TimeSeries> ngrammaps;
    public NGramMap(String wordsFilename, String countsFilename) {
        In words = new In(wordsFilename);
        In counts = new In(countsFilename);
        ngrammaps = new HashMap<>();
        TimeSeries ngrammap;
        while (words.hasNextChar()) {
            String next = words.readLine();
            String[] newnext = next.split("\t");
            String word = newnext[0];
            int year = Integer.parseInt(newnext[1]);
            double frequency = Double.parseDouble(newnext[2]);
            if (!ngrammaps.containsKey(word)) {
                 ngrammap = new TimeSeries();
                 ngrammap.put(year, frequency);
                 ngrammaps.put(word, ngrammap);
            } else {
                ngrammaps.get(word).put(year, frequency);
            }
        }
        ngrammap = new TimeSeries();
        while (counts.hasNextLine()) {
            String next = counts.readLine();
            String[] newnext = next.split(",");
            int year = Integer.parseInt(newnext[0]);
            double total_number = Double.parseDouble(newnext[1]);
            ngrammap.put(year, total_number);
        }
        ngrammaps.put("total",ngrammap);
    }

    /** Provides the history of WORD. The returned TimeSeries should be a copy,
     *  not a link to this NGramMap's TimeSeries. In other words, changes made
     *  to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word) {

        return ngrammaps.get(word);
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     *  returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other words,
     *  changes made to the object returned by this function should not also affect the
     *  NGramMap. This is also known as a "defensive copy". */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        return new TimeSeries(ngrammaps.get(word), startYear, endYear);
    }

    /** Returns a defensive copy of the total number of words recorded per year in all volumes. */
    public TimeSeries totalCountHistory() {
        return ngrammaps.get("total");
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD compared to
     *  all words recorded in that year. */
    public TimeSeries weightHistory(String word) {
        return ngrammaps.get(word).dividedBy(totalCountHistory());
    }

    /** Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     *  and ENDYEAR, inclusive of both ends. */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        TimeSeries weightHistory = new TimeSeries(ngrammaps.get(word), startYear, endYear);
        weightHistory = weightHistory.dividedBy(totalCountHistory());
        return weightHistory;
    }

    /** Returns the summed relative frequency per year of all words in WORDS. */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        TimeSeries summedWeightHistory = new TimeSeries();
        for (String word : words) {
            if (ngrammaps.containsKey(word)) {
                summedWeightHistory.plus(ngrammaps.get(word));
            }
        }
        summedWeightHistory = summedWeightHistory.dividedBy(totalCountHistory());
        return summedWeightHistory;
    }

    /** Provides the summed relative frequency per year of all words in WORDS
     *  between STARTYEAR and ENDYEAR, inclusive of both ends. If a word does not exist in
     *  this time frame, ignore it rather than throwing an exception. */
    public TimeSeries summedWeightHistory(Collection<String> words,
                              int startYear, int endYear) {
        TimeSeries summedWeightHistory = new TimeSeries();
        for (String word : words) {
            if (ngrammaps.containsKey(word)) {
                TimeSeries temp = new TimeSeries(ngrammaps.get(word), startYear, endYear);
                summedWeightHistory.plus(temp);
            }
        }
        summedWeightHistory = summedWeightHistory.dividedBy(totalCountHistory());
        return summedWeightHistory;
    }


}
