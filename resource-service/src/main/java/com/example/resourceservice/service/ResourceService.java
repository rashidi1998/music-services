package com.example.resourceservice.service;

import com.example.resourceservice.entity.Resource;
import com.example.resourceservice.repository.ResourceRepository;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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

    public byte[] getFile(Long id) throws Exception {
        return resourceRepository.findById(id).orElseThrow().getData();
    }

    public void deleteFiles(Iterable<Long> ids) {
        resourceRepository.deleteAllById(ids);
    }
}
