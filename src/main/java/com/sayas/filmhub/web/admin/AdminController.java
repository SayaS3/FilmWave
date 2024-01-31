package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.errorreport.ErrorReport;
import com.sayas.filmhub.domain.errorreport.ErrorReportService;
import com.sayas.filmhub.domain.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
class AdminController {
    private final ErrorReportService errorReportService;

    public AdminController(ErrorReportService errorReportService, UserService userService) {
        this.errorReportService = errorReportService;
    }

    public static final String NOTIFICATION_ATTRIBUTE = "notification";
    @GetMapping("/admin")
    public String getAdminPanel() {
        return "admin/admin";
    }
    @GetMapping("/admin/view-reports")
    public String viewErrorReports(Model model) {
        List<ErrorReport> errorReports = errorReportService.getAllErrorReports();
        model.addAttribute("errorReports", errorReports);
        return "admin/view-reports";
    }
    @DeleteMapping("/admin/delete-report/{id}")
    public String reportError(@RequestParam Long id) {
        errorReportService.deleteReport(id);
        return "redirect:/admin/view-reports" ;
    }
}