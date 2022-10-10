package finki.ukim.mk.wbs.service;

import lombok.Data;

import java.util.List;

@Data
public class Actor {
    private String actorAbstract;
    private String name;
    private String birthDate;
    private String image;
    private List<String> movies;

    public Actor(String actorAbstract, String name,String birthDate, String image,List<String> movies) {
        this.actorAbstract = actorAbstract;
        this.name = name;
        this.birthDate=birthDate;
        this.image=image;
        this.movies = movies;
    }

    public String getImage() {
        return image;
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
