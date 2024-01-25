package com.sayas.filmhub.domain.comment;

import com.sayas.filmhub.domain.movie.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface CommentRepository extends CrudRepository<Comment, Long> {
    void deleteByMovie(Movie movie);
    List<Comment> findByMovieId(Long movieId);
    Optional<Comment> findById(Long id);

}
