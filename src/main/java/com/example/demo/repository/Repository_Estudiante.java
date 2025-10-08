package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Estudiante;
import com.example.model.Consejero; 
import java.util.List;  

;

public interface Repository_Estudiante extends JpaRepository<Estudiante, Long> {
	 Long countByNivelRiesgo(String nivelRiesgo);
	// Busca todos los estudiantes asociados a un consejero específico.
	    List<Estudiante> findByConsejero(Consejero consejero);
}
