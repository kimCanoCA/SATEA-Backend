package com.example.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "consejero")
public class Consejero {
	@Id
	@Column(name="id_Consejero")
	private Long id_Consejero;
	
	@Column(name="username")
	private String username;
	
	@Column(name="password")
	private  String password;
	
	@Column(name="rol")
	private String rol;
	
	@Column(name="fecha_creacion")
	private Date fecha_creacion;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="email")
	private String email;

	public Consejero() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Consejero(Long idConsejero, String username, String password, String rol, Date fecha_creacion, String nombre,
			String email) {
		super();
		this.id_Consejero = idConsejero;
		this.username = username;
		this.password = password;
		this.rol = rol;
		this.fecha_creacion = fecha_creacion;
		this.nombre = nombre;
		this.email = email;
	}

	public Long getIdConsejero() {
		return id_Consejero;
	}

	public void setIdConsejero(Long idConsejero) {
		this.id_Consejero = idConsejero;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public Date getFecha_creacion() {
		return fecha_creacion;
	}

	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
