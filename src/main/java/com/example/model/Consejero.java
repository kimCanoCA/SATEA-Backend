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
	
	// Relación Consejero -> Estudiante (Uno a Muchos)
    @OneToMany
    @JoinColumn(name = "id_consejero", referencedColumnName = "id_consejero")
    private List<Estudiante> estudiantes;

    // Relación Consejero -> Recomendacion (Uno a Muchos)
    @OneToMany
    @JoinColumn(name = "id_consejero", referencedColumnName = "id_consejero")
    private List<Recomendacion> recomendaciones;
	
    public Consejero() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Consejero(Long id_Consejero, String username, String password, String rol, Date fecha_creacion,
			String nombre, String email, List<Estudiante> estudiantes, List<Recomendacion> recomendaciones) {
		super();
		this.id_Consejero = id_Consejero;
		this.username = username;
		this.password = password;
		this.rol = rol;
		this.fecha_creacion = fecha_creacion;
		this.nombre = nombre;
		this.email = email;
		this.estudiantes = estudiantes;
		this.recomendaciones = recomendaciones;
	}

	public Long getId_Consejero() {
		return id_Consejero;
	}

	public void setId_Consejero(Long id_Consejero) {
		this.id_Consejero = id_Consejero;
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
