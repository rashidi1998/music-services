package com.example.resourceservice.service;

import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MetadataService {

    @Autowired
    private RestTemplate restTemplate;

    private static final String SONG_SERVICE_URL = "http://localhost:8081/songs";
    @Transactional
    public void save(Long resourceId, Metadata metadata) {
        Map<String, String> metadataMap = convertMetadataToMap(metadata);

        Map<String, Object> songData = new HashMap<>();
        songData.put("id", resourceId);
        songData.put("name", metadataMap.get("creator"));
        songData.put("artist", metadataMap.get("xmpDM:artist"));
        songData.put("album", metadataMap.get("xmpDM:album"));
        songData.put("duration", metadataMap.get("xmpDM:duration"));
        songData.put("year", metadataMap.get("xmpDM:releaseDate"));

        restTemplate.postForEntity(SONG_SERVICE_URL, songData, Object.class);
    }


    private Map<String, String> convertMetadataToMap(Metadata metadata) {
        Map<String, String> map = new HashMap<>();
        for (String name : metadata.names()) {
            String value = metadata.get(name);

            if (name.equalsIgnoreCase("xmpDM:duration")) {
                value = convertDuration(value);
            }

            map.put(name, value);
        }
        return map;
    }

    private String convertDuration(String durationInSeconds) {
        try {
            double seconds = Double.parseDouble(durationInSeconds);
            int minutes = (int) (seconds / 60);
            int sec = (int) (seconds % 60);
            return String.format("%02d:%02d", minutes, sec);
        } catch (NumberFormatException e) {
            return durationInSeconds;
        }
    }
    public void deleteMetadata(List<Long> ids) {
        restTemplate.delete(SONG_SERVICE_URL + "?ids=" + String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)))    ;
    }
}
