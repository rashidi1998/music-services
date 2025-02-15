package com.example.resourceservice.service;


import com.example.resourceservice.entity.MetadataRecord;
import com.example.resourceservice.repository.MetadataRepository;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;

    public void save(Long resourceId, Metadata metadata) {
        MetadataRecord record = new MetadataRecord();
        record.setResourceId(resourceId);
        record.setValues(convertMetadataToMap(metadata));
        metadataRepository.save(record);
    }

    private Map<String, String> convertMetadataToMap(Metadata metadata) {
        Map<String, String> map = new HashMap<>();
        for (String name : metadata.names()) {
            map.put(name, metadata.get(name));
        }
        return map;
    }
}
