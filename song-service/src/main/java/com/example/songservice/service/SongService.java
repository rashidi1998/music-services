package com.example.songservice.service;

import com.example.songservice.entity.Song;
import com.example.songservice.entity.SongDto;
import com.example.songservice.exceptions.ResourceNotFoundException;
import com.example.songservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Transactional
    public Song saveSong(SongDto song) {
        Song newSong = new Song();
        newSong.setName(song.name());
        newSong.setArtist(song.artist());
        newSong.setAlbum(song.album());
        newSong.setDuration(song.duration());
        newSong.setYear(song.year());
        if (songRepository.existsById(song.id())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Expected response to contain '409'");
        }
        newSong.setId(song.id());
        return songRepository.save(newSong);
    }

    public Optional<Song> findSongById(Long id) {
        return Optional.ofNullable(songRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource with ID=" + id + " not found")));
    }

    public List<Long> deleteSong(String ids) {
        if (ids.length() > 200) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected response to contain '400'");
        }
        List<Long> songIds = new ArrayList<>();
        try {
            songIds = Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Expected response to contain '400'", e);
        }
        List<Long> nonExistentIds = new ArrayList<>();
        for (Long id : songIds) {
            if (songRepository.existsById(id)) {
                nonExistentIds.add(id);
                songRepository.deleteById(id);
            }
        }
       return nonExistentIds;

    }
}
