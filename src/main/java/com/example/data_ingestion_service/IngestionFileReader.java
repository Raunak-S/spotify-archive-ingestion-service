package com.example.data_ingestion_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Component
public class IngestionFileReader implements ApplicationRunner {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private PlaylistRepository repository;

    private InputStream getFileAsInputStream(String filename) throws IllegalArgumentException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(filename);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found! " + filename);
        }

        return inputStream;
    }

    private void readFiles() {
        try (InputStreamReader inputStreamReader = new InputStreamReader(getFileAsInputStream("playlist-src/metadata-compact.json"));
             BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String metadataJson = reader.readLine();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Map<String, String> metadataMap = objectMapper.readValue(metadataJson, new TypeReference<Map<String, String>>(){});

            for (String id : metadataMap.keySet()) {
                Playlist p = objectMapper.readValue(applicationContext.getResource("classpath:playlist-src/playlists/" + id + ".json").getFile(), Playlist.class);
                p.setId(id);
                this.repository.save(p);
            }
//            System.out.println(this.repository.findById("01WIu4Rst0xeZnTunWxUL7"));

            System.out.println(this.repository.findAllByOrderByNumTracksDesc().iterator().next());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.readFiles();
    }
}
