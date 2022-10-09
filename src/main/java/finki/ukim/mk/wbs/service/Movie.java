package finki.ukim.mk.wbs.service;

import lombok.Data;

import java.util.List;

@Data
public class Movie {
    String movieAbstract;
    String composer;
    String director;
    List<String> actors;
    String composerNameFromUrl;

    public Movie(String movieAbstract, String composer, String director, List<String> actors) {
        this.movieAbstract = movieAbstract;
        this.composer = composer;
        this.director = director;
        this.actors = actors;
        this.composerNameFromUrl=composer.substring(28).replace("_", " ");
    }


    @Override
    public String toString() {
        return "Movie{" +
                "movieAbstract='" + movieAbstract + '\'' +
                ", director='" + director + '\'' +
                ", movies=" + actors +
                '}';
    }

}
