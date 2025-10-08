package com.example.demo.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.Datos.CorrelationResult;
import com.example.demo.Datos.Recommendation;
import com.example.demo.Datos.RiskSegment;
import com.example.demo.Datos.RiskSummary; 
import com.example.model.Estudiante; 

@Service
public class RiskAnalysisService {
	private final DataService dataService;

    public RiskAnalysisService(DataService dataService) {
        this.dataService = dataService;
    }

    // ======================================================================
    // RF1: Correlaciones y Factores de Riesgo
    // ======================================================================
    
    /**
     * RF1.1 & RF1.2: Analiza y devuelve las correlaciones de factores de estrés (Simulado - Modelo General).
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
     * RF1.2: Obtiene las correlaciones relevantes para un estudiante específico.
     */
 // --- Pega este nuevo método dentro de la clase RiskAnalysisService ---

    /**
     * Calcula y devuelve el nivel de riesgo de un estudiante basado en sus datos.
     * @param estudiante El estudiante a analizar.
     * @return Una cadena de texto: "Alto", "Moderado" o "Bajo".
     */
    /**
     * Calcula y devuelve el nivel de riesgo de un estudiante basado en sus datos.
     * @param estudiante El estudiante a analizar.
     * @return Una cadena de texto: "Alto", "Moderado" o "Bajo".
     */
    public String calculateRiskLevel(Estudiante estudiante) {
        // Reglas para determinar el riesgo (puedes ajustarlas según tu modelo)

        // **Criterio de Riesgo Alto:**
        // Si la ansiedad O la depresión son muy altas, O si el rendimiento académico es muy bajo
        // Y la carga de estudio es alta.
        if ((estudiante.getAnxietyLevel() != null && estudiante.getAnxietyLevel() >= 7) ||
            (estudiante.getDepression() != null && estudiante.getDepression() >= 7) ||
            (estudiante.getAcademicPerformance() != null && estudiante.getAcademicPerformance() <= 4 &&
             estudiante.getStudyLoad() != null && estudiante.getStudyLoad() >= 7)) {
            return "Alto";
        }

        // **Criterio de Riesgo Moderado:**
        // Si el estrés es moderado, la calidad del sueño es regular o si hay preocupaciones sobre el futuro.
        if ((estudiante.getStressLevel() != null && estudiante.getStressLevel() >= 5) ||
            (estudiante.getSleepQuality() != null && estudiante.getSleepQuality() <= 4) ||
            (estudiante.getFutureCareerConcerns() != null && estudiante.getFutureCareerConcerns() >= 6)) {
            return "Moderado";
        }

        // **Por defecto, el riesgo es Bajo.**
        return "Bajo";
    }

    // ======================================================================
    // RF2: Segmentación y Resumen de Riesgo (Dashboard)
    // ======================================================================

    /**
     * RF2.1: Devuelve un resumen simple de los conteos por segmento de riesgo (para el Dashboard).
     */
    public RiskSummary getRiskSummary() {
        // 🚨 CORRECCIÓN CLAVE: Dejar de SIMULAR y obtener los conteos REALES
        int lowRiskCount = dataService.countStudentsByRiskLevel("BAJO"); 
        int moderateRiskCount = dataService.countStudentsByRiskLevel("MODERADO");
        int highRiskCount = dataService.countStudentsByRiskLevel("ALTO"); 

        return new RiskSummary(lowRiskCount, moderateRiskCount, highRiskCount);
    }

    /**
     * RF2.1 & RF2.2: Segmenta a los estudiantes en grupos de riesgo.
     * Refactorizado para usar getRiskSummary().
     */
    public List<RiskSegment> getRiskSegmentation() {
        RiskSummary summary = getRiskSummary(); 
        
        return Arrays.asList(
                new RiskSegment("Alto Riesgo", summary.getHighRiskCount(), "Puntaje de estrés severo. Requiere intervención inmediata.", Arrays.asList("stress_level", "depression", "study_load")),
                new RiskSegment("Riesgo Moderado", summary.getModerateRiskCount(), "Puntaje de estrés entre 5 y 8. Monitoreo y apoyo proactivo.", Arrays.asList("anxiety_level", "peer_pressure")),
                new RiskSegment("Bajo Riesgo", summary.getLowRiskCount(), "Puntaje de estrés bajo. Monitoreo rutinario.", Arrays.asList("social_support"))
            );
        }

    // ======================================================================
    // RF3: Recomendaciones Personalizadas
    // ======================================================================

