package com.example.resourceservice.controller;

import com.example.resourceservice.service.ResourceService;
import org.checkerframework.checker.index.qual.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> uploadResource(@RequestBody byte[] audioData) {
        try {
            if (audioData == null || audioData.length == 0) {
                return ResponseEntity.badRequest().body("{\"error\": \"File is empty\"}");
            }

            Long resourceId = resourceService.storeFile(audioData);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"id\": " + resourceId + "}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"File upload failed\"}");
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        byte[] data = resourceService.getFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(data);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteResources(@RequestParam("id") String ids) {


       List<Long> idList = resourceService.deleteFiles(ids);
        Map<String, List<Long>> response = Map.of("ids", idList);
        return ResponseEntity.ok().body(response);
    }
}
