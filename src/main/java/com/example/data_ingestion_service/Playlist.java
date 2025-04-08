package com.example.data_ingestion_service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.data.repository.CrudRepository;

@JsonDeserialize(using = CustomPlaylistDeserializer.class)
@Entity
public class Playlist {

    @Id
    private String id;
    @Column(columnDefinition = "CLOB")
    private String description;
    private int numFollowers;
    private String name;
    private String url;
    private int numTracks;

    protected Playlist() {}

    public Playlist(String description, int numFollowers, String name, String url, int numTracks) {
        this.description = description;
        this.numFollowers = numFollowers;
        this.name = name;
        this.url = url;
        this.numTracks = numTracks;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumFollowers() {
        return numFollowers;
    }

    public void setNumFollowers(int numFollowers) {
        this.numFollowers = numFollowers;
    }

    public int getNumTracks() {
        return numTracks;
    }

    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", numFollowers=" + numFollowers +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", numTracks=" + numTracks +
                '}';
    }
}


interface PlaylistRepository extends CrudRepository<Playlist, String> {

    Iterable<Playlist> findAllByOrderByNumTracksDesc();

}
