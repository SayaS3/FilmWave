package com.sayas.filmhub.domain.errorreport;

import com.sayas.filmhub.domain.genre.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ErrorReportRepository extends CrudRepository<ErrorReport, Long> {
    List<ErrorReport> findAll();

}
