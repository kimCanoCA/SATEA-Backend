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
import com.example.demo.Datos.FactorDTO;
import com.example.demo.Datos.Recommendation;
import com.example.demo.Datos.RiskSegment;
import com.example.demo.Datos.RiskSummary; // IMPORTACIÓN NECESARIA para el nuevo endpoint
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
    public ResponseEntity<?> uploadStudents(@RequestParam("file") MultipartFile file, Authentication authentication) { // <-- AÑADIMOS Authentication
        if (file.isEmpty()) {
            return new ResponseEntity<>("Por favor, seleccione un archivo CSV.", HttpStatus.BAD_REQUEST);
        }

        try {
            // 1. Obtenemos el email del consejero que sube el archivo
            String userEmail = authentication.getName();

            // 2. Buscamos la entidad completa del Consejero
            Consejero consejeroLogueado = consejeroRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("Consejero no encontrado para el email: " + userEmail));

            // 3. Pasamos el archivo Y el consejero al servicio
            List<Estudiante> savedStudents = csvService.processCsvFile(file, consejeroLogueado);
            
            // Creamos un cuerpo de respuesta JSON más amigable para Angular
            String successMessage = "Se cargaron y guardaron " + savedStudents.size() + " estudiantes.";
            return ResponseEntity.status(HttpStatus.CREATED).body(new java.util.HashMap<String, String>() {{
                put("message", successMessage);
            }});

        } catch (Exception e) {
            // Devolvemos el mensaje de error en formato JSON
            String errorMessage = "Error al procesar el archivo CSV: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new java.util.HashMap<String, String>() {{
                put("message", errorMessage);
            }});
        }
    }
    // ======================================================================
    // 2. CRUD DE ESTUDIANTES (Nuevos Endpoints RIMP3.1)
    // ======================================================================

    // GET /api/v1/students: Obtener la lista de estudiantes.
    @GetMapping("/students")
    public ResponseEntity<List<Estudiante>> getAllStudentsByConsejero(Authentication authentication) {
        // 1. Obtiene el email del consejero logueado
        String userEmail = authentication.getName();

        // 2. Busca al consejero en la BD
        Consejero consejeroLogueado = consejeroRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Consejero no encontrado"));

        // 3. Usa el nuevo método del repositorio para buscar solo SUS estudiantes
        // (Asegúrate de que tu DataService tenga un método que llame al repositorio)
        List<Estudiante> estudiantes = dataService.getEstudiantesByConsejero(consejeroLogueado);
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
    public ResponseEntity<Estudiante> updateStudent(@PathVariable Long id, @RequestBody Estudiante estudianteDetails, Authentication authentication) {
        // 1. Obtiene el consejero logueado
        String userEmail = authentication.getName();
        Consejero consejeroLogueado = consejeroRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Consejero no encontrado"));

        // 2. Busca al estudiante que se quiere actualizar
        return dataService.getEstudianteById(id)
            .map(estudiante -> {
                // 3. VERIFICACIÓN CRÍTICA: ¿El consejero del estudiante es el mismo que el logueado?
                if (!estudiante.getConsejero().getId_consejero().equals(consejeroLogueado.getId_consejero())) {
                    // Si no es su estudiante, no tiene permiso.
                    return new ResponseEntity<Estudiante>(HttpStatus.FORBIDDEN); // 403 Prohibido
                }

                // 4. Si es su estudiante, procede a actualizar
                return dataService.updateEstudiante(id, estudianteDetails)
                        .map(ResponseEntity::ok)
                        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE /api/v1/students/{id}: Eliminar un estudiante.
    @DeleteMapping("/students/{id}")
    public ResponseEntity<HttpStatus> deleteStudent(@PathVariable Long id, Authentication authentication) {
        // 1. Obtiene el consejero logueado
        String userEmail = authentication.getName();
        Consejero consejeroLogueado = consejeroRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Consejero no encontrado"));

        // 2. Busca al estudiante que se quiere eliminar
        return dataService.getEstudianteById(id)
            .map(estudiante -> {
                // 3. VERIFICACIÓN CRÍTICA: ¿Pertenece a este consejero?
                if (!estudiante.getConsejero().getId_consejero().equals(consejeroLogueado.getId_consejero())) {
                    return new ResponseEntity<HttpStatus>(HttpStatus.FORBIDDEN); // 403 Prohibido
                }
                // 4. Si pertenece, procede a eliminar
                dataService.deleteEstudiante(id);
                return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
        List<CorrelationResult> correlations = riskAnalysisService.getCorrelations();
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
    
    @GetMapping("/risk/factors")
    public ResponseEntity<List<FactorDTO>> getStressFactors() {
        List<FactorDTO> factors = dataService.getTopStressFactors();
        return ResponseEntity.ok(factors);
    }
}