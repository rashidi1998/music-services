package com.example.songservice.service;

import com.example.songservice.entity.Song;
import com.example.songservice.entity.SongDto;
import com.example.songservice.exceptions.CustomConflictException;
import com.example.songservice.exceptions.CustomValidationException;
import com.example.songservice.exceptions.ResourceNotFoundException;
import com.example.songservice.repository.SongRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
public class SongService {
    @Autowired
    private SongRepository songRepository;

    @Transactional
    public Song saveSong(@Valid SongDto song) {
        if (songRepository.existsById(song.getId())) {
            throw new CustomConflictException("Song with ID=" + song.getId() + " already exists.");
        }
        Song songEntity = convertToEntity(song);
        return songRepository.save(songEntity);
    }

    private Song convertToEntity(SongDto songDto) {
        Song song = new Song();
        song.setId(songDto.getId());
        song.setName(songDto.getName());
        song.setArtist(songDto.getArtist());
        song.setAlbum(songDto.getAlbum());
        song.setDuration(songDto.getDuration());
        song.setYear(songDto.getYear());
        return song;
    }

    public Optional<Song> findSongById(Long id) {
        return Optional.ofNullable(songRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Song with ID=" + id + " not found")));
    }

    public List<Long> deleteSongs(String ids) {
        if (ids.length() > 200) {
            throw new CustomValidationException("Validation error",
                    Map.of("ID", "The length of the ID list must not exceed 200 characters."));
        }

        List<Long> parsedIds = new ArrayList<>();
        List<Long> deletedIds = new ArrayList<>();
        List<String> invalidIds = new ArrayList<>();

        String[] splitIds = ids.split(",");
        for (String id : splitIds) {
            id = id.trim();
            if (!id.isEmpty()) {
                try {
                    Long parsedId = Long.parseLong(id);
                    parsedIds.add(parsedId);
                } catch (NumberFormatException e) {
                    invalidIds.add(id);
                }
            }
        }

        if (!invalidIds.isEmpty()) {
            Map<String, String> errorDetails = invalidIds.stream()
                    .collect(Collectors.toMap(
                            id -> "ID",
                            id -> "ID: " + id + " is not valid."
                    ));
            throw new CustomValidationException("Invalid IDs provided: " + String.join(", ", invalidIds), errorDetails);
        }

        for (Long id : parsedIds) {
            if (songRepository.existsById(id)) {
                songRepository.deleteById(id);
                deletedIds.add(id);
            }
        }

        return deletedIds;
    }


}
