package com.example.resourceservice.service;

import com.example.resourceservice.entity.Resource;
import com.example.resourceservice.exceptions.CustomValidationException;
import com.example.resourceservice.exceptions.EmptyFileException;
import com.example.resourceservice.exceptions.MetadataExtractException;
import com.example.resourceservice.exceptions.ResourceNotFoundException;
import com.example.resourceservice.repository.ResourceRepository;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.ContentHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private MetadataService metadataService;

    @Transactional
    public Long storeFile(byte[] audioData) throws EmptyFileException, MetadataExtractException {
        if (audioData == null || audioData.length == 0) {
            throw new EmptyFileException("File is empty");
        }

        Resource resource = new Resource();
        resource.setData(audioData);
        resource = resourceRepository.save(resource);

        try {
            extractMetadata(new ByteArrayInputStream(audioData), resource.getId());
        } catch (Exception e) {
            throw new MetadataExtractException("Failed to extract metadata", e);
        }

        return resource.getId();
    }


    private void extractMetadata(InputStream inputStream, Long resourceId) throws Exception {
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        new Mp3Parser().parse(inputStream, handler, metadata, new ParseContext());
        metadataService.save(resourceId, metadata);
    }

    public byte[] getFile(Long id) {
        if (id <= 0) {
            throw new CustomValidationException("Validation error",
                    Collections.singletonMap("ID", "ID must be a positive number."));
        }
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID=" + id + " not found")).getData();
    }

    public Map<String, List<Long>> deleteFiles(String ids) {
        if (ids.length() > 200) {
            throw new CustomValidationException("Validation error",
                    Collections.singletonMap("IDs", "The length of the ID list must not exceed 200 characters."));
        }

        List<Long> idList;
        try {
            idList = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new CustomValidationException("Validation error",
                    Collections.singletonMap("IDs", "IDs must be a comma-separated list of numbers."));
        }

        List<Long> nonExistentIds = new ArrayList<>();
        List<Long> deletedIds = new ArrayList<>();

        for (Long id : idList) {
            if (!resourceRepository.existsById(id)) {
                nonExistentIds.add(id);
            } else {
                resourceRepository.deleteById(id);
                metadataService.deleteMetadata(Collections.singletonList(id));
                deletedIds.add(id);
            }
        }

        if (!nonExistentIds.isEmpty()) {
            System.out.println("Non-existent IDs: " + nonExistentIds);
        }

        return Map.of("ids", deletedIds);
    }

}
