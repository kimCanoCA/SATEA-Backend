package com.example.demo.Service;

import com.example.demo.repository.Repository_Estudiante;
import com.example.model.Consejero;
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

    // 👇 CAMBIO 1: Inyecta el servicio de análisis de riesgo
    @Autowired
    private RiskAnalysisService riskAnalysisService;
   

    public List<Estudiante> processCsvFile(MultipartFile file, Consejero consejero) throws IOException {
        List<Estudiante> students = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                Estudiante student = new Estudiante();

                // Mapeo de columnas del CSV (sin cambios)
                student.setIdEstudiante(Long.parseLong(csvRecord.get("idEstudiante")));
                student.setNombre(csvRecord.get("nombre"));
                student.setEmail(csvRecord.get("email"));
                // ... (todos los demás campos del estudiante)
                student.setAnxietyLevel(Integer.parseInt(csvRecord.get("anxietyLevel")));
                student.setSelfEsteem(Integer.parseInt(csvRecord.get("selfEsteem")));
                student.setMentalHealthHistory(Integer.parseInt(csvRecord.get("mentalHealthHistory")));
                student.setDepression(Integer.parseInt(csvRecord.get("depression")));
                student.setHeadache(Integer.parseInt(csvRecord.get("headache")));
                student.setBloodPressure(Integer.parseInt(csvRecord.get("bloodPressure")));
                student.setSleepQuality(Integer.parseInt(csvRecord.get("sleepQuality")));
                student.setBreathingProblem(Integer.parseInt(csvRecord.get("breathingProblem")));
                student.setNoiseLevel(Integer.parseInt(csvRecord.get("noiseLevel")));
                student.setLivingConditions(Integer.parseInt(csvRecord.get("livingConditions")));
                student.setSafety(Integer.parseInt(csvRecord.get("safety")));
                student.setBasicNeeds(Integer.parseInt(csvRecord.get("basicNeeds")));
                student.setAcademicPerformance(Integer.parseInt(csvRecord.get("academicPerformance")));
                student.setStudyLoad(Integer.parseInt(csvRecord.get("studyLoad")));
                student.setTeacherStudentRelationship(Integer.parseInt(csvRecord.get("teacherStudentRelationship")));
                student.setFutureCareerConcerns(Integer.parseInt(csvRecord.get("futureCareerConcerns")));
                student.setSocialSupport(Integer.parseInt(csvRecord.get("socialSupport")));
                student.setPeerPressure(Integer.parseInt(csvRecord.get("peerPressure")));
                student.setExtracurricularActivities(Integer.parseInt(csvRecord.get("extracurricularActivities")));
                student.setBullying(Integer.parseInt(csvRecord.get("bullying")));
                student.setStressLevel(Integer.parseInt(csvRecord.get("stressLevel")));
                student.setCarrera(csvRecord.get("carrera"));
                student.setEdad(Integer.parseInt(csvRecord.get("edad")));
                
                // Asigna el consejero actual al estudiante
                student.setConsejero(consejero);

                // 👇 CAMBIO 2: Llama al nuevo método para calcular y asignar el nivel de riesgo
                String riskLevel = riskAnalysisService.calculateRiskLevel(student);
                student.setNivelRiesgo(riskLevel);

                students.add(student);
            }

            // Guarda todos los estudiantes en la base de datos
            return studentRepository.saveAll(students);
        }
    }
}