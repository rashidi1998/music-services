package com.example.resourceservice.controller;

import com.example.resourceservice.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<Object> uploadResource(@RequestBody byte[] audioData) {
        try {
            if (audioData == null || audioData.length == 0) {
                return ResponseEntity.badRequest().body("{\"error\": \"File is empty\"}");
            }

            Long resourceId = resourceService.storeFile(audioData);

            return ResponseEntity.ok().body("{\"id\": " + resourceId + "}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("{\"error\": \"File upload failed\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
            byte[] data = resourceService.getFile(id);
            return ResponseEntity.ok(data);
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteResources(@RequestParam("id") String ids) {


        resourceService.deleteFiles(ids);
        return ResponseEntity.ok().body("{\"ids\": \"" + ids.toString() + "\"}");
    }
}
