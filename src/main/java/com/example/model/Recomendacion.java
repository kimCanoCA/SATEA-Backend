package com.example.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recomendacion")
public class Recomendacion {
	@Id
	@Column(name="id_recomendacion")
	private Long id_recomendacion;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="fecha_recomendacion")
	private Date fecha_recomendacion;

	public Recomendacion() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Recomendacion(Long id_recomendacion, String descripcion, Date fecha_recomendacion) {
		super();
		this.id_recomendacion = id_recomendacion;
		this.descripcion = descripcion;
		this.fecha_recomendacion = fecha_recomendacion;
	}

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
	
	

}
