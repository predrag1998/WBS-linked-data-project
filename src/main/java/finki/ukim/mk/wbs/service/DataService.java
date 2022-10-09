package finki.ukim.mk.wbs.service;

import java.util.List;

public interface DataService {

    void makeModel(String url);
    void printModel();
    Actor getActorInfo();
    Movie getMovieInfo(String movieUrl);
    Composer getComposerInfo(String composerUrl);
}
