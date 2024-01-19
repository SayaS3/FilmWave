package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import com.sayas.filmhub.domain.rating.RatingService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Controller
public class MovieController {
    private final MovieService movieService;
    private final RatingService ratingService;

    public MovieController(MovieService movieService, RatingService ratingService) {
        this.movieService = movieService;
        this.ratingService = ratingService;
    }

    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable long id,
                           Model model,
                           Authentication authentication) {
        MovieDto movie = movieService.findMovieById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("movie", movie);
        //Jeżeli użytkownik jest zalogowany
        if (authentication != null) {
            String currentUserEmail = authentication.name();
            //to wyszukujemy jego głos
            Integer rating = ratingService.getUserRatingForMovie(currentUserEmail, id).orElse(0);
            //i zapisujemy go w modelu
            model.addAttribute("userRating", rating);
        }
        return "movie";
    }
    @GetMapping("/top10")
    public String findTop10(Model model) {
        List<MovieDto> top10Movies = movieService.findTopMovies(10);
        model.addAttribute("heading", "Top 10 Movies");
        model.addAttribute("description", "Movies highly rated by users");
        model.addAttribute("movies", top10Movies);
        return "movie-listing";
    }
}