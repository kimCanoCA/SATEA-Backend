package com.example.model;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "recomendaciones")
public class Recomendacion {

    @Id
    @Column(name = "id_recomendacion")
    private Long id_recomendacion;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_recomendacion")
    private Date fecha_recomendacion;

    // Relación con Estudiante (Muchos a Uno)
    @ManyToOne
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante")
    @JsonBackReference
    private Estudiante estudiante;

    // Relación con Consejero (Muchos a Uno)
    @ManyToOne
    @JoinColumn(name = "id_Consejero")
    @JsonBackReference(value = "consejero-recomendaciones")
    private Consejero consejero;


    // Relación con AnalisisRiesgo (Muchos a Uno)
    @ManyToOne
    @JoinColumn(name = "id_analisis", referencedColumnName = "id_analisis")
    private Analisis_Riesgo analisis_riesgo;

    // Constructor vacío
    public Recomendacion() {
        super();
    }

    // Constructor con parámetros
    public Recomendacion(Long id_recomendacion, String descripcion, Date fecha_recomendacion,
                         Estudiante estudiante, Consejero consejero, Analisis_Riesgo analisis_riesgo) {
        super();
        this.id_recomendacion = id_recomendacion;
        this.descripcion = descripcion;
        this.fecha_recomendacion = fecha_recomendacion;
        this.estudiante = estudiante;
        this.consejero = consejero;
        this.analisis_riesgo = analisis_riesgo;
    }

    // Getters y Setters
    public Long getId_recomendacion() {
        return id_recomendacion;
    }

    public void setId_recomendacion(Long id_recomendacion) {
        this.id_recomendacion = id_recomendacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_recomendacion() {
        return fecha_recomendacion;
    }

    public void setFecha_recomendacion(Date fecha_recomendacion) {
        this.fecha_recomendacion = fecha_recomendacion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Consejero getConsejero() {
        return consejero;
    }

    public void setConsejero(Consejero consejero) {
        this.consejero = consejero;
    }

    public Analisis_Riesgo getAnalisis_riesgo() {
        return analisis_riesgo;
    }

    public void setAnalisis_riesgo(Analisis_Riesgo analisis_riesgo) {
        this.analisis_riesgo = analisis_riesgo;
    }
}
