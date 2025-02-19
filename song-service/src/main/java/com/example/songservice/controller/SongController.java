package com.example.songservice.controller;

import com.example.songservice.entity.Song;
import com.example.songservice.entity.SongDto;
import com.example.songservice.service.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @PostMapping
    public ResponseEntity<Song> createSong(@Valid @RequestBody SongDto song) {
        Song createdSong = songService.saveSong(song);
        return ResponseEntity.ok(createdSong);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        return songService.findSongById(id).map(song -> ResponseEntity.ok(song)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteSongs(@RequestParam("id") String ids) {
        List<Long> longList = songService.deleteSong(ids);
        Map<String, List<Long>> response = Map.of("ids", longList);
        return ResponseEntity.ok().body(response);
    }
}
