package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;
import ngordnet.ngrams.NGramMap;

public class HyponymsHandler extends NgordnetQueryHandler{
    private WordNet wn;
    private NGramMap ngm;


    public HyponymsHandler (WordNet wn, NGramMap ngm) {
        this.wn = wn;
        this.ngm = ngm;
    }
    @Override
    public String handle(NgordnetQuery q) {
        return "Hello!";
    }
}
