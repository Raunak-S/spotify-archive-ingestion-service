package com.example.data_ingestion_service;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CustomPlaylistDeserializer extends StdDeserializer<Playlist> {
    public CustomPlaylistDeserializer() {
        this(null);
    }

    protected CustomPlaylistDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Playlist deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        String description = node.get("description").asText();
        int numFollowers = node.get("num_followers").intValue();
        String name = node.get("original_name").asText();
        String url = node.get("url").asText();
        int tracks = node.get("tracks").size();
//        List<Track> tracks = new ArrayList<>();
//        for (JsonNode child : node.get("tracks")) {
//            Track track = new Track();
//            track.setDuration(child.get("duration_ms").intValue());
//            track.setUrl(child.get("url").asText());
//            tracks.add(track);
//        }
        return new Playlist(description, numFollowers, name, url, tracks);
    }
}
