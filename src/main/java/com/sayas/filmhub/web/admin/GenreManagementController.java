package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.genre.GenreService;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        return "redirect:/admin";
    }
}