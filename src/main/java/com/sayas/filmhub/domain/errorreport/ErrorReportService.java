package com.sayas.filmhub.domain.errorreport;

import com.sayas.filmhub.domain.movie.Movie;
import com.sayas.filmhub.domain.movie.MovieRepository;
import com.sayas.filmhub.domain.user.User;
import com.sayas.filmhub.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErrorReportService {

    private final ErrorReportRepository errorReportRepository;

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public ErrorReportService(ErrorReportRepository errorReportRepository, MovieRepository movieRepository, UserRepository userRepository) {
        this.errorReportRepository = errorReportRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }


    public void reportError(Long movieId, String username, String errorDescription){
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with name: " + username));
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));
        ErrorReport errorReport = new ErrorReport();
        errorReport.setUser(user);
        errorReport.setMovie(movie);
        errorReport.setErrorDescription(errorDescription);
        errorReportRepository.save(errorReport);
    }

    public List<ErrorReport> getAllErrorReports() {
            return errorReportRepository.findAll();
    }

    public void deleteReport(Long id) {
        errorReportRepository.findById(id).ifPresentOrElse(errorReport -> {
            errorReportRepository.deleteById(id);
        },() -> {
            throw new RuntimeException("Report not found with id: " + id);
        });
    }
}

