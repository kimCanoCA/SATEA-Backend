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
                
                if (values.length >= 24) { 
                    try {
                        Estudiante estudiante = new Estudiante();
                        // 1. id_estudiante (Long)
                        estudiante.setIdEstudiante(Long.parseLong(values[0].trim()));
                        // 2. nombre (String)
                        estudiante.setNombre(values[1].trim());
                        // 3. email (String)
                        estudiante.setEmail(values[2].trim());
                        
                        // 4. a 24. Campos Integer (anxiety_level a stress_level)
                        estudiante.setAnxietyLevel(Integer.parseInt(values[3].trim()));
                        estudiante.setSelfEsteem(Integer.parseInt(values[4].trim()));
                        estudiante.setMentalHealthHistory(Integer.parseInt(values[5].trim()));
                        estudiante.setDepression(Integer.parseInt(values[6].trim()));
                        estudiante.setHeadache(Integer.parseInt(values[7].trim()));
                        estudiante.setBloodPressure(Integer.parseInt(values[8].trim()));
                        estudiante.setSleepQuality(Integer.parseInt(values[9].trim()));
                        estudiante.setBreathingProblem(Integer.parseInt(values[10].trim()));
                        estudiante.setNoiseLevel(Integer.parseInt(values[11].trim()));
                        estudiante.setLivingConditions(Integer.parseInt(values[12].trim()));
                        estudiante.setSafety(Integer.parseInt(values[13].trim()));
                        estudiante.setBasicNeeds(Integer.parseInt(values[14].trim()));
                        estudiante.setAcademicPerformance(Integer.parseInt(values[15].trim()));
                        estudiante.setStudyLoad(Integer.parseInt(values[16].trim()));
                        estudiante.setTeacherStudentRelationship(Integer.parseInt(values[17].trim()));
                        estudiante.setFutureCareerConcerns(Integer.parseInt(values[18].trim()));
                        estudiante.setSocialSupport(Integer.parseInt(values[19].trim()));
                        estudiante.setPeerPressure(Integer.parseInt(values[20].trim()));
                        estudiante.setExtracurricularActivities(Integer.parseInt(values[21].trim()));
                        estudiante.setBullying(Integer.parseInt(values[22].trim()));
                        estudiante.setStressLevel(Integer.parseInt(values[23].trim()));

                        estudiantes.add(estudiante);

                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato numérico en la línea: " + line);
                    }
                }
            }
        }

        estudianteRepository.saveAll(estudiantes);
        return estudiantes.size();
    }

    public Optional<Estudiante> getEstudianteById(Long id) {
        return estudianteRepository.findById(id);
    }
    
    public List<Estudiante> getAllEstudiantes() {
        return estudianteRepository.findAll();
    }
}