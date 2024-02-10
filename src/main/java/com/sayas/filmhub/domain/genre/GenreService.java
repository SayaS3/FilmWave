package com.sayas.filmhub.domain.genre;

import com.sayas.filmhub.domain.genre.dto.GenreDto;
import com.sayas.filmhub.domain.movie.Movie;
import com.sayas.filmhub.domain.movie.MovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


@Service
public class GenreService {
    private final GenreRepository genreRepository;
    private final MovieRepository movieRepository;


    public GenreService(GenreRepository genreRepository, MovieRepository movieRepository) {
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
    }

    public List<GenreDto> findAllGenres() {
        return StreamSupport.stream(genreRepository.findAll().spliterator(), false)
                .map(GenreDtoMapper::map)
                .toList();
    }

    @Transactional
    public void addGenre(GenreDto genre) {
        Genre genreToSave = new Genre();
        genreToSave.setName(genre.getName());
        genreToSave.setDescription(genre.getDescription());
        genreRepository.save(genreToSave);
    }

    @Transactional
    public void editGenre(GenreDto genreToEdit) {
        genreRepository.findById(genreToEdit.getId()).ifPresent(genre -> {
            genre.setName(genreToEdit.getName());
            genre.setDescription(genreToEdit.getDescription());
            genreRepository.save(genre);
        });
    }

    public Optional<GenreDto> findGenreById(Long id) {
        return genreRepository.findById(id)
                .map(GenreDtoMapper::map);
    }

    @Transactional
    public void deleteGenreById(Long id) {
        genreRepository.findById(id).ifPresent(genre -> {
            List<Movie> moviesWithThatGenre = movieRepository.findAllByGenre(genre);
            for (Movie movie : moviesWithThatGenre) {
                movie.setGenre(null);
            }
            movieRepository.saveAll(moviesWithThatGenre);
            genreRepository.delete(genre);
        });

    }
}