package com.example.demo.Datos;

public class RiskSummaryDTO {
    private String nivel;      // "BAJO", "MODERADO", "ALTO"
    private int count;         // Número de estudiantes
    private double percentage; // Porcentaje (0.0 a 1.0)
	public String getNivel() {
		return nivel;
	}
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getPercentage() {
		return percentage;
	}
	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

}
