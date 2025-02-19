package com.example.songservice.service;

import com.example.songservice.entity.Song;
import com.example.songservice.entity.SongDto;
import com.example.songservice.exceptions.CustomValidationException;
import com.example.songservice.exceptions.ResourceNotFoundException;
import com.example.songservice.repository.SongRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Transactional
    public Song saveSong(@Valid SongDto song) {
        String yearPattern = "^(19|20)\\d{2}$";
        if (!song.getYear().matches(yearPattern)) {
            Map<String, String> errors = new HashMap<>();
            errors.put("year", song.getYear());
            errors.put("duration",song.getDuration());
            errors.put("error","Expected response to contain '400'");
            throw new CustomValidationException("Validation error",errors);
        }
        Song newSong = new Song();
        newSong.setName(song.getName());
        newSong.setArtist(song.getArtist());
        newSong.setAlbum(song.getAlbum());
        newSong.setDuration(song.getDuration());
        newSong.setYear(song.getYear());
        if (songRepository.existsById(song.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Expected response to contain '409'");
        }
        newSong.setId(song.getId());
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
