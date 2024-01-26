package com.sayas.filmhub.domain.movie;

import com.sayas.filmhub.domain.genre.Genre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
    List<Movie> findAllByPromotedIsTrue();
    List<Movie> findAllByGenre_NameIgnoreCase(String genre);

    @Query("select m from Movie m join m.ratings r group by m order by avg(r.rating) desc")
    List<Movie> findTopByRating(Pageable page);
    List<Movie> findAllByGenre(Genre genre);

    List<Movie> findByTitleContainingIgnoreCase(String query);
}

