package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.model.Report; // ← ✅ 就係要加呢行！
import com.example.demo.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
        List<Report> reports = reportService.getAllReportsFromFirestore();
        model.addAttribute("reports", reports);
        return "reports";
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

    @GetMapping("/reports/add")
    public String showAddReportPage(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"worker".equals(user.getRole())) {
            return "error/403";
        }
        return "add-report";
    }

    @PostMapping("/reports/save")
    public String saveReport(@RequestParam("imageFile") MultipartFile imageFile,
                             @RequestParam String site,
                             @RequestParam String batchId,
                             @RequestParam String weather,
                             HttpSession session) throws IOException {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || !"worker".equals(user.getRole())) {
            return "error/403";
        }

        // 產生唯一檔名
        String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();

        // ✅ 用絕對路徑建立 uploads 目錄（避免寫入 temp）
        String uploadPath = System.getProperty("user.dir") + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        // 儲存圖片
        File dest = new File(uploadDir, imageName);
        imageFile.transferTo(dest);
        System.out.println("✅ Image saved to: " + dest.getAbsolutePath());

        // 模擬 AI 分析
        double confidence = 0.87;
        String grade = confidence >= 0.9 ? "A" : confidence >= 0.75 ? "B" : "C";

        // 儲存報告
        reportService.saveReport(imageName, site, batchId, user.getUsername(), weather, confidence, grade);
        return "redirect:/my-reports";
    }


    @PostMapping("/reports/{id}/approve")
    public String approveReport(@PathVariable("id") String id,
                                @RequestParam("comment") String comment,
                                HttpSession session) {
        reportService.updateReportStatus(id, "approved", comment);
        return "redirect:/reports";
    }


    @PostMapping("/reports/{id}/reject")
    public String rejectReport(@PathVariable("id") String id,
            @RequestParam("comment") String comment,
            HttpSession session) {
reportService.updateReportStatus(id, "rejected", comment);
return "redirect:/reports";
}

}
