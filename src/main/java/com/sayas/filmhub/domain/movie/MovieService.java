package com.sayas.filmhub.domain.movie;

import com.sayas.filmhub.domain.genre.Genre;
import com.sayas.filmhub.domain.genre.GenreRepository;
import com.sayas.filmhub.domain.genre.dto.GenreDto;
import com.sayas.filmhub.domain.movie.dto.MovieDto;
import com.sayas.filmhub.domain.movie.dto.MovieSaveDto;
import com.sayas.filmhub.storage.FileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final FileStorageService fileStorageService;

    public MovieService(MovieRepository movieRepository,
                        GenreRepository genreRepository,
                        FileStorageService fileStorageService) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<MovieDto> findAllPromotedMovies() {
        return movieRepository.findAllByPromotedIsTrue().stream()
                .map(MovieDtoMapper::map)
                .toList();
    }

    public Optional<MovieDto> findMovieById(long id) {
        return movieRepository.findById(id).map(MovieDtoMapper::map);
    }

    public List<MovieDto> findMoviesByGenreName(String genre) {
        return movieRepository.findAllByGenre_NameIgnoreCase(genre).stream()
                .map(MovieDtoMapper::map)
                .toList();
    }

    @Transactional
    public void addMovie(MovieSaveDto movieToSave) {
        Movie movie = new Movie();
        movie.setTitle(movieToSave.getTitle());
        movie.setOriginalTitle(movieToSave.getOriginalTitle());
        movie.setPromoted(movieToSave.isPromoted());
        movie.setReleaseYear(movieToSave.getReleaseYear());
        movie.setShortDescription(movieToSave.getShortDescription());
        movie.setDescription(movieToSave.getDescription());
        movie.setYoutubeTrailerId(movieToSave.getYoutubeTrailerId());
        Genre genre = genreRepository.findByNameIgnoreCase(movieToSave.getGenre()).orElseThrow();
        movie.setGenre(genre);
        if (movieToSave.getPoster() != null && !movieToSave.getPoster().isEmpty()) {
            String savedFileName = fileStorageService.saveImage(movieToSave.getPoster());
            movie.setPoster(savedFileName);
        }
        movieRepository.save(movie);
    }

    public List<MovieDto> findTopMovies(int size) {
        Pageable page = PageRequest.of(0, size);
        return movieRepository.findTopByRating(page).stream()
                .map(MovieDtoMapper::map)
                .toList();
    }

    @Transactional
    public void editMovie(Long id, MovieSaveDto movieChanged) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isPresent()) {
            Movie movie = optionalMovie.get();
            Genre genre = genreRepository.findByNameIgnoreCase(movieChanged.getGenre())
                    .orElseThrow(() -> new RuntimeException("Genre not found: " + movieChanged.getGenre()));
            movie.setTitle(movieChanged.getTitle());
            movie.setOriginalTitle(movieChanged.getOriginalTitle());
            movie.setShortDescription(movieChanged.getShortDescription());
            movie.setDescription(movieChanged.getDescription());
            movie.setYoutubeTrailerId(movieChanged.getYoutubeTrailerId());
            if (movieChanged.getPoster() != null && !movieChanged.getPoster().isEmpty()) {
                String savedFileName = fileStorageService.saveImage(movieChanged.getPoster());
                movie.setPoster(savedFileName);
            }
            movie.setGenre(genre);
            movie.setReleaseYear(movieChanged.getReleaseYear());
            movieRepository.save(movie);
        } else {
            throw new RuntimeException("Movie not found with id: " + id);
        }
    }

    @Transactional
    public void deleteMovie(Long id) {
        Optional<Movie> movieToDelete = movieRepository.findById(id);
        if (movieToDelete.isPresent()) {
            Movie movie = movieToDelete.get();
            movieRepository.delete(movie);
        }
    }
}