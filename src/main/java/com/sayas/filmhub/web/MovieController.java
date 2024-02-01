package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.comment.CommentService;
import com.sayas.filmhub.domain.comment.dto.CommentDto;
import com.sayas.filmhub.domain.movie.MovieService;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import com.sayas.filmhub.domain.rating.RatingService;
import com.sayas.filmhub.domain.user.UserService;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String searchMovies(@RequestParam("query") String query,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        page = Math.max(page, 0);

        Pageable pageable = PageRequest.of(page, size).previousOrFirst();

        Page<MovieDto> searchResultsPage = movieService.searchMovies(query, pageable);

        model.addAttribute("heading", "Search Results");
        model.addAttribute("description", "Search results for: " + query);
        model.addAttribute("movies", searchResultsPage.getContent());
        model.addAttribute("currentPage", searchResultsPage.getNumber());
        model.addAttribute("totalPages", searchResultsPage.getTotalPages());

        return "movie-listing";
    }
    @GetMapping("/movie/{id}")
    public String getMovie(@PathVariable long id,
                           Model model,
                           Authentication authentication) {

        List<CommentDto> allComments = commentService.getCommentsByMovie(id);

        // Filtruj komentarze, usuwając te od shadowbanned użytkowników, ale pozwalaj na zobaczenie własnych komentarzy
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

        MovieDto movie = movieService.findMovieById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("movie", movie);
        model.addAttribute("comments", filteredComments);

        // Jeżeli użytkownik jest zalogowany
        if (authentication != null) {
            Integer rating = ratingService.getUserRatingForMovie(currentUser, id).orElse(0);
            model.addAttribute("userRating", rating);
        }
        return "movie";
    }

    @GetMapping("/top10")
    public String findTop10(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            Model model) {
        if (page < 0) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<MovieDto> top10MoviesPage = movieService.findTopMovies(pageable);


        model.addAttribute("heading", "Top 10 Movies");
        model.addAttribute("description", "Movies highly rated by users");
        model.addAttribute("movies", top10MoviesPage.getContent());
        model.addAttribute("currentPage", top10MoviesPage.getNumber());
        model.addAttribute("totalPages", top10MoviesPage.getTotalPages());

        return "movie-listing";
    }
}