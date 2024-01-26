package com.sayas.filmhub.domain.comment.dto;



public class CommentDto {

    private Long id;
    private Long movieId;
    private Long userId;
    private String username;
    private String content;

    public CommentDto(Long id, Long movieId, Long userId, String username, String content) {
        this.id = id;
        this.movieId = movieId;
        this.userId = userId;
        this.username = username;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMovieId() {
        return movieId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
