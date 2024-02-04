package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.rating.Rating;
import com.sayas.filmhub.domain.rating.RatingService;
import com.sayas.filmhub.domain.user.UserService;
import com.sayas.filmhub.domain.user.dto.UserCredentialsDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class UserProfileController {
    private final RatingService ratingService;

    private final UserService userService;

    public UserProfileController(RatingService ratingService, UserService userService) {

        this.ratingService = ratingService;

        this.userService = userService;
    }

    @GetMapping("/user/{username}")
    public String getUserProfile(@PathVariable String username, Model model) {
        Optional<UserCredentialsDto> userFind = userService.getuserByName(username);

        if (userFind.isPresent()) {
            UserCredentialsDto user = userFind.get();
            List<Rating> userRatings = ratingService.findByUsername(user);

            model.addAttribute("user", user);
            model.addAttribute("userRatings", userRatings);

            return "user-profile";
        } else {
            return "error/404";
        }
    }
    @GetMapping("/user-profile")
    public String getUserProfile(Model model) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        userService.getuserByName(currentUsername)
                .ifPresentOrElse(
                        user -> {
                            List<Rating> userRatings = ratingService.findByUsername(user);

                            model.addAttribute("user", user);
                            model.addAttribute("userRatings", userRatings);
                        },
                        () -> model.addAttribute("error", "User not found")
                );

        return "user-profile";
    }

}
