package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;
import ngordnet.plotting.Plotter;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class HypohistHandler extends NgordnetQueryHandler {
    private WordNet wn;
    private NGramMap ngm;


    public HypohistHandler(WordNet wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        if (k == 0) {
            k = 5;
        }
        ArrayList<TimeSeries> lts = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        ArrayList<String> wordlist;

        wordlist = wn.khyponyms(words, startYear, endYear, k, ngm);

        for (String word: wordlist) {
            TimeSeries newline = ngm.weightHistory(word, startYear, endYear);
            labels.add(word);
            lts.add(newline);
        }
        XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);
        return encodedImage;
    }
}
