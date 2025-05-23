package com.example.demo.service;

import com.example.demo.model.Report;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import com.google.cloud.Timestamp;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReportService {
    private final List<Report> reportStore = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    // 🔁 Firestore 版：讀取 reports collection
    public List<Report> getAllReportsFromFirestore() {
        List<Report> reports = new ArrayList<>();
        try {
            Firestore db = FirestoreClient.getFirestore();
            ApiFuture<QuerySnapshot> future = db.collection("reports").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                Report report = doc.toObject(Report.class);
                report.setId(doc.getId()); // ✅ 這行是關鍵：將 Firestore 文件 ID 設定入 report.id
                reports.add(report);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return reports;
    }


    // ✅ 原本表單上傳使用（仍保留）
    public Report saveReport(String imageName, String site, String batchId, String workerName, String weather,
                             double confidence, String grade) {
        Report report = new Report();
        report.setImageName(imageName);
        report.setSite(site);
        report.setBatchId(batchId);
        report.setTimestamp(Timestamp.now());
        report.setWorkerId(workerName);
        report.setWeather(weather);
        report.setConfidence(confidence);
        report.setGrade(grade);
        report.setStatus("pending");
        report.setComment(null);

        reportStore.add(report);
        return report;
    }

    // ✅ 原有 in-memory 查詢方法（保留舊資料）
    public List<Report> getAllReports() {
        return reportStore;
    }

    public List<Report> getReportsByWorker(String workerName) {
        return reportStore.stream()
                .filter(r -> workerName.equals(r.getWorkerId()))
                .toList();
    }

    public Report getReportById(Long id) {
        return null; // Firestore 無 ID 索引，你可視情況補寫
    }

    public void updateReportStatus(String id, String status, String comment) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection("reports").document(id);
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("comment", comment);
        docRef.update(updates);
    }

}
