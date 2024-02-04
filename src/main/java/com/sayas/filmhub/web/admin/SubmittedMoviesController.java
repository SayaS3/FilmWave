package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller

public class SubmittedMoviesController {
    @Autowired
    private MovieService movieService;

    @GetMapping("/admin/submitted-movies")
    String getMoviesSubmittedByUsers(Model model){
       List<MovieDto> movies = movieService.findMoviesByNotApproved();
       model.addAttribute("movies",movies);
        return "/admin/submitted-movies-listing";
    }
    @GetMapping("/admin/submit-movie/{id}")
    public String getSubmittedMovie(@PathVariable long id,
                                    Model model) {
        MovieDto movie = movieService.findMovieById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("movie", movie);
        return "/admin/submitted-movie";
    }
}
