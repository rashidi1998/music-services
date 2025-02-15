package com.example.resourceservice.controller;

import com.example.resourceservice.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<Object> uploadResource(@RequestParam("file") MultipartFile file) {
        try {
            Long resourceId = resourceService.storeFile(file);
            return ResponseEntity.ok().body("{\"id\": " + resourceId + "}");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        try {
            byte[] data = resourceService.getFile(id);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteResources(@RequestParam List<Long> id) {
        resourceService.deleteFiles(id);
        return ResponseEntity.ok().body("{\"ids\": " + id.toString() + "}");
    }
}
