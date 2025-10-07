package com.example.controller; // Asegúrate de que el paquete sea correcto

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Datos.CorrelationResult;
import com.example.demo.Datos.Recommendation;
import com.example.demo.Datos.RiskSegment;
import com.example.demo.Datos.RiskSummary; // IMPORTACIÓN NECESARIA para el nuevo endpoint
import com.example.demo.Service.DataService;
import com.example.demo.Service.RiskAnalysisService;
import com.example.model.Estudiante;

@RestController
@RequestMapping("/api/v1")
public class Estudiante_Controller {

    private final DataService dataService;
    private final RiskAnalysisService riskAnalysisService;

    // Inyección de dependencias por constructor
    public Estudiante_Controller(DataService dataService, RiskAnalysisService riskAnalysisService) {
        this.dataService = dataService;
        this.riskAnalysisService = riskAnalysisService;
    }

    // ======================================================================
    // RIMP3.3: Endpoint de Carga de Archivos
    // ======================================================================
    @PostMapping("/data/upload")
    public ResponseEntity<String> uploadDataFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Debe seleccionar un archivo para cargar.", HttpStatus.BAD_REQUEST);
        }

        try {
            // Llama al servicio para procesar y guardar los datos
            int savedCount = dataService.processAndSaveStudents(file);
            return new ResponseEntity<>("Archivo '" + file.getOriginalFilename() + 
                                        "' procesado exitosamente. Se guardaron " + savedCount + " registros de estudiantes.", 
                                        HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>("Error al leer el archivo de datos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Captura errores de NumberFormatException o base de datos
            System.err.println("Error al procesar el archivo: " + e.getMessage());
            return new ResponseEntity<>("Error al procesar el archivo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // ======================================================================
    // 2. CRUD DE ESTUDIANTES (Nuevos Endpoints RIMP3.1)
    // ======================================================================

    // GET /api/v1/students: Obtener la lista de estudiantes.
    @GetMapping("/students")
    public ResponseEntity<List<Estudiante>> getAllStudents() {
        List<Estudiante> estudiantes = dataService.getAllEstudiantes();
        return ResponseEntity.ok(estudiantes); 
    }

    // GET /api/v1/students/{id}: Obtener un estudiante específico.
    @GetMapping("/students/{id}")
    public ResponseEntity<Estudiante> getStudentById(@PathVariable Long id) {
        // Delega la lógica de búsqueda al servicio
        return dataService.getEstudianteById(id)
                          .map(ResponseEntity::ok)
                          .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND)); 
    }

    // POST /api/v1/students: Crear un nuevo estudiante.
    @PostMapping("/students")
    public ResponseEntity<Estudiante> createStudent(@RequestBody Estudiante estudiante) {
        Estudiante savedEstudiante = dataService.saveEstudiante(estudiante);
        return new ResponseEntity<>(savedEstudiante, HttpStatus.CREATED); 
    }

    // PUT /api/v1/students/{id}: Actualizar un estudiante existente.
    @PutMapping("/students/{id}")
    public ResponseEntity<Estudiante> updateStudent(@PathVariable Long id, @RequestBody Estudiante estudianteDetails) {
        // Delega la lógica de actualización al servicio
        return dataService.updateEstudiante(id, estudianteDetails)
                          .map(ResponseEntity::ok)
                          .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE /api/v1/students/{id}: Eliminar un estudiante.
    @DeleteMapping("/students/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable Long id) {
        if (dataService.deleteEstudiante(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
    // ======================================================================
    // Análisis de Riesgo (RF1, RF2, RF3)
    // ======================================================================
    
    // RIMP3.1 & RF2.1: Obtener el resumen de riesgo para el Dashboard (¡NUEVO!)
    @GetMapping("/risk/summary")
    public ResponseEntity<RiskSummary> getRiskSummary() {
        RiskSummary summary = riskAnalysisService.getRiskSummary();
        return ResponseEntity.ok(summary);
    }
    
    // RIMP3.1 & RF2.2: Obtener la segmentación de riesgo
    @GetMapping("/risk/segmentation")
    public ResponseEntity<List<RiskSegment>> getRiskSegmentation() {
        List<RiskSegment> segmentation = riskAnalysisService.getRiskSegmentation();
        return ResponseEntity.ok(segmentation);
    }
    
    // RIMP3.1 & RF3.1: Obtener recomendaciones personalizadas
    @GetMapping("/students/{id}/recommendations")
    public ResponseEntity<List<Recommendation>> getRecommendations(@PathVariable Long id) {
        List<Recommendation> recommendations = riskAnalysisService.getPersonalizedRecommendations(id);
        
        // Retorna 404 Not Found si el estudiante no existe
        if (dataService.getEstudianteById(id).isEmpty()) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return ResponseEntity.ok(recommendations);
    }
    
    // RIMP3.1 & RF1.2: Obtener el análisis de correlación (Modelo General - se mantiene)
    @GetMapping("/risk/correlations")
    public ResponseEntity<List<CorrelationResult>> getCorrelations() {
        List<CorrelationResult> correlations = riskAnalysisService.getCorrelations();
        return ResponseEntity.ok(correlations);
    }
    
    // 🚨 NUEVO ENDPOINT: Obtener las correlaciones personalizadas
    @GetMapping("/students/{id}/correlations") 
    public ResponseEntity<List<CorrelationResult>> getStudentCorrelations(@PathVariable Long id) {
        // Retorna 404 Not Found si el estudiante no existe
        if (dataService.getEstudianteById(id).isEmpty()) {
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        // Llama al nuevo método que filtra por estudiante
        List<CorrelationResult> correlations = riskAnalysisService.getRelevantCorrelations(id); 
        return ResponseEntity.ok(correlations);
    }
    
 // 🔹 RIMP3.1 & RF2.3: Obtener lista de estudiantes con nivel de riesgo "Alto"
    @GetMapping("/risk/high")
    public ResponseEntity<List<Estudiante>> getHighRiskStudents() {
        List<Estudiante> highRiskStudents = dataService.getAllEstudiantes()
            .stream()
            .filter(est -> "Alto".equalsIgnoreCase(est.getNivelRiesgo()))
            .toList();

        return ResponseEntity.ok(highRiskStudents);
    }
}