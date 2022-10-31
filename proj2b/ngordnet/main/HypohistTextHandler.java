package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;
import ngordnet.ngrams.TimeSeries;

import java.util.ArrayList;
import java.util.List;

public class HypohistTextHandler extends NgordnetQueryHandler {
    private WordNet wn;
    private NGramMap ngm;


    public HypohistTextHandler (WordNet wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }

    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();
        String response = "";
        ArrayList<String> wordlist;

        wordlist = wn.khyponyms(words, startYear, endYear, k, ngm);
        for (String word : wordlist) {
            TimeSeries newline = ngm.weightHistory(word, startYear, endYear);
            response += word;
            response += newline.toString() + "\n";
        }
        return response;
    }



}
