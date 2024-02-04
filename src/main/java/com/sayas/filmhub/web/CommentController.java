package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.comment.CommentService;
import javassist.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        commentService.addComment(authentication.getName(), movieId, content);
        return "redirect:/movie/" + movieId;
    }
    @PutMapping("/shadow-ban/{id}")
    public String shadowBan(@PathVariable String id,
                            @RequestParam Long movieId) throws NotFoundException {
        commentService.shadowBan(Long.parseLong(id));
        return "redirect:/movie/" + movieId;
    }
    @DeleteMapping("/delete-comment/{id}")
    public String deleteComment(@PathVariable String id, @RequestParam Long movieId) throws NotFoundException {
        commentService.deleteComment(Long.parseLong(id));
        return "redirect:/movie/" + movieId;
    }
}
