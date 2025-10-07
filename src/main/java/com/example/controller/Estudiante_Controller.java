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
import com.example.demo.Service.DataService;
import com.example.demo.Service.RiskAnalysisService;
import com.example.model.Estudiante;
import com.example.demo.Service.CSVService;
import com.example.demo.repository.Repository_Consejero; 
import com.example.model.Consejero;
import org.springframework.security.core.Authentication; 
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class Estudiante_Controller {

    private final DataService dataService;
    private final RiskAnalysisService riskAnalysisService;
    private final CSVService csvService;
    
    @Autowired // <-- Usa Autowired para inyectar el repositorio
    private Repository_Consejero consejeroRepository;

    // Inyección de dependencias por constructor
    public Estudiante_Controller(DataService dataService, RiskAnalysisService riskAnalysisService,CSVService csvService) {
        this.dataService = dataService;
        this.riskAnalysisService = riskAnalysisService;
        this.csvService = csvService;
    }

    // ======================================================================
    // RIMP3.3: Endpoint de Carga de Archivos
    // ======================================================================
    @PostMapping("/students/upload")
    public ResponseEntity<?> uploadStudents(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return new ResponseEntity<>("Por favor, seleccione un archivo CSV.", HttpStatus.BAD_REQUEST);
        }

        try {
            List<Estudiante> savedStudents = csvService.processCsvFile(file);
            return new ResponseEntity<>("Se cargaron y guardaron " + savedStudents.size() + " estudiantes.", HttpStatus.CREATED);
        } catch (Exception e) {
            // Esto capturará errores de formato en el CSV (ej. texto donde debería ir un número)
            return new ResponseEntity<>("Error al procesar el archivo CSV: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<Estudiante> createStudent(@RequestBody Estudiante estudiante, Authentication authentication) {

        // 1. Obtiene el email (username) del consejero que ha iniciado sesión desde el token JWT.
        String userEmail = authentication.getName();

        // 2. Busca la entidad completa del Consejero en la base de datos usando su email.
        Consejero consejeroLogueado = consejeroRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Consejero no encontrado para el email: " + userEmail));

        // 3. Asigna este consejero al nuevo estudiante que se está creando.
        estudiante.setConsejero(consejeroLogueado);

        // 4. Guarda el estudiante en la base de datos (ahora con el id_consejero).
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
    
    // RIMP3.1 & RF2.2: Obtener la segmentación de riesgo
    @GetMapping("/risk/segmentation")
    public ResponseEntity<List<RiskSegment>> getRiskSegmentation() {
        List<RiskSegment> segmentation = riskAnalysisService.getRiskSegmentation();
        return ResponseEntity.ok(segmentation);
    }
    
    // RIMP3.1 & RF1.2: Obtener el análisis de correlación
    @GetMapping("/risk/correlations")
    public ResponseEntity<List<CorrelationResult>> getCorrelations() {
        List<CorrelationResult> correlations = riskAnalysisService.getCorrelations();
        return ResponseEntity.ok(correlations);
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
}