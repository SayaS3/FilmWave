package com.sayas.filmhub.domain.movie;

import com.sayas.filmhub.domain.genre.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findAllByPromotedIsTrue(Pageable pageable);
    List<Movie> findAllByGenre_NameIgnoreCase(String genre);
    @Query("select m from Movie m join m.ratings r group by m order by avg(r.rating) desc")
    Page<Movie> findTopByRating(Pageable pageable);
    List<Movie> findAllByGenre(Genre genre);

    Page<Movie> findByTitleContainingIgnoreCase(String query, Pageable pageable);
}

