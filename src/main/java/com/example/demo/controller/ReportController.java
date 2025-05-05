package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reports")
    public String showAllReports(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"manager".equals(user.getRole())) {
            return "error/403";
        }
        model.addAttribute("reports", reportService.getAllReports());
        return "reports";
    }

    @GetMapping("/reports/add")
    public String showAddReportPage(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"worker".equals(user.getRole())) {
            return "error/403";
        }
        return "add-report";
    }

    @GetMapping("/my-reports")
    public String showMyReports(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"worker".equals(user.getRole())) {
            return "error/403";
        }
        model.addAttribute("reports", reportService.getReportsByWorker(user.getUsername()));
        return "my-reports";
    }

    @PostMapping("/reports/save")
    public String saveReport(@RequestParam String imageName,
                             @RequestParam String site,
                             @RequestParam String batchId,
                             @RequestParam String weather,
                             HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"worker".equals(user.getRole())) {
            return "error/403";
        }

        // 模擬 AI 分析
        double confidence = 0.87;
        String grade = confidence >= 0.9 ? "A" : confidence >= 0.75 ? "B" : "C";

        reportService.saveReport(imageName, site, batchId, user.getUsername(), weather, confidence, grade);
        return "redirect:/my-reports";
    }

    @PostMapping("/reports/{id}/approve")
    public String approveReport(@PathVariable Long id,
                                @RequestParam(required = false) String comment,
                                HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"manager".equals(user.getRole())) {
            return "error/403";
        }
        reportService.updateReportStatus(id, "approved", comment);
        return "redirect:/reports";
    }

    @PostMapping("/reports/{id}/reject")
    public String rejectReport(@PathVariable Long id,
                               @RequestParam(required = false) String comment,
                               HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"manager".equals(user.getRole())) {
            return "error/403";
        }
        reportService.updateReportStatus(id, "rejected", comment);
        return "redirect:/reports";
    }
}
