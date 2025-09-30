package com.example.demo.Datos;

import java.util.List;

public class RiskSegment {
    private String segmentName;
    private Integer studentCount;
    private String description;
    // ¡ESTA LISTA ES EL CUARTO ARGUMENTO QUE FALTA EN TU LLAMADA!
    private List<String> keyContributingFactors; 

    public RiskSegment(String segmentName, Integer studentCount, String description, List<String> keyContributingFactors) {
        this.segmentName = segmentName;
        this.studentCount = studentCount;
        this.description = description;
        this.keyContributingFactors = keyContributingFactors;
    }

    // Getters y Setters...
    public String getSegmentName() { return segmentName; }
    public void setSegmentName(String segmentName) { this.segmentName = segmentName; }
    public Integer getStudentCount() { return studentCount; }
    public void setStudentCount(Integer studentCount) { this.studentCount = studentCount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<String> getKeyContributingFactors() { return keyContributingFactors; }
    public void setKeyContributingFactors(List<String> keyContributingFactors) { this.keyContributingFactors = keyContributingFactors; }
}
