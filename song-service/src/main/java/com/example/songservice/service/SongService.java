package com.example.songservice.service;

import com.example.songservice.entity.MetadataRequest;
import com.example.songservice.entity.Song;
import com.example.songservice.exceptions.ResourceNotFoundException;
import com.example.songservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    public Optional<Song> findSongById(Long id) {
        return Optional.ofNullable(songRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource with ID=" + id + " not found")));
    }

    public void deleteSong(Long id) {

        List<Long> nonExistentIds = new ArrayList<>();

        if (!songRepository.existsById(id)) {
            nonExistentIds.add(id);
        } else {
            songRepository.deleteById(id);
        }
        if (!nonExistentIds.isEmpty()) {
            throw new ResourceNotFoundException("Resources not found for IDs: " + nonExistentIds);
        }

    }


    public void saveMetadata(MetadataRequest metadataRequest) {
        Long resourceId = metadataRequest.getResourceId();
        Optional<Song> optionalSong = songRepository.findById(resourceId);

        Song song = optionalSong.orElseGet(Song::new);
        song.setId(resourceId);
        song.setTitle(metadataRequest.getMetadata().getOrDefault("title", "Unknown Title"));
        song.setArtist(metadataRequest.getMetadata().getOrDefault("artist", "Unknown Artist"));
        song.setAlbum(metadataRequest.getMetadata().getOrDefault("album", "Unknown Album"));
        song.setDuration(metadataRequest.getMetadata().getOrDefault("xmpDM:duration", "00:00"));

        songRepository.save(song);
    }
}
