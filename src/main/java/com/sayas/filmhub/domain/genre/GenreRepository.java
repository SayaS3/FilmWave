package com.sayas.filmhub.domain.genre;

import com.sayas.filmhub.domain.genre.Genre;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface GenreRepository extends CrudRepository<Genre, Long> {
    Optional<Genre> findByNameIgnoreCase(String name);
}