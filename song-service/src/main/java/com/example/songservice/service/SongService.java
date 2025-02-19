package com.example.songservice.service;

import com.example.songservice.entity.Song;
import com.example.songservice.entity.SongDto;
import com.example.songservice.exceptions.ResourceNotFoundException;
import com.example.songservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        newSong.setId(song.id());
        return songRepository.save(newSong);
    }

    public Optional<Song> findSongById(Long id) {
        return Optional.ofNullable(songRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Resource with ID=" + id + " not found")));
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
}
