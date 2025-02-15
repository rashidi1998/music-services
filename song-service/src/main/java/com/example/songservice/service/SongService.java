package com.example.songservice.service;

import com.example.songservice.entity.Song;
import com.example.songservice.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    public Optional<Song> findSongById(Long id) {
        return songRepository.findById(id);
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }
}
