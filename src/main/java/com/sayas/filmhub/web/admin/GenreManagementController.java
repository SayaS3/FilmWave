package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.genre.GenreService;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class GenreManagementController {
    private final GenreService genreService;

    public GenreManagementController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/admin/add-genre")
    public String addGenreForm(Model model) {
        GenreDto genre = new GenreDto();
        model.addAttribute("genre", genre);
        return "admin/genre-form";
    }

    @PostMapping("/admin/add-genre")
    public String addGenre(GenreDto genre, RedirectAttributes redirectAttributes) {
        genreService.addGenre(genre);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Genre %s has been saved.\n".formatted(genre.getName()));
        return "redirect:/genres";
    }
    @PostMapping("/admin/edit-genre/{id}")
    public String editGenre(GenreDto genre, RedirectAttributes redirectAttributes) {
        genreService.editGenre(genre);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Genre %s has been saved.\n".formatted(genre.getName()));
        return "redirect:/genres";
    }
    @GetMapping("/admin/edit-genre/{id}")
    public String editGenre(@PathVariable Long id, Model model) {
        Optional<GenreDto> genreOptional = genreService.findGenreById(id);
        if (genreOptional.isPresent()) {
            GenreDto existingGenre = genreOptional.get();
            List<GenreDto> allGenres = genreService.findAllGenres();
            model.addAttribute("genres", allGenres);
            model.addAttribute("genre", existingGenre);
            return "admin/edit-genre-form";
        } else {
            return "redirect:/admin"; // Przykładowy przekierowanie na stronę admina
        }
    }
    @GetMapping("/admin/delete-genre/{id}")
    public String deleteGenre(GenreDto genre, RedirectAttributes redirectAttributes) {
        genreService.deleteGenre(genre);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Genre %s has been deleted.\n".formatted(genre.getName()));
        return "redirect:/genres";
    }
}