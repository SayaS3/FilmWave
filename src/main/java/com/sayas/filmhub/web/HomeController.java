package com.sayas.filmhub.web;


import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private final MovieService movieService;

    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<MovieDto> promotedMovies = movieService.findAllPromotedMovies();
        model.addAttribute("heading", "Featured Movies");
        model.addAttribute("description", "Movies recommended by our team");
        model.addAttribute("movies", promotedMovies);
        return "movie-listing";
    }
}