package com.example.songservice.entity;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record SongDto(
        Long id,
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Artist is required") String artist,
        @NotBlank(message = "Album is required") String album,
        @NotBlank(message = "Duration is required") String duration,
        @NotBlank(message = "Year is required")
        @Pattern(regexp = "^(19|20)\\d{2}$", message = "Year must be in the format YYYY and between 1900 and 2099")
        String year
) {

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

