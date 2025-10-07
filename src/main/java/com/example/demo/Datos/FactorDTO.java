package com.example.demo.Datos;



public class FactorDTO {
	
    private String nombre;     // Nombre del factor de estrés (Ej: "Carga Académica")
    private double porcentaje; // Porcentaje de estudiantes afectados (0.0 a 1.0)

    // -------------------------------------------------------------
    // 1. CONSTRUCTOR COMPLETO (NECESARIO para DataService)
    // -------------------------------------------------------------
    public FactorDTO(String nombre, double porcentaje) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
    }

    // -------------------------------------------------------------
    // 2. GETTERS Y SETTERS (Limpios y requeridos por Spring/JSON)
    // -------------------------------------------------------------
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public double getPorcentaje() {
        return porcentaje;
    }
    
    public void setPorcentaje(double porcentaje) {
        this.porcentaje = porcentaje;
    }
}
