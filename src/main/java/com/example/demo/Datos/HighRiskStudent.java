package com.example.demo.Datos;

import java.util.List;

public class HighRiskStudent {
	
	    private final Long idEstudiante;
	    private final String nombre;
	    private final String email;
	    private final String carrera;
	    private final String nivelRiesgo; // 👈 5º campo: el nivel de riesgo detallado
	    private final List<Recommendation> recomendacionesUrgentes;
		public HighRiskStudent(Long idEstudiante, String nombre, String email, String carrera, String nivelRiesgo,
				List<Recommendation> recomendacionesUrgentes) {
			super();
			this.idEstudiante = idEstudiante;
			this.nombre = nombre;
			this.email = email;
			this.carrera = carrera;
			this.nivelRiesgo = nivelRiesgo;
			this.recomendacionesUrgentes = recomendacionesUrgentes;
		}
		public Long getIdEstudiante() {
			return idEstudiante;
		}
		public String getNombre() {
			return nombre;
		}
		public String getEmail() {
			return email;
		}
		public String getCarrera() {
			return carrera;
		}
		public String getNivelRiesgo() {
			return nivelRiesgo;
		}
		public List<Recommendation> getRecomendacionesUrgentes() {
			return recomendacionesUrgentes;
		}
	    
	
	}