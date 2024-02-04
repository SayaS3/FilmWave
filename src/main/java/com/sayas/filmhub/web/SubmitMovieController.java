package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.genre.GenreService;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieSaveDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SubmitMovieController {
    public static final String NOTIFICATION_ATTRIBUTE = "notification";
    @Autowired
    private GenreService genreService;
    @Autowired
    private MovieService movieService;
    @GetMapping("/submit-movie")
    public String submitMovieForm(Model model) {
        List<GenreDto> allGenres = genreService.findAllGenres();
        MovieSaveDto movie = new MovieSaveDto();
        model.addAttribute("movie", movie);
        model.addAttribute("genres", allGenres);
        return "submit-movie";
    }
    @PostMapping ("/submit-movie")
    public String submitMovie(MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        movieService.submitMovie(movie);
        redirectAttributes.addFlashAttribute(
                NOTIFICATION_ATTRIBUTE,
                "Movie %s has been submited.".formatted(movie.getTitle()));
        return "redirect:submit-movie";
    }
}
