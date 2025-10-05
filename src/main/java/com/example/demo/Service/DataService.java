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

@Service
public class DataService {

    private final Repository_Estudiante estudianteRepository;

    public DataService(Repository_Estudiante estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    /**
     * RIMP3.3: Procesa un archivo CSV y guarda los registros de Estudiante.
     */
    @Transactional
    public int processAndSaveStudents(MultipartFile file) throws IOException {
        List<Estudiante> estudiantes = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            reader.readLine(); // Descarta la cabecera

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                
                // Se espera un total de 25 campos (incluyendo el nuevo campo 'carrera')
                if (values.length >= 25) { 
                    try {
                        Estudiante estudiante = new Estudiante();
                        // 1. id_estudiante (Long)
                        estudiante.setIdEstudiante(Long.parseLong(values[0].trim()));
                        // 2. nombre (String)
                        estudiante.setNombre(values[1].trim());
                        // 3. email (String)
                        estudiante.setEmail(values[2].trim());
                        
                        // 4. carrera (String) - NUEVO CAMPO en el índice 3
                        estudiante.setCarrera(values[3].trim());

                        // 5. a 25. Campos Integer (anxiety_level a stress_level) - Índices 4 a 24
                        estudiante.setAnxietyLevel(Integer.parseInt(values[4].trim()));
                        estudiante.setSelfEsteem(Integer.parseInt(values[5].trim()));
                        estudiante.setMentalHealthHistory(Integer.parseInt(values[6].trim()));
                        estudiante.setDepression(Integer.parseInt(values[7].trim()));
                        estudiante.setHeadache(Integer.parseInt(values[8].trim()));
                        estudiante.setBloodPressure(Integer.parseInt(values[9].trim()));
                        estudiante.setSleepQuality(Integer.parseInt(values[10].trim()));
                        estudiante.setBreathingProblem(Integer.parseInt(values[11].trim()));
                        estudiante.setNoiseLevel(Integer.parseInt(values[12].trim()));
                        estudiante.setLivingConditions(Integer.parseInt(values[13].trim()));
                        estudiante.setSafety(Integer.parseInt(values[14].trim()));
                        estudiante.setBasicNeeds(Integer.parseInt(values[15].trim()));
                        estudiante.setAcademicPerformance(Integer.parseInt(values[16].trim()));
                        estudiante.setStudyLoad(Integer.parseInt(values[17].trim()));
                        estudiante.setTeacherStudentRelationship(Integer.parseInt(values[18].trim()));
                        estudiante.setFutureCareerConcerns(Integer.parseInt(values[19].trim()));
                        estudiante.setSocialSupport(Integer.parseInt(values[20].trim()));
                        estudiante.setPeerPressure(Integer.parseInt(values[21].trim()));
                        estudiante.setExtracurricularActivities(Integer.parseInt(values[22].trim()));
                        estudiante.setBullying(Integer.parseInt(values[23].trim()));
                        estudiante.setStressLevel(Integer.parseInt(values[24].trim())); // Último índice (24)

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

    // 3. Crear (o Guardar) un estudiante
    public Estudiante saveEstudiante(Estudiante estudiante) {
        // Aquí podrías añadir lógica de validación antes de guardar
        return estudianteRepository.save(estudiante);
    }

    // 4. Actualizar un estudiante
    @Transactional // Recomendado para operaciones de actualización
    public Optional<Estudiante> updateEstudiante(Long id, Estudiante estudianteDetails) {
        return estudianteRepository.findById(id).map(estudianteExistente -> {
            // Aquí debes copiar los campos que quieres actualizar
            estudianteExistente.setNombre(estudianteDetails.getNombre());
            estudianteExistente.setEmail(estudianteDetails.getEmail());
            // Se añade la actualización para la carrera
            estudianteExistente.setCarrera(estudianteDetails.getCarrera()); 
            estudianteExistente.setAnxietyLevel(estudianteDetails.getAnxietyLevel());
            
            // Si es necesario, añade aquí todos los demás setters que se requieran actualizar
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
}
