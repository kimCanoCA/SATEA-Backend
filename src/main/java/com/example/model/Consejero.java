package com.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "consejero")
public class Consejero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_consejero") // <-- 🔹 CORREGIDO: en minúsculas y consistente con las demás clases
    private Long id_consejero;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="rol")
    private String rol;

    @Column(name="fecha_creacion")
    private Date fecha_creacion;

    @Column(name="nombre")
    private String nombre;

    @Column(name="email")
    private String email;

    // --- RELACIÓN CON ESTUDIANTE ---
    @OneToMany(mappedBy = "consejero", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "consejero-estudiantes")
    private List<Estudiante> estudiantes;

    @OneToMany(mappedBy = "consejero", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "consejero-recomendaciones")
    private List<Recomendacion> recomendaciones;


    public Consejero() {}

    public Consejero(Long id_consejero, String username, String password, String rol, Date fecha_creacion,
                     String nombre, String email, List<Estudiante> estudiantes, List<Recomendacion> recomendaciones) {
        this.id_consejero = id_consejero;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.fecha_creacion = fecha_creacion;
        this.nombre = nombre;
        this.email = email;
        this.estudiantes = estudiantes;
        this.recomendaciones = recomendaciones;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId_consejero() {
        return id_consejero;
    }

    public void setId_consejero(Long id_consejero) {
        this.id_consejero = id_consejero;
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