    /**
     * RF3.1 & RF3.2: Genera recomendaciones personalizadas para un estudiante (Descripciones DETALLADAS).
     */
    public List<Recommendation> getPersonalizedRecommendations(Long idEstudiante) {
        Optional<Estudiante> estudianteOpt = dataService.getEstudianteById(idEstudiante);
        if (estudianteOpt.isEmpty()) {
            return List.of(); 
        }
        
        Estudiante estudiante = estudianteOpt.get();
        String carrera = estudiante.getCarrera() != null && !estudiante.getCarrera().trim().isEmpty() 
                         ? estudiante.getCarrera() 
                         : "tu área de estudio"; 
        
        List<Recommendation> recommendations = new ArrayList<>();

        // -- 1. SALUD MENTAL y BIENESTAR --
        // Ansiedad y Depresión (Riesgo Alto)
        if (estudiante.getAnxietyLevel() >= 7 || estudiante.getDepression() >= 7) {
            recommendations.add(new Recommendation(
                "Terapia Psicológica Individual Inmediata",
                "**Seguimiento de Crisis:** Se requiere iniciar sesiones individuales urgentes con un psicólogo clínico. La terapia se enfocará en el desarrollo de **habilidades de afrontamiento** y en técnicas específicas para el manejo de los **síntomas agudos de ansiedad y/o depresión**.",
                "Psicólogo Clínico Universitario"
            ));
        }

        // Calidad del Sueño baja y Estrés moderado (Riesgo Moderado)
        if (estudiante.getSleepQuality() <= 4 && estudiante.getStressLevel() >= 5) {
            recommendations.add(new Recommendation(
                "Taller Avanzado de Higiene del Sueño",
                "Participación en un taller especializado que revisa los **hábitos pre-sueño**, optimización del **entorno de descanso** (especialmente si el ruido es alto), y estrategias de **relajación profunda** para facilitar un sueño reparador y continuo.",
                "Centro de Bienestar"
            ));
        }
        
        // -- 2. ACADÉMICO y CARGA --
        // Bajo Rendimiento y Carga Alta (Riesgo Alto)
        if (estudiante.getAcademicPerformance() <= 4 && estudiante.getStudyLoad() >= 7) {
            recommendations.add(new Recommendation(
                "Asesoría Académica Personalizada (Programa de Alto Impacto)",
                "Sesión intensiva para la optimización de **técnicas de estudio** específicas para la carrera de **" + carrera + "**, **planificación estratégica de la carga** y desarrollo de un **horario de estudio sostenible** que maximice resultados y reduzca el agotamiento.",
                "Coordinación Académica/Tutoría"
            ));
        }

        // Preocupaciones de Carrera/Futuro (Riesgo Moderado)
        if (estudiante.getFutureCareerConcerns() >= 6) {
            recommendations.add(new Recommendation(
                "Coaching Vocacional y Laboral",
                "Un programa estructurado de 4 semanas para explorar **opciones de carrera**, **reducir la incertidumbre** sobre el futuro profesional y conectar al estudiante con **recursos de prácticas y orientación** en su campo de interés.",
                "Servicios de Orientación"
            ));
        }
        
        // -- 3. FACTORES SOCIALES y BÁSICOS --
        // Bajo Soporte Social o Bullying (Riesgo Alto)
        if (estudiante.getSocialSupport() <= 4 || estudiante.getPeerPressure() >= 7 || estudiante.getBullying() >= 5) {
            recommendations.add(new Recommendation(
                "Programa de Integración y Tutoría de Pares",
                "Asignación a un **grupo de apoyo social** supervisado por Trabajo Social Universitario y emparejamiento con un **tutor senior** para fomentar la conexión, la seguridad y la construcción de una **red de soporte** efectiva.",
                "Trabajo Social Universitario"
            ));
            if (estudiante.getBullying() >= 5) {
                 recommendations.add(new Recommendation(
                    "Activación de Protocolo Anti-Acoso (Acción Legal)",
                    "**Seguimiento confidencial de la denuncia.** Se proveerá **asesoría legal** y acompañamiento para la aplicación inmediata del **protocolo institucional de prevención y sanción** contra el acoso escolar (bullying), priorizando la seguridad del estudiante.",
                    "Departamento Legal/Decanato"
                ));
            }
        }

        // Necesidades Básicas Comprometidas (Riesgo CRÍTICO)
        if (estudiante.getBasicNeeds() <= 4 || estudiante.getLivingConditions() <= 4 || estudiante.getSafety() <= 4) {
             recommendations.add(new Recommendation(
                "Asistencia de Emergencia (Ayudas Socioeconómicas)",
                "**Evaluación inmediata** de elegibilidad para becas de subsistencia, **ayuda alimentaria de emergencia** y/o reubicación temporal en **alojamiento seguro** si las condiciones de vivienda o seguridad representan un riesgo crítico.",
                "Departamento de Ayudas Financieras"
            ));
        }
        
        // -- 4. RECOMENDACIÓN PROACTIVA / POR DEFECTO --
        if (recommendations.isEmpty()) {
            recommendations.add(new Recommendation(
                "Chequeo de Bienestar y Hábitos (Enfoque Preventivo)",
                "Dado el bajo perfil de riesgo, se recomienda una **consulta preventiva** para revisar hábitos de estudio, nutrición y actividad física, asegurando el mantenimiento de un **estilo de vida óptimo** y la prevención de futuros factores de estrés.",
                "Centro de Bienestar General"
            ));
             recommendations.add(new Recommendation(
                "Participación en Extensión Cultural",
                "Explorar y comprometerse con al menos dos **actividades extracurriculares o clubes** universitarios para fomentar el **equilibrio entre vida académica y personal** y fortalecer la conexión con la comunidad.",
                "Departamento de Extensión Cultural"
            ));
        }
        
        return recommendations;
    }
}
