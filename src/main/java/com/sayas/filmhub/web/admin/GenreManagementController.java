package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.genre.GenreService;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class GenreManagementController {
    private final GenreService genreService;

    public GenreManagementController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/add-genre")
    public String addGenreForm(Model model) {
        GenreDto genre = new GenreDto();
        model.addAttribute("genre", genre);
        return "admin/genre-form";
    }

    @PostMapping("/add-genre")
    public String addGenre(GenreDto genre, RedirectAttributes redirectAttributes) {
        genreService.addGenre(genre);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Genre %s has been saved.\n".formatted(genre.getName()));
        return "redirect:/genres";
    }
    @PutMapping("/edit-genre/{id}")
    public String editGenre(GenreDto genre, RedirectAttributes redirectAttributes) {
        genreService.editGenre(genre);
        redirectAttributes.addFlashAttribute(
                AdminController.NOTIFICATION_ATTRIBUTE,
                "Genre %s has been saved.\n".formatted(genre.getName()));
        return "redirect:/genres";
    }
    @GetMapping("/edit-genre/{id}")
    public String editGenre(@PathVariable Long id, Model model) {
        Optional<GenreDto> genreOptional = genreService.findGenreById(id);
        if (genreOptional.isPresent()) {
            GenreDto existingGenre = genreOptional.get();
            model.addAttribute("genre", existingGenre);
            return "admin/edit-genre-form";
        } else {
            return "redirect:/admin";
        }
    }
    @DeleteMapping("/delete-genre/{id}")
    public String deleteGenre(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<GenreDto> genre = genreService.getGenreById(id);
        if (genre.isPresent()) {
            genreService.deleteGenreById(id);
            redirectAttributes.addFlashAttribute(
                    AdminController.NOTIFICATION_ATTRIBUTE,
                    "Genre %s has been deleted.\n".formatted(genre.get()));
        } else {
            redirectAttributes.addFlashAttribute(
                    AdminController.NOTIFICATION_ATTRIBUTE,
                    "Genre with ID %d does not exist.\n".formatted(id));
        }
        return "redirect:/genres";
    }
}