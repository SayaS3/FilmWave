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

    public Optional<GenreDto> findGenreByName(String name) {
        return genreRepository.findByNameIgnoreCase(name)
                .map(GenreDtoMapper::map);
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
        Optional<Genre> genreToSave = genreRepository.findById(genreToEdit.getId());
        if(genreToSave.isPresent()){
            Genre genre = genreToSave.get();
            genre.setName(genreToEdit.getName());
            genre.setDescription(genreToEdit.getDescription());
            genreRepository.save(genre);
        }

    }
    @Transactional
    public void deleteGenre(GenreDto genreToEdit) {
        Optional<Genre> genreToDelete = genreRepository.findById(genreToEdit.getId());
        if (genreToDelete.isPresent()) {
            Genre genre = genreToDelete.get();
            List<Movie> moviesWithThatGenre = movieRepository.findAllByGenre(genre);
            // Oznaczanie filmów jako bez gatunku
            for (Movie movie : moviesWithThatGenre) {
                movie.setGenre(null);
            }
            movieRepository.saveAll(moviesWithThatGenre);
            genreRepository.delete(genre);
        }
    }
    public Optional<GenreDto> findGenreById(Long id) {
        return genreRepository.findById(id)
                .map(GenreDtoMapper::map);
    }
}