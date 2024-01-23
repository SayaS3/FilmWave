package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.comment.CommentService;
import com.sayas.filmhub.domain.comment.dto.CommentDto;
import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import com.sayas.filmhub.domain.rating.RatingService;
import com.sayas.filmhub.domain.user.UserService;
import com.sayas.filmhub.domain.user.dto.UserCredentialsDto;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class MovieController {
    private final MovieService movieService;
    private final RatingService ratingService;
    private final CommentService commentService;
    private final UserService userService;

    public MovieController(MovieService movieService, RatingService ratingService, CommentService commentService, UserService userService) {
        this.movieService = movieService;
        this.ratingService = ratingService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable long id,
                           Model model,
                           Authentication authentication) {
        List<CommentDto> comments = commentService.getCommentsByMovie(id);
        MovieDto movie = movieService.findMovieById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("movie", movie);
        List<CommentDto> commentsWithUsernames = new ArrayList<>();
        for (CommentDto comment : comments) {
            Optional<UserCredentialsDto> userCredentialsOptional = userService.getuserNameById(comment.getUserId());
            if (userCredentialsOptional.isPresent()) {
                UserCredentialsDto userCredentials = userCredentialsOptional.get();
                String username = userCredentials.getUsername();
                CommentDto commentWithUsername = new CommentDto(
                        comment.getId(),
                        comment.getMovieId(),
                        comment.getUserId(),
                        username,
                        comment.getContent()
                );
                commentsWithUsernames.add(commentWithUsername);
            }
        }
        model.addAttribute("comments", commentsWithUsernames);

        // Jeżeli użytkownik jest zalogowany
        if (authentication != null) {
            String currentUserEmail = authentication.name();
            Integer rating = ratingService.getUserRatingForMovie(currentUserEmail, id).orElse(0);
            model.addAttribute("userRating", rating);
        }
        return "movie";
    }
    @GetMapping("/top10")
    public String findTop10(Model model) {
        List<MovieDto> top10Movies = movieService.findTopMovies(10);
        model.addAttribute("heading", "Top 10 Movies");
        model.addAttribute("description", "Movies highly rated by users");
        model.addAttribute("movies", top10Movies);
        return "movie-listing";
    }
}