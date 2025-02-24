package com.example.resourceservice.controller;

import com.example.resourceservice.entity.response.ResourceResponse;
import com.example.resourceservice.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceResponse> uploadResource(@RequestBody(required = false) byte[] audioData) {
        Long resourceId = resourceService.storeFile(audioData);
        return ResponseEntity.ok(new ResourceResponse(resourceId));
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        byte[] data = resourceService.getFile(id);
        return ResponseEntity.ok().contentType(MediaType.valueOf("audio/mpeg")).body(data);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteResources(@RequestParam("id") String ids) {
        return ResponseEntity.ok().body(resourceService.deleteFiles(ids));
    }
}
