package com.example.demo.Datos;

public class CorrelationResult {
	private String factor1;
    private String factor2;
    private Double correlationValue;
    private String patternDescription;
    
    public CorrelationResult(String factor1, String factor2, Double correlationValue, String patternDescription) {
        this.factor1 = factor1;
        this.factor2 = factor2;
        this.correlationValue = correlationValue;
        this.patternDescription = patternDescription;
    }

    // Getters y Setters
    public String getFactor1() { return factor1; }
    public void setFactor1(String factor1) { this.factor1 = factor1; }
    public String getFactor2() { return factor2; }
    public void setFactor2(String factor2) { this.factor2 = factor2; }
    public Double getCorrelationValue() { return correlationValue; }
    public void setCorrelationValue(Double correlationValue) { this.correlationValue = correlationValue; }
    public String getPatternDescription() { return patternDescription; }
    public void setPatternDescription(String patternDescription) { this.patternDescription = patternDescription; }
}
