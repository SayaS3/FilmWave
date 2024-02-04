package com.sayas.filmhub.web.admin;

import com.sayas.filmhub.domain.errorreport.ErrorReport;
import com.sayas.filmhub.domain.errorreport.ErrorReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
class AdminController {
    private final ErrorReportService errorReportService;

    public AdminController(ErrorReportService errorReportService) {
        this.errorReportService = errorReportService;
    }

    public static final String NOTIFICATION_ATTRIBUTE = "notification";
    @GetMapping
    public String getAdminPanel() {
        return "admin/admin";
    }
    @GetMapping("/reports")
    public String viewErrorReports(Model model) {
        List<ErrorReport> errorReports = errorReportService.getAllErrorReports();
        model.addAttribute("errorReports", errorReports);
        return "/admin/users-reports";
    }

    @DeleteMapping("/delete-report/{id}")
    public String reportError(@RequestParam Long id) {
        errorReportService.deleteReport(id);
        return "redirect:/admin/view-reports" ;
    }
}