package com.example.songservice.controller;

import com.example.songservice.entity.Song;
import com.example.songservice.entity.SongDto;
import com.example.songservice.exceptions.ResourceNotFoundException;
import com.example.songservice.service.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @PostMapping
    public ResponseEntity<SongDto> createSong(@Valid @RequestBody SongDto songDto) {
        return ResponseEntity.ok( songService.saveSong(songDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSongById(@PathVariable Long id) {
        SongDto songDto = songService.findSongById(id);
        return ResponseEntity.ok(songDto);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, List<Long>>> deleteSongs(@RequestParam("id") String ids) {
        return ResponseEntity.ok(songService.deleteSongs(ids));
    }
}
