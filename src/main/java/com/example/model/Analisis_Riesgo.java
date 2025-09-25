package com.example.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

	public Analisis_Riesgo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Analisis_Riesgo(Long id_analisis, String segmento_riesgo, String factores_clave_correlacion,
			Date fecha_analisis) {
		super();
		this.id_analisis = id_analisis;
		this.segmento_riesgo = segmento_riesgo;
		this.factores_clave_correlacion = factores_clave_correlacion;
		this.fecha_analisis = fecha_analisis;
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


}
