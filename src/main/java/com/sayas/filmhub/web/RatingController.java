package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.rating.RatingService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
@Controller
public class RatingController {
    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/rate-movie")
    public String addMovieRating(@RequestParam long movieId,
                                 @RequestParam int rating,
                                 @RequestHeader String referer,
                                 Authentication authentication) throws NotFoundException {
        String currentUserName = authentication.getName();
        ratingService.addOrUpdateRating(currentUserName, movieId, rating);
        return "redirect:" + referer;
    }
}