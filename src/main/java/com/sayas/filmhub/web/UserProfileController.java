package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.rating.Rating;
import com.sayas.filmhub.domain.rating.RatingService;
import com.sayas.filmhub.domain.user.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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
        return userService.getUserByName(username)
                .map(user -> {
                    List<Rating> userRatings = ratingService.findByUsername(user);

                    model.addAttribute("user", user);
                    model.addAttribute("userRatings", userRatings);
                    return "user-profile";
                })
                .orElse("error/404");
    }
    @GetMapping("/user-profile")
    public String getUserProfile(Model model) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        return userService.getUserByName(currentUsername)
                .map(user -> {
                    List<Rating> userRatings = ratingService.findByUsername(user);
                    model.addAttribute("user", user);
                    model.addAttribute("userRatings", userRatings);
                    return "user-profile";
                })
                .orElse("error/404");
    }


}
