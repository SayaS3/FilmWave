package com.sayas.filmhub.domain.movie;

import com.opencsv.bean.CsvToBean;
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
    Page<Movie> findByGenreName(String genreName, Pageable pageable);

    List<Movie> findAllByGenre(Genre genre);
    List<Movie> findByApprovedFalse();
    List<Movie> findMoviesByIdAndApprovedIsFalse(Long id);

    @Query("SELECT m FROM Movie m JOIN m.ratings r GROUP BY m ORDER BY AVG(r.rating) DESC LIMIT 10")
    List<Movie> findTopByRatingAndApproved(boolean approved);

    List<Movie> findByTitleContainingIgnoreCaseAndApproved(String query, boolean b);
}


