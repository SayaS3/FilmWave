package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.comment.CommentService;
import com.sayas.filmhub.domain.comment.dto.CommentDto;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import com.sayas.filmhub.domain.movie.dto.MovieSaveDto;
import com.sayas.filmhub.domain.rating.RatingService;
import com.sayas.filmhub.domain.user.UserService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


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

    @GetMapping("/search")
    public String searchMovies(@RequestParam("query") String query, Model model) {
        List<MovieDto> searchResults = movieService.searchMovies(query);
        model.addAttribute("heading", "Search Results");
        model.addAttribute("description", "Search results for: " + query);
        model.addAttribute("movies", searchResults);

        return "search-movies";
    }

    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable String id,
                           Model model,
                           Authentication authentication) {

        try {
            long movieId = Long.parseLong(String.valueOf(id));

            MovieDto movie = movieService.findMovieById(movieId)
                    .filter(MovieDto::isApproved)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));


            List<CommentDto> allComments = commentService.getCommentsByMovie(movieId);

            String currentUser = (authentication != null) ? authentication.getName() : null;

            List<CommentDto> filteredComments = allComments.stream()
                    .filter(comment -> {
                        try {
                            boolean isCurrentUserComment = currentUser != null && currentUser.equals(comment.getUsername());
                            return !userService.isShadowBanned(comment.getUserId()) || isCurrentUserComment;
                        } catch (NotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toList());

            model.addAttribute("movie", movie);
            model.addAttribute("comments", filteredComments);

            if (authentication != null && authentication.isAuthenticated()) {
                Integer rating = ratingService.getUserRatingForMovie(currentUser, movieId).orElse(0);
                model.addAttribute("userRating", rating);
            }
            return "movie";
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid movie ID");
            return "error/404";
        }
    }

    @GetMapping("/top10")
    public String findTop10(Model model) {
        List<MovieDto> top10Movies = movieService.findTop10Movies();
        model.addAttribute("heading", "Top 10 Movies");
        model.addAttribute("description", "Movies highly rated by users");
        model.addAttribute("movies", top10Movies);

        return "top10-movies";
    }
}