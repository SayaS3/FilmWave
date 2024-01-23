package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.comment.CommentService;
import javassist.NotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add-comment")
    public String addComment(@RequestParam Long movieId,
                             @RequestParam String content,
                             Authentication authentication) throws NotFoundException {
        String userName = authentication.getName();
        commentService.addComment(userName, movieId, content);
        return "redirect:/movie/" + movieId;
    }
}