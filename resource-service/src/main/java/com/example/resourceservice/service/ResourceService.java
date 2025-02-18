package com.example.resourceservice.service;

import com.example.resourceservice.entity.Resource;
import com.example.resourceservice.exceptions.ResourceNotFoundException;
import com.example.resourceservice.repository.ResourceRepository;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.xml.sax.ContentHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;
    @Autowired
    private MetadataService metadataService;

    public Long storeFile(byte[] audioData) throws Exception {
        Resource resource = new Resource();
        resource.setData(audioData);
        resource = resourceRepository.save(resource);

        extractMetadata(new ByteArrayInputStream(audioData), resource.getId());
        return resource.getId();
    }

    private void extractMetadata(InputStream inputStream, Long resourceId) throws Exception {
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        new Mp3Parser().parse(inputStream, handler, metadata, new ParseContext());
        metadataService.save(resourceId, metadata);
    }

    public byte[] getFile(Long id){
        if (id<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected response to contain '400'");
        }
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID=" + id + " not found")).getData();
    }

    public List<Long> deleteFiles(String ids) {
        if (ids.length() > 200) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Expected response to contain '400'");
        }

        List<Long> idList;
        try {
            idList = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected response to contain '400'", e);
        }
        List<Long> nonExistentIds = new ArrayList<>();


        for (Long id : idList) {
            if (resourceRepository.existsById(id)) {
                nonExistentIds.add(id);
                resourceRepository.deleteById(id);
                metadataService.deleteMetadata(Collections.singletonList(id));
            }
        }

        return nonExistentIds;
    }

}
