package com.example.demo.Service;

import com.example.demo.repository.Repository_Estudiante;
import com.example.model.Estudiante;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    private Repository_Estudiante studentRepository;

    public List<Estudiante> processCsvFile(MultipartFile file) throws IOException {
        List<Estudiante> students = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                Estudiante student = new Estudiante();
                // Mapeo de columnas del CSV a los campos de la entidad Estudiante
                student.setIdEstudiante(Long.parseLong(csvRecord.get("id_estudiante")));
                student.setNombre(csvRecord.get("nombre"));
                student.setEmail(csvRecord.get("email"));
                student.setAnxietyLevel(Integer.parseInt(csvRecord.get("anxiety_level")));
                student.setSelfEsteem(Integer.parseInt(csvRecord.get("self_esteem")));
                student.setMentalHealthHistory(Integer.parseInt(csvRecord.get("mental_health_history")));
                student.setDepression(Integer.parseInt(csvRecord.get("depression")));
                student.setHeadache(Integer.parseInt(csvRecord.get("headache")));
                student.setBloodPressure(Integer.parseInt(csvRecord.get("blood_pressure")));
                student.setSleepQuality(Integer.parseInt(csvRecord.get("sleep_quality")));
                student.setBreathingProblem(Integer.parseInt(csvRecord.get("breathing_problem")));
                student.setNoiseLevel(Integer.parseInt(csvRecord.get("noise_level")));
                student.setLivingConditions(Integer.parseInt(csvRecord.get("living_conditions")));
                student.setSafety(Integer.parseInt(csvRecord.get("safety")));
                student.setBasicNeeds(Integer.parseInt(csvRecord.get("basic_needs")));
                student.setAcademicPerformance(Integer.parseInt(csvRecord.get("academic_performance")));
                student.setStudyLoad(Integer.parseInt(csvRecord.get("study_load")));
                student.setTeacherStudentRelationship(Integer.parseInt(csvRecord.get("teacher_student_relationship")));
                student.setFutureCareerConcerns(Integer.parseInt(csvRecord.get("future_career_concerns")));
                student.setSocialSupport(Integer.parseInt(csvRecord.get("social_support")));
                student.setPeerPressure(Integer.parseInt(csvRecord.get("peer_pressure")));
                student.setExtracurricularActivities(Integer.parseInt(csvRecord.get("extracurricular_activities")));
                student.setBullying(Integer.parseInt(csvRecord.get("bullying")));
                student.setStressLevel(Integer.parseInt(csvRecord.get("stress_level")));

                students.add(student);
            }

            // Guarda todos los estudiantes en la base de datos en una sola operación
            return studentRepository.saveAll(students);
        }
    }
}