package com.example.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "analisis_riesgo")
public class Analisis_Riesgo {
	@Id
	@Column(name="id_analisis")
	private Long id_analisis;
	
	@Column(name="segmento_riesgo")
	private String segmento_riesgo;
	
	@Column(name="factores_clave_correlacion")
	private String factores_clave_correlacion;
	
	@Column(name="fecha_analisis")
	private Date fecha_analisis;


	// Relación AnalisisRiesgo -> Estudiante (Uno a Muchos)
    @OneToMany
    @JoinColumn(name = "id_analisis", referencedColumnName = "id_analisis")
    private List<Estudiante> estudiantes;

    // Relación AnalisisRiesgo -> Recomendacion (Uno a Muchos)
    @OneToMany
    @JoinColumn(name = "id_analisis", referencedColumnName = "id_analisis")
    private List<Recomendacion> recomendaciones;

    public Analisis_Riesgo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Analisis_Riesgo(Long id_analisis, String segmento_riesgo, String factores_clave_correlacion,
			Date fecha_analisis, List<Estudiante> estudiantes, List<Recomendacion> recomendaciones) {
		super();
		this.id_analisis = id_analisis;
		this.segmento_riesgo = segmento_riesgo;
		this.factores_clave_correlacion = factores_clave_correlacion;
		this.fecha_analisis = fecha_analisis;
		this.estudiantes = estudiantes;
		this.recomendaciones = recomendaciones;
	}

	public Long getId_analisis() {
		return id_analisis;
	}

	public void setId_analisis(Long id_analisis) {
		this.id_analisis = id_analisis;
	}

	public String getSegmento_riesgo() {
		return segmento_riesgo;
	}

	public void setSegmento_riesgo(String segmento_riesgo) {
		this.segmento_riesgo = segmento_riesgo;
	}

	public String getFactores_clave_correlacion() {
		return factores_clave_correlacion;
	}

	public void setFactores_clave_correlacion(String factores_clave_correlacion) {
		this.factores_clave_correlacion = factores_clave_correlacion;
	}

	public Date getFecha_analisis() {
		return fecha_analisis;
	}

	public void setFecha_analisis(Date fecha_analisis) {
		this.fecha_analisis = fecha_analisis;
	}

	public List<Estudiante> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(List<Estudiante> estudiantes) {
		this.estudiantes = estudiantes;
	}

	public List<Recomendacion> getRecomendaciones() {
		return recomendaciones;
	}

	public void setRecomendaciones(List<Recomendacion> recomendaciones) {
		this.recomendaciones = recomendaciones;
	}

}
