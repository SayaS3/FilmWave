package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.user.UserService;
import com.sayas.filmhub.domain.user.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registrationForm(Model model) {
        UserRegistrationDto userRegistration = new UserRegistrationDto();
        model.addAttribute("user", userRegistration);
        return "registration-form";
    }

    @PostMapping("/registration")
    public String register(UserRegistrationDto userRegistration, Model model) {
        model.addAttribute("user", userRegistration);

        if (userService.existsByEmail(userRegistration.getEmail())) {
            model.addAttribute("emailExistsError", "Użytkownik z tym adresem e-mail już istnieje");
            return "registration-form";
        }

        if (userService.existsByUsername(userRegistration.getUsername())) {
            model.addAttribute("usernameExistsError", "Użytkownik z tym nickiem już istnieje");
            return "registration-form";
        }

        userService.registerUserWithDefaultRole(userRegistration);
        return "redirect:/login";
    }
}