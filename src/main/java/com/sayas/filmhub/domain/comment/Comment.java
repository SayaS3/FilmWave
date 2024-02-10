package com.sayas.filmhub.domain.comment;

import com.sayas.filmhub.domain.movie.Movie;
import com.sayas.filmhub.domain.user.User;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="comments")
public class Comment {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
    private String content;

}

