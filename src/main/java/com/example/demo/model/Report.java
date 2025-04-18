package com.example.demo.model;

import java.time.LocalDateTime;

public class Report {
    private Long id;
    private String imageName;
    private String site;
    private String batchId;
    private LocalDateTime timestamp;
    private String workerName;
    private String weather;
    private double confidence;
    private String grade;
    private String status; // e.g. pending, approved, rejected
    private String comment;

    // Constructors
    public Report() {}

    public Report(Long id, String imageName, String site, String batchId, LocalDateTime timestamp,
                  String workerName, String weather, double confidence, String grade,
                  String status, String comment) {
        this.id = id;
        this.imageName = imageName;
        this.site = site;
        this.batchId = batchId;
        this.timestamp = timestamp;
        this.workerName = workerName;
        this.weather = weather;
        this.confidence = confidence;
        this.grade = grade;
        this.status = status;
        this.comment = comment;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public String getSite() { return site; }
    public void setSite(String site) { this.site = site; }

    public String getBatchId() { return batchId; }
    public void setBatchId(String batchId) { this.batchId = batchId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getWorkerName() { return workerName; }
    public void setWorkerName(String workerName) { this.workerName = workerName; }

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
