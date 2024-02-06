package com.sayas.filmhub.domain.movie;

import com.sayas.filmhub.domain.movie.dto.MovieDto;
import com.sayas.filmhub.domain.rating.Rating;

class MovieDtoMapper {
    static MovieDto map(Movie movie) {
        double avgRating = movie.getRatings().stream()
                .map(Rating::getRating)
                .mapToDouble(val -> val)
                .average().orElse(0);
        int ratingCount = movie.getRatings().size();

        String genreName = "";
        if (movie.getGenre() != null) {
            genreName = movie.getGenre().getName();
        }

        return new MovieDto(
                movie.getId(),
                movie.getTitle(),
                movie.getShortDescription(),
                movie.getDescription(),
                movie.getYoutubeTrailerId(),
                movie.getReleaseYear(),
                genreName,
                movie.isPromoted(),
                movie.getPoster(),
                avgRating,
                ratingCount,
                movie.isApproved());
    }
}