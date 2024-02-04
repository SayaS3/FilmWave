package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.genre.GenreService;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import com.sayas.filmhub.domain.movie.dto.MovieSaveDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class MovieManagementController {
    private final MovieService movieService;
    private final GenreService genreService;

    public MovieManagementController(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;
    }

    @GetMapping("/add-movie")
    public String addMovieForm(Model model) {
        List<GenreDto> allGenres = genreService.findAllGenres();
        model.addAttribute("genres", allGenres);
        MovieSaveDto movie = new MovieSaveDto();
        model.addAttribute("movie", movie);
        return "admin/movie-form";
    }
    @GetMapping("/edit-movie/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Optional<MovieDto> movieOptional = movieService.findMovieById(id);

        if (movieOptional.isPresent()) {
            MovieDto existingMovie = movieOptional.get();
            List<GenreDto> allGenres = genreService.findAllGenres();
            model.addAttribute("movie", existingMovie);
            model.addAttribute("genres", allGenres);

            return "admin/edit-movie-form";
        } else {
            return "redirect:/admin"; // Przykładowy przekierowanie na stronę admina
        }
    }
    @PostMapping("/add-movie")
    public String addMovie(MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        movieService.addMovie(movie);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Movie %s has been saved.".formatted(movie.getTitle()));
        return "redirect:/admin";
    }

    @PutMapping("/edit-movie/{id}")
    public String editMovie(@PathVariable Long id, MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        movieService.editMovie(id, movie);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Movie %s has been updated.".formatted(movie.getTitle()));
        return "redirect:/movie/{id}";
    }
    @DeleteMapping("/delete-movie/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        movieService.deleteMovie(id);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Movie has been deleted.");
        return "redirect:/";
    }
}