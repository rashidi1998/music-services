package com.example.songservice.controller;

import com.example.songservice.entity.Song;
import com.example.songservice.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {

    @Autowired
    private SongService songService;

    @PostMapping
    public ResponseEntity<Song> createSong(@RequestBody Song song) {
        Song createdSong = songService.saveSong(song);
        return ResponseEntity.ok(createdSong);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        return songService.findSongById(id).map(song -> ResponseEntity.ok(song)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSongs(@RequestParam("ids") String ids) {
        if (ids.length() > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IDs parameter is too long. Max length is 200 characters.");
        } List<Long> songIds = List.of(ids.split(",")).stream().map(Long::parseLong).toList();

        songIds.forEach(id -> songService.deleteSong(id));
        return ResponseEntity.ok().build();
    }

}
