package com.example.songservice.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SongDto {
    private Long id;

    @NotEmpty(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    private String name;

    @NotEmpty(message = "Artist is required")
    @Size(max = 100, message = "Artist must be less than 100 characters")
    private String artist;

    @NotEmpty(message = "Album is required")
    @Size(max = 100, message = "Album must be less than 100 characters")
    private String album;

    @NotEmpty(message = "Duration is required")
    @Pattern(regexp = "^(0[0-9]|[1-5][0-9]):[0-5][0-9]$", message = "Duration must be in the format MM:SS")
    private String duration;

    @NotEmpty(message = "Year is required")
    @Pattern(regexp = "^(19|20)\\d{2}$", message = "Year must be in the format YYYY and between 1900 and 2099")
    private String year;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public SongDto(Long id, String name, String artist, String album, String duration, String year) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.year = year;
    }

    public SongDto() {
    }
}
