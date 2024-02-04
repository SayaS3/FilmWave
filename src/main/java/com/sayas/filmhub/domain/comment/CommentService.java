package com.sayas.filmhub.domain.comment;

import com.sayas.filmhub.domain.comment.dto.CommentDto;
import com.sayas.filmhub.domain.movie.Movie;
import com.sayas.filmhub.domain.movie.MovieRepository;
import com.sayas.filmhub.domain.user.User;
import com.sayas.filmhub.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;


    public List<CommentDto> getCommentsByMovie(Long movieId) {
        List<Comment> comments = commentRepository.findByMovieId(movieId);
        return comments.stream()
                .map(CommentDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addComment(String userName, Long movieId, String content) throws NotFoundException {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + userName));

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found with ID: " + movieId));
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setMovie(movie);
        comment.setContent(content);
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long id) throws NotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment not found for user, movie, and content combination."));

        commentRepository.delete(comment);
    }

    @Transactional
    public void shadowBan(Long id) throws NotFoundException {
        Optional<Comment> commentToFind = commentRepository.findById(id);
        if (commentToFind.isPresent()) {
            Comment comment = commentToFind.get();
            User user = userRepository.findById(comment.getUser().getId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            user.setShadowBanned(true);
            userRepository.save(user);
        } else {
            throw new NotFoundException("User not found.");
        }
    }
}