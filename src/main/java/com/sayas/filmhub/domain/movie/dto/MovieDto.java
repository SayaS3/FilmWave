package com.sayas.filmhub.domain.movie.dto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieDto {
    private Long id;
    private String title;
    private String shortDescription;
    private String description;
    private String youtubeTrailerId;
    private Integer releaseYear;
    private String genre;
    @NotNull
    private Boolean promoted;
    private String poster;
    private double avgRating;
    private int ratingCount;
    private boolean approved;

}