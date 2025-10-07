package com.example.demo.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Estudiante;
import com.example.demo.Datos.FactorDTO;
import com.example.demo.repository.Repository_Estudiante;


@Service
public class DataService {

    private final Repository_Estudiante estudianteRepository;

    public DataService(Repository_Estudiante estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    // -------------------------------------------------------------
    // RIMP3.3: Procesa archivo CSV (Lógica existente y correcta)
    // -------------------------------------------------------------
    @Transactional
    public int processAndSaveStudents(MultipartFile file) throws IOException {
        List<Estudiante> estudiantes = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            reader.readLine(); // Descarta la cabecera

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",", -1);
                
                if (values.length >= 26) { 
                    try {
                        Estudiante estudiante = new Estudiante();
                        // ... Lógica de parseo y asignación de campos (dejada igual) ...
                        estudiante.setIdEstudiante(Long.parseLong(values[0].trim()));
                        estudiante.setNombre(values[1].trim());
                        estudiante.setEmail(values[2].trim());
                        try {
                            estudiante.setEdad(Integer.parseInt(values[3].trim())); 
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            estudiante.setEdad(null); 
                        }
                        estudiante.setCarrera(values[4].trim());
                        estudiante.setAnxietyLevel(Integer.parseInt(values[5].trim()));
                        estudiante.setSelfEsteem(Integer.parseInt(values[6].trim()));
                        estudiante.setMentalHealthHistory(Integer.parseInt(values[7].trim()));
                        estudiante.setDepression(Integer.parseInt(values[8].trim()));
                        estudiante.setHeadache(Integer.parseInt(values[9].trim()));
                        estudiante.setBloodPressure(Integer.parseInt(values[10].trim()));
                        estudiante.setSleepQuality(Integer.parseInt(values[11].trim()));
                        estudiante.setBreathingProblem(Integer.parseInt(values[12].trim()));
                        estudiante.setNoiseLevel(Integer.parseInt(values[13].trim()));
                        estudiante.setLivingConditions(Integer.parseInt(values[14].trim()));
                        estudiante.setSafety(Integer.parseInt(values[15].trim()));
                        estudiante.setBasicNeeds(Integer.parseInt(values[16].trim()));
                        estudiante.setAcademicPerformance(Integer.parseInt(values[17].trim()));
                        estudiante.setStudyLoad(Integer.parseInt(values[18].trim()));
                        estudiante.setTeacherStudentRelationship(Integer.parseInt(values[19].trim()));
                        estudiante.setFutureCareerConcerns(Integer.parseInt(values[20].trim()));
                        estudiante.setSocialSupport(Integer.parseInt(values[21].trim()));
                        estudiante.setPeerPressure(Integer.parseInt(values[22].trim()));
                        estudiante.setExtracurricularActivities(Integer.parseInt(values[23].trim()));
                        estudiante.setBullying(Integer.parseInt(values[24].trim()));
                        estudiante.setStressLevel(Integer.parseInt(values[25].trim()));

                        // Calcular riesgo para el estudiante importado
                        estudiante.setNivelRiesgo(calcularNivelRiesgo(estudiante));
                        
                        estudiantes.add(estudiante);

                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico o ID no válido en la línea: " + line);
                    }
                } else {
                    System.err.println("Error de longitud de línea (esperado 26 campos o más): " + line);
                }
            }
        }

        estudianteRepository.saveAll(estudiantes);
        return estudiantes.size();
    }


    // 1. Obtener la lista de estudiantes
    public List<Estudiante> getAllEstudiantes() {
        return estudianteRepository.findAll();
    }

    // 2. Obtener un estudiante por ID
    public Optional<Estudiante> getEstudianteById(Long id) {
        return estudianteRepository.findById(id);
    }

    // 3. Crear (o Guardar) un estudiante
    public Estudiante saveEstudiante(Estudiante estudiante) {
        String riesgoCalculado = calcularNivelRiesgo(estudiante);
        estudiante.setNivelRiesgo(riesgoCalculado);
        
        return estudianteRepository.save(estudiante);
    }

    // 4. Actualizar un estudiante
    @Transactional 
    public Optional<Estudiante> updateEstudiante(Long id, Estudiante estudianteDetails) {
        return estudianteRepository.findById(id).map(estudianteExistente -> {
            // ... Actualizar todos los campos (dejado igual) ...
            estudianteExistente.setNombre(estudianteDetails.getNombre());
            estudianteExistente.setEmail(estudianteDetails.getEmail());
            estudianteExistente.setCarrera(estudianteDetails.getCarrera());
            estudianteExistente.setEdad(estudianteDetails.getEdad()); 
            estudianteExistente.setAnxietyLevel(estudianteDetails.getAnxietyLevel());
            estudianteExistente.setSelfEsteem(estudianteDetails.getSelfEsteem());
            estudianteExistente.setMentalHealthHistory(estudianteDetails.getMentalHealthHistory());
            estudianteExistente.setDepression(estudianteDetails.getDepression());
            estudianteExistente.setHeadache(estudianteDetails.getHeadache());
            estudianteExistente.setBloodPressure(estudianteDetails.getBloodPressure());
            estudianteExistente.setSleepQuality(estudianteDetails.getSleepQuality());
            estudianteExistente.setBreathingProblem(estudianteDetails.getBreathingProblem());
            estudianteExistente.setNoiseLevel(estudianteDetails.getNoiseLevel());
            estudianteExistente.setLivingConditions(estudianteDetails.getLivingConditions());
            estudianteExistente.setSafety(estudianteDetails.getSafety());
            estudianteExistente.setBasicNeeds(estudianteDetails.getBasicNeeds());
            estudianteExistente.setAcademicPerformance(estudianteDetails.getAcademicPerformance());
            estudianteExistente.setStudyLoad(estudianteDetails.getStudyLoad());
            estudianteExistente.setTeacherStudentRelationship(estudianteDetails.getTeacherStudentRelationship());
            estudianteExistente.setFutureCareerConcerns(estudianteDetails.getFutureCareerConcerns());
            estudianteExistente.setSocialSupport(estudianteDetails.getSocialSupport());
            estudianteExistente.setPeerPressure(estudianteDetails.getPeerPressure());
            estudianteExistente.setExtracurricularActivities(estudianteDetails.getExtracurricularActivities());
            estudianteExistente.setBullying(estudianteDetails.getBullying());
            estudianteExistente.setStressLevel(estudianteDetails.getStressLevel());

            // Volver a calcular el riesgo
            String riesgoCalculado = calcularNivelRiesgo(estudianteExistente); 
            estudianteExistente.setNivelRiesgo(riesgoCalculado);
            
            return estudianteRepository.save(estudianteExistente);
        });
    }

    // 5. Eliminar un estudiante
    public boolean deleteEstudiante(Long id) {
        if (estudianteRepository.existsById(id)) {
            estudianteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // 6. Método: Lógica de Cálculo de Riesgo (dejada igual)
    private String calcularNivelRiesgo(Estudiante estudiante) {
        int sumScores = 0;
        
        sumScores += Optional.ofNullable(estudiante.getAnxietyLevel()).orElse(0);
        sumScores += Optional.ofNullable(estudiante.getDepression()).orElse(0);
        sumScores += Optional.ofNullable(estudiante.getStressLevel()).orElse(0);
        
        // Reglas de clasificación:
        if (sumScores >= 25) {
            return "ALTO";
        }
        if (sumScores >= 15) {
            return "MODERADO";
        }
        return "BAJO";
    }
    
    // ----------------------------------------------------------------------
    // 7. ✅ NUEVO MÉTODO REAL: Conteo por Nivel de Riesgo (Gráfico de Barras)
    // ----------------------------------------------------------------------
    /**
     * Devuelve el número de estudiantes que coinciden con el nivel de riesgo dado.
     * Usado para alimentar el Gráfico de Barras.
     */
    public int countStudentsByRiskLevel(String nivel) {
        // Esto depende de que hayas añadido el método en tu Repository_Estudiante
        return estudianteRepository.countByNivelRiesgo(nivel).intValue();
    }

    // ----------------------------------------------------------------------
    // 8. ✅ NUEVO MÉTODO REAL: Obtener Factores de Estrés (Gráfico Circular)
    // ----------------------------------------------------------------------
    /**
     * Calcula los porcentajes reales de los principales factores de estrés
     * basándose en un umbral de riesgo (score >= 8).
     */
    public List<FactorDTO> getTopStressFactors() {
        List<Estudiante> todosEstudiantes = estudianteRepository.findAll();
        long total = todosEstudiantes.size();
        if (total == 0) return List.of();

        // 🚨 UMBRAL DE RIESGO: Un score de 8 o más en el factor se considera riesgo.
        final int UMBRAL_RIESGO = 8; 

        // 1. Contadores basados en el umbral
        long countCargaAcademica = todosEstudiantes.stream()
            .filter(e -> Optional.ofNullable(e.getStudyLoad()).orElse(0) >= UMBRAL_RIESGO)
            .count();
            
        // Se asume que en Sleep Quality, un score bajo es riesgo (ej: 0, 1, 2)
        // Por lo tanto, el riesgo es: 10 - UMBRAL_RIESGO (e.g., 10 - 8 = 2 o menos)
        long countProblemasSueno = todosEstudiantes.stream()
            .filter(e -> Optional.ofNullable(e.getSleepQuality()).orElse(0) <= (10 - UMBRAL_RIESGO)) 
            .count();
            
        long countPreocupacionCarrera = todosEstudiantes.stream()
            .filter(e -> Optional.ofNullable(e.getFutureCareerConcerns()).orElse(0) >= UMBRAL_RIESGO)
            .count();

        // 2. Crear DTOs con los porcentajes reales
        List<FactorDTO> factors = new ArrayList<>();
        
        factors.add(new FactorDTO("Carga Académica", (double) countCargaAcademica / total));
        factors.add(new FactorDTO("Problemas de Sueño", (double) countProblemasSueno / total));
        factors.add(new FactorDTO("Preocupación Profesional", (double) countPreocupacionCarrera / total));
        
        // 3. Calcular "Otros" (para que el gráfico circular sume 100%)
        double totalPorcentajeFactores = factors.stream().mapToDouble(FactorDTO::getPorcentaje).sum();
        double porcentajeOtros = Math.max(0, 1.0 - totalPorcentajeFactores);
        factors.add(new FactorDTO("Otros", porcentajeOtros));

        // Ordenar por porcentaje descendente (mejor visualización)
        return factors.stream()
               .sorted((f1, f2) -> Double.compare(f2.getPorcentaje(), f1.getPorcentaje()))
               .collect(Collectors.toList());
    }
}