package com.sayas.filmhub.domain.comment;

import com.sayas.filmhub.domain.comment.dto.CommentDto;

public class CommentDtoMapper {
    static CommentDto map(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getMovie().getId(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getContent()
        );
    }
}
