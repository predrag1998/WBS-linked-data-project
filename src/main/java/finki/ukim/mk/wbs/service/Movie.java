package finki.ukim.mk.wbs.service;

import lombok.Data;

import java.util.List;

@Data
public class Movie {
    String movieAbstract;
    String name;
    String image;
    String composer;
    String director;
    String genre;
    List<String> actors;
    String imdbUrl;
    String composerNameFromUrl;
    String directorNameFromUrl;

    public Movie(String movieAbstract,String name,String image, String composer, String director,String genre, List<String> actors,String imdbUrl) {
        this.movieAbstract = movieAbstract;
        this.name=name;
        this.image=image;
        this.composer = composer;
        this.director = director;
        this.genre=genre;
        this.actors = actors;
        this.imdbUrl=imdbUrl;
        this.composerNameFromUrl=composer.substring(28).replace("_", " ");
        this.directorNameFromUrl=director.substring(28).replace("_", " ");
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
