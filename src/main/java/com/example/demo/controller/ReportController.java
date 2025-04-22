package com.example.demo.controller;

import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reports")
    public String showAllReports(@RequestParam(defaultValue = "worker") String role, Model model) {
        if (!"manager".equals(role)) {
            return "error/403"; // 自己整個簡單 HTML 螢幕顯示無權限
        }
        model.addAttribute("reports", reportService.getAllReports());
        return "reports";
    }

    @PostMapping("/reports/{id}/approve")
    public String approveReport(@PathVariable Long id, @RequestParam(required = false) String comment) {
        reportService.updateReportStatus(id, "approved", comment);
        return "redirect:/reports?role=manager";
    }

    @PostMapping("/reports/{id}/reject")
    public String rejectReport(@PathVariable Long id, @RequestParam(required = false) String comment) {
        reportService.updateReportStatus(id, "rejected", comment);
        return "redirect:/reports?role=manager";
    }
}

