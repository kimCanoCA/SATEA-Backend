package com.example.demo.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.example.model.Estudiante;
import com.example.demo.repository.Repository_Estudiante;
import java.util.stream.Collectors;

@Service
public class DataService {

    private final Repository_Estudiante estudianteRepository;

    public DataService(Repository_Estudiante estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    // -------------------------------------------------------------
    // RIMP3.3: Procesa archivo CSV (Añadida Lógica de Riesgo)
    // -------------------------------------------------------------
    @Transactional
    public int processAndSaveStudents(MultipartFile file) throws IOException {
        List<Estudiante> estudiantes = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            reader.readLine(); // Descarta la cabecera

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                
                // Se espera un total de 25 campos (id, nombre, email, carrera + 21 scores)
                if (values.length >= 26) { 
                    try {
                        Estudiante estudiante = new Estudiante();
                        // 1. id_estudiante (Long)
                        estudiante.setIdEstudiante(Long.parseLong(values[0].trim()));
                        // 2. nombre (String)
                        estudiante.setNombre(values[1].trim());
                        // 3. email (String)
                        estudiante.setEmail(values[2].trim());
                        
                        // 🚨 CAMPO EDAD INSERTADO (Índice 3)
                        try {
                            estudiante.setEdad(Integer.parseInt(values[3].trim())); 
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            // Manejo de error local para Edad. Asigna null si falla.
                            estudiante.setEdad(null); 
                        }
                        
                        // 4. carrera (String) - ÍNDICE CORREGIDO a 4
                        estudiante.setCarrera(values[4].trim());
                        
                        // 5. a 25. Campos Integer (anxiety_level a stress_level) - Índices 5 a 25
                        // NOTA: Estos campos siguen usando Integer.parseInt directo, 
                        // lo que puede causar fallos si el CSV contiene valores no numéricos.
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
                        estudiante.setStressLevel(Integer.parseInt(values[25].trim())); // Último índice (25)


                        // 🚨 Calcular riesgo para el estudiante importado
                        estudiante.setNivelRiesgo(calcularNivelRiesgo(estudiante));
                        
                        estudiantes.add(estudiante);

                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico o ID no válido en la línea: " + line);
                    }
                } else {
                    System.err.println("Error de longitud de línea (esperado 25 campos o más): " + line);
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

    // -------------------------------------------------------------
    // 3. Crear (o Guardar) un estudiante (Añadida Lógica de Riesgo)
    // -------------------------------------------------------------
    public Estudiante saveEstudiante(Estudiante estudiante) {
        // 🚨 PASO CLAVE: Calcular el riesgo ANTES de llamar al repositorio
        String riesgoCalculado = calcularNivelRiesgo(estudiante);
        estudiante.setNivelRiesgo(riesgoCalculado);
        
        return estudianteRepository.save(estudiante);
    }

    // -------------------------------------------------------------
    // 4. Actualizar un estudiante (Añadida Lógica de Riesgo y Edad)
    // -------------------------------------------------------------
    @Transactional 
    public Optional<Estudiante> updateEstudiante(Long id, Estudiante estudianteDetails) {
        return estudianteRepository.findById(id).map(estudianteExistente -> {
            // Actualizar campos básicos
            estudianteExistente.setNombre(estudianteDetails.getNombre());
            estudianteExistente.setEmail(estudianteDetails.getEmail());
            estudianteExistente.setCarrera(estudianteDetails.getCarrera());
            estudianteExistente.setEdad(estudianteDetails.getEdad()); // 🚨 Campo Edad

            // Actualizar todos los scores
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

            // 🚨 Volver a calcular el riesgo con los datos actualizados
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

    // -------------------------------------------------------------
    // 6. Método: Lógica de Cálculo de Riesgo
    // -------------------------------------------------------------
    private String calcularNivelRiesgo(Estudiante estudiante) {
        // Lógica de cálculo: Suma de indicadores clave (AJÚSTALA a tus reglas)
        int sumScores = 0;
        
        // Sumamos los scores de Ansiedad, Depresión y Estrés.
        sumScores += Optional.ofNullable(estudiante.getAnxietyLevel()).orElse(0);
        sumScores += Optional.ofNullable(estudiante.getDepression()).orElse(0);
        sumScores += Optional.ofNullable(estudiante.getStressLevel()).orElse(0);
        
        // Asumiendo que el score máximo total es 30 (3 indicadores * 10 puntos)
        if (sumScores >= 25) {
            return "ALTO";
        }
        if (sumScores >= 15) {
            return "MODERADO";
        }
        return "BAJO";
    }
}