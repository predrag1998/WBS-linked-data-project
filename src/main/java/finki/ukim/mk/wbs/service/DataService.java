package finki.ukim.mk.wbs.service;

import java.io.IOException;
import java.util.List;

public interface DataService {

    void makeModel(String url);
    void printModel();
    Actor getActorInfo();
    Movie getMovieInfo(String movieUrl,String movieName) throws IOException;
    Composer getComposerInfo(String composerUrl);
}
