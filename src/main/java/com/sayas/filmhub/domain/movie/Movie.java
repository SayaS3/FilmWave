package com.sayas.filmhub.domain.movie;


import com.sayas.filmhub.domain.genre.Genre;
import com.sayas.filmhub.domain.rating.Rating;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String shortDescription;
    private String description;
    private String youtubeTrailerId;
    private Integer releaseYear;

    @ManyToOne
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    private Genre genre;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    private Set<Rating> ratings = new HashSet<>();
    private boolean promoted;

    private String poster;

    @Column(name = "approved")
    private boolean approved;


}