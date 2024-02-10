package com.sayas.filmhub.domain.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long movieId;
    private Long userId;
    private String username;
    private String content;

}
