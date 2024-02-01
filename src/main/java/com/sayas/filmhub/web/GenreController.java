package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.genre.GenreService;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Controller
public class GenreController {
    private final GenreService genreService;
    private final MovieService movieService;

    public GenreController(GenreService genreService, MovieService movieService) {
        this.genreService = genreService;
        this.movieService = movieService;
    }

    @GetMapping("/genre/{name}")
    public String getGenre(@PathVariable String name, Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size) {
        page = Math.max(page, 0);

        // Create Pageable with pagination information
        Pageable pageable = PageRequest.of(page, size);

        // Retrieve a Page of MovieDto using movieService with pagination
        Page<MovieDto> moviesPage = movieService.findMoviesByGenreNameWithPagination(name, pageable);

        // Add attributes to the model for Thymeleaf template
        model.addAttribute("heading", name);
        model.addAttribute("movies", moviesPage.getContent());
        model.addAttribute("currentPage", moviesPage.getNumber());
        model.addAttribute("totalPages", moviesPage.getTotalPages());
        model.addAttribute("totalItems", moviesPage.getTotalElements());

        return "movie-listing";
    }

    @GetMapping("/genres")
    public String getGenreList(Model model) {
        List<GenreDto> genres = genreService.findAllGenres();
        model.addAttribute("genres", genres);
        return "genre-listing";
    }
}