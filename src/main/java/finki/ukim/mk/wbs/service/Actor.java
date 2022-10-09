package finki.ukim.mk.wbs.service;

import lombok.Data;

import java.util.List;

@Data
public class Actor {
    private String actorAbstract;
    private String name;
    private List<String> movies;

    public Actor(String actorAbstract, String name, List<String> movies) {
        this.actorAbstract = actorAbstract;
        this.name = name;
        this.movies = movies;
    }

    public String getActorAbstract() {
        return actorAbstract;
    }

    public String getName() {
        return name;
    }

    public List<String> getMovies() {
        return movies;
    }
}
