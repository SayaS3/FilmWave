package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.genre.GenreService;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieSaveDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class MovieManagementController {
    private final MovieService movieService;
    private final GenreService genreService;

    public MovieManagementController(MovieService movieService, GenreService genreService) {
        this.movieService = movieService;
        this.genreService = genreService;
    }

    @GetMapping("/movie")
    public String addMovieForm(Model model) {
        List<GenreDto> allGenres = genreService.findAllGenres();
        MovieSaveDto movie = new MovieSaveDto();
        model.addAttribute("movie", movie);
        model.addAttribute("genres", allGenres);
        return "admin/movie-form";
    }

    @GetMapping("/movie/{id}")
    public String editMovieForm(@PathVariable Long id, Model model) {
        return movieService.findMovieById(id)
                .map(movie -> {
                    List<GenreDto> allGenres = genreService.findAllGenres();
                    model.addAttribute("movie", movie);
                    model.addAttribute("genres", allGenres);
                    return "admin/edit-movie-form";
                })
                .orElse("redirect:/admin");
    }

    @PostMapping("/movie")
    public String addMovie(MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        movieService.addMovie(movie);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Movie %s has been saved.".formatted(movie.getTitle()));
        return "redirect:/admin";
    }

    @PutMapping("/movie/{id}")
    public String editMovie(@PathVariable Long id, MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        movieService.editMovie(id, movie);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Movie %s has been updated.".formatted(movie.getTitle()));
        return "redirect:/movie/{id}";
    }

    @DeleteMapping("/movie/{id}")
    public String deleteMovie(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        movieService.deleteMovie(id);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Movie has been deleted.");
        return "redirect:/";
    }

    @PutMapping("/approve-movie/{id}")
    public String approveMovie(@PathVariable Long id, MovieSaveDto movie, RedirectAttributes redirectAttributes) {
        movieService.approveMovie(id, movie);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Movie %s has been approved.".formatted(movie.getTitle()));
        return "redirect:/movie/{id}";
    }

}