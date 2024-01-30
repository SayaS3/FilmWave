package com.sayas.filmhub.domain.errorreport;

import com.sayas.filmhub.domain.movie.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ErrorReportRepository extends CrudRepository<ErrorReport, Long> {
    List<ErrorReport> findAll();

    void deleteByMovie(Movie movie);
}
