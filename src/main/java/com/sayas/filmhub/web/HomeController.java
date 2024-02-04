package com.sayas.filmhub.web;


import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final MovieService movieService;

    public HomeController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size) {

        page = Math.max(page, 0);

        Pageable pageable = PageRequest.of(page, size);

        try {
            Page<MovieDto> promotedMoviesPage = movieService.findAllPromotedMovies(pageable);

            model.addAttribute("heading", "Featured Movies");
            model.addAttribute("description", "Movies recommended by our team");
            model.addAttribute("movies", promotedMoviesPage.getContent());

            model.addAttribute("currentPage", promotedMoviesPage.getNumber());
            model.addAttribute("totalPages", promotedMoviesPage.getTotalPages());

        } catch (IllegalArgumentException e) {
            return "error";
        }

        return "movie-listing";
    }
}