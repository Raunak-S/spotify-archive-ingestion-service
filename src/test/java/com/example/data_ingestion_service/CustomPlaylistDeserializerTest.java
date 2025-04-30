package com.example.data_ingestion_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomPlaylistDeserializerTest {

    @Test
    public void testDeserializeValidInput() throws IOException {
        String json = "{\"description\":\"Sample playlist\",\"num_followers\":100,\"original_name\":\"Sample\",\"url\":\"https://www.sampleplaylist.com/\",\"tracks\":[{\"duration_ms\":120000,\"url\":\"https://www.sampletrack1.com/\"},{\"duration_ms\":240000,\"url\":\"https://www.sampletrack2.com/\"}]}";
        Playlist p = new ObjectMapper().readValue(json, Playlist.class);

        assertEquals("Sample", p.getName());
        assertEquals("Sample playlist", p.getDescription());
        assertEquals(100, p.getNumFollowers());
        assertEquals(2, p.getNumTracks());
        assertEquals("https://www.sampleplaylist.com/", p.getUrl());
        assertEquals(360000, p.getDuration());
    }

    @Test
    public void testDeserializeMalformedInput() throws IOException {
        String json = "{\"description\":\"Sample playlist\",,,,,\"num_followers\":100,\"original_name\":\"Sample\",\"url\":\"https://www.sampleplaylist.com/\",\"tracks\":[{\"duration_ms\":120000,\"url\":\"https://www.sampletrack1.com/\"},{\"duration_ms\":240000,\"url\":\"https://www.sampletrack2.com/\"}]}";

        assertThrows(IOException.class, () -> new ObjectMapper().readValue(json, Playlist.class));
    }

}
