package ngordnet.main;

import ngordnet.hugbrowsermagic.NgordnetQuery;
import ngordnet.hugbrowsermagic.NgordnetQueryHandler;

public class HyponymsHandler extends NgordnetQueryHandler{
    @Override
    public String handle(NgordnetQuery q) {
        String response = "Hello!";
        return response;
    }
}
