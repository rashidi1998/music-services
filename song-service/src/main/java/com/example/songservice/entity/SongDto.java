package com.example.songservice.entity;


import jakarta.validation.constraints.NotBlank;


public record SongDto(Long id,
                      @NotBlank(message = "Name is required") String name,
                      @NotBlank(message = "Artist is required") String artist,
                      @NotBlank(message = "Album is required") String album,
                      @NotBlank(message = "Duration is required") String duration,
                      @NotBlank(message = "Year is required") String year) {

    @Override
    public String name() {
        return name;
    }

    @Override
    public String artist() {
        return artist;
    }

    @Override
    public String album() {
        return album;
    }

    @Override
    public String duration() {
        return duration;
    }

    @Override
    public String year() {
        return year;
    }
}

