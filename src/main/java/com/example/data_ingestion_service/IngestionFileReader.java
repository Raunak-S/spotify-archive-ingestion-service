package com.example.data_ingestion_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class IngestionFileReader {

    @Autowired
    private PlaylistRepository repository;

    @Value("${service.spotify.archive.root.dir}")
    private String spotifyArchiveRootDir;

    private InputStream getFileAsInputStream(String filename) throws IllegalArgumentException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + filename);
        }

        return inputStream;
    }

    public void readFiles() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(getFileAsInputStream(this.spotifyArchiveRootDir + "playlists/metadata/metadata-compact.json"));
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String metadataJson = reader.readLine();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Map<String, String> metadataMap = objectMapper.readValue(metadataJson, new TypeReference<Map<String, String>>(){});

            List<Playlist> playlistList = new ArrayList<>();
            for (String id : metadataMap.keySet()) {
                Playlist p = objectMapper.readValue(getFileAsInputStream(this.spotifyArchiveRootDir + "playlists/pretty" + id + ".json"), Playlist.class);
                p.setId(id);
                playlistList.add(p);
            }
            this.repository.saveAll(playlistList);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
