package com.example.demo.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Datos.CorrelationResult;
import com.example.demo.Datos.Recommendation;
import com.example.demo.Datos.RiskSegment;
import com.example.model.Estudiante;

@Service
public class RiskAnalysisService {
	private final DataService dataService;

    public RiskAnalysisService(DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * RF1.1 & RF1.2: Analiza y devuelve las correlaciones de factores de estrés (Simulado).
     */
    public List<CorrelationResult> getCorrelations() {
        // Lógica simulada: Se implementa aquí el análisis de correlación (e.g., Pearson).
        return Arrays.asList(
            new CorrelationResult("study_load", "sleep_quality", -0.75, "Correlación Negativa Fuerte: Mayor carga de estudio, menor calidad del sueño."),
            new CorrelationResult("anxiety_level", "headache", 0.68, "Correlación Positiva Moderada: Altos niveles de ansiedad se relacionan con más dolores de cabeza."),
            new CorrelationResult("social_support", "depression", -0.55, "Correlación Negativa Moderada: Mayor soporte social reduce los niveles de depresión.")
        );
    }

    /**
     * RF2.1 & RF2.2: Segmenta a los estudiantes en grupos de riesgo (Simulado).
     */
    public List<RiskSegment> getRiskSegmentation() {
        int totalStudents = dataService.getAllEstudiantes().size();
        
        // Simulación de conteos basada en porcentajes (ej. 15% alto, 35% moderado, 50% bajo)
        int highRiskCount = (int) (totalStudents * 0.15); 
        int moderateRiskCount = (int) (totalStudents * 0.35);
        int lowRiskCount = totalStudents - highRiskCount - moderateRiskCount;


        return Arrays.asList(
                // ¡AHORA SE PASAN LOS 4 ARGUMENTOS AL CONSTRUCTOR!
                new RiskSegment("Alto Riesgo", highRiskCount, "Puntaje de estrés severo. Requiere intervención inmediata.", Arrays.asList("stress_level", "depression", "study_load")),
                new RiskSegment("Riesgo Moderado", moderateRiskCount, "Puntaje de estrés entre 5 y 8. Monitoreo y apoyo proactivo.", Arrays.asList("anxiety_level", "peer_pressure")),
                new RiskSegment("Bajo Riesgo", lowRiskCount, "Puntaje de estrés bajo. Monitoreo rutinario.", Arrays.asList("social_support")) // Se añade un factor de ejemplo para el segmento bajo
            );
        }

    /**
     * RF3.1 & RF3.2: Genera recomendaciones personalizadas para un estudiante (Simulado).
     */
    public List<Recommendation> getPersonalizedRecommendations(Long idEstudiante) {
        Optional<Estudiante> estudianteOpt = dataService.getEstudianteById(idEstudiante);
        if (estudianteOpt.isEmpty()) {
            return List.of(); 
        }
        
        Estudiante estudiante = estudianteOpt.get();
        // Obtener la carrera, usando un valor por defecto si es nula.
        String carrera = estudiante.getCarrera() != null && !estudiante.getCarrera().trim().isEmpty() 
                         ? estudiante.getCarrera() 
                         : "tu área de estudio"; 
        
        // Simulación: Genera recomendaciones basadas en los indicadores altos del estudiante.
        if (estudiante.getAnxietyLevel() >= 7 && estudiante.getSleepQuality() <= 4) {
            return Arrays.asList(
                new Recommendation("Taller de Manejo de la Ansiedad", "Sesiones grupales para aprender técnicas de relajación.", "Psicólogo Universitario"),
                new Recommendation("Consejos para la Higiene del Sueño", "Revisar los hábitos de sueño y horarios.", "Centro de Bienestar")
            );
        } else if (estudiante.getAcademicPerformance() <= 4 && estudiante.getStudyLoad() >= 7) {
            return Arrays.asList(
                // Recomendación ahora incluye la carrera del estudiante
                new Recommendation("Asesoría Académica Específica", 
                                   "Sesiones uno a uno para optimizar técnicas de estudio relevantes para la carrera de " + carrera + ".", 
                                   "Coordinación Académica"),
                new Recommendation("Taller de Gestión del Tiempo", "Aprender a priorizar tareas y reducir la carga percibida.", "Servicios Estudiantiles")
            );
        } else {
            return Arrays.asList(
                new Recommendation("Revisión de Bienestar General", "Agenda una cita para una revisión completa de salud mental.", "Centro de Bienestar")
            );
        }
    }
}
