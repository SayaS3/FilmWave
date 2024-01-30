package com.sayas.filmhub.domain.rating;

import com.sayas.filmhub.domain.movie.Movie;
import com.sayas.filmhub.domain.movie.MovieRepository;
import com.sayas.filmhub.domain.user.User;
import com.sayas.filmhub.domain.user.UserRepository;
import com.sayas.filmhub.domain.user.dto.UserCredentialsDto;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public RatingService(RatingRepository ratingRepository, UserRepository userRepository, MovieRepository movieRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public void addOrUpdateRating(String username, long movieId, int rating) throws NotFoundException {
        Rating ratingToSaveOrUpdate = ratingRepository.findByUser_UsernameAndMovie_Id(username, movieId)
                .orElseGet(Rating::new);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + username));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found with ID: " + movieId));
        ratingToSaveOrUpdate.setUser(user);
        ratingToSaveOrUpdate.setMovie(movie);
        ratingToSaveOrUpdate.setRating(rating);
        ratingRepository.save(ratingToSaveOrUpdate);
    }

    public Optional<Integer> getUserRatingForMovie(String username, long movieId) {
        return ratingRepository.findByUser_UsernameAndMovie_Id(username, movieId)
                .map(Rating::getRating);
    }

    public List<Rating> findByUsername(UserCredentialsDto user) {
        return ratingRepository.findByUser_Username(user.getUsername());
    }
}