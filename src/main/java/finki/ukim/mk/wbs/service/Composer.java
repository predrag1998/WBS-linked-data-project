package finki.ukim.mk.wbs.service;

import lombok.Data;

import java.util.List;

@Data
public class Composer {
    String name;
    String birthDate;
    List<String> albums;

    public Composer(String name, String birthDate, List<String> albums) {
        this.name = name;
        this.birthDate = birthDate;
        this.albums = albums;
    }


    public String getName() {
        return name;
    }

    public List<String> getAlbums() {
        return albums;
    }
}
