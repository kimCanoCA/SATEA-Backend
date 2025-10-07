package com.example.demo.Datos;

public class RiskSummary {
	private final int lowRiskCount;
    private final int moderateRiskCount;
    private final int highRiskCount;

    public RiskSummary(int lowRiskCount, int moderateRiskCount, int highRiskCount) {
        this.lowRiskCount = lowRiskCount;
        this.moderateRiskCount = moderateRiskCount;
        this.highRiskCount = highRiskCount;
    }

    // Asegúrate de incluir los Getters para que Jackson (Spring) pueda serializar el JSON
    public int getLowRiskCount() {
        return lowRiskCount;
    }

    public int getModerateRiskCount() {
        return moderateRiskCount;
    }

    public int getHighRiskCount() {
        return highRiskCount;
    }
}
