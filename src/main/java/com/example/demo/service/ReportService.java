package com.example.demo.service;

import com.example.demo.model.Report;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReportService {
    private final List<Report> reportStore = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Report saveReport(String imageName, String site, String batchId, String workerName, String weather,
                             double confidence, String grade) {
        Report report = new Report(
                idCounter.getAndIncrement(),
                imageName,
                site,
                batchId,
                LocalDateTime.now(),
                workerName,
                weather,
                confidence,
                grade,
                "pending",
                null
        );
        reportStore.add(report);
        return report;
    }

    public List<Report> getAllReports() {
        return reportStore;
    }

    public Report getReportById(Long id) {
        return reportStore.stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean updateStatus(Long id, String status, String comment) {
        Report report = getReportById(id);
        if (report != null) {
            report.setStatus(status);
            report.setComment(comment);
            return true;
        }
        return false;
    }
} 
