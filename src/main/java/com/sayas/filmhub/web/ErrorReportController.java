package com.sayas.filmhub.web;

import com.sayas.filmhub.domain.errorreport.ErrorReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ErrorReportController {

    private final ErrorReportService errorReportService;

    @Autowired
    public ErrorReportController(ErrorReportService errorReportService) {
        this.errorReportService = errorReportService;
    }

    @PostMapping("/report-error")
    public String reportError(@RequestParam Long movieId, @RequestParam String errorDescription,
                              Authentication authentication, RedirectAttributes redirectAttributes) {
        String userName = authentication.getName();
        errorReportService.reportError(movieId, userName, errorDescription);
        redirectAttributes.addFlashAttribute("reportSuccessMessage", "Error reported successfully!");
        return "redirect:/movie/" + movieId;
    }

}