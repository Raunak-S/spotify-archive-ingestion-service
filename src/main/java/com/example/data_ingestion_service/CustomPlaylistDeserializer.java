package com.example.data_ingestion_service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class CustomPlaylistDeserializer extends StdDeserializer<Playlist> {
    public CustomPlaylistDeserializer() {
        this(null);
    }

    protected CustomPlaylistDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Playlist deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String description = node.get("description").asText();
        int numFollowers = node.get("num_followers").intValue();
        String name = node.get("original_name").asText();
        String url = node.get("url").asText();
        int numTracks = 0;
        int duration = 0;
        for (JsonNode child : node.get("tracks")) {
            numTracks += 1;
            duration += child.get("duration_ms").intValue();
        }

        return new Playlist(description, numFollowers, name, url, numTracks, duration);
    }
}
