package com.sayas.filmhub.domain.rating;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends CrudRepository<Rating, Long> {
    List<Rating> findByUser_Username(String username);
    Optional<Rating> findByUser_UsernameAndMovie_Id(String username, Long movieId);

}