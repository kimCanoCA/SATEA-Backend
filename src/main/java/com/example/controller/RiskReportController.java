package com.example.controller;

import com.example.demo.Datos.FactorDTO;
import com.example.demo.Datos.RiskSummaryDTO;
import com.example.demo.Service.DataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports") // 👈 Ruta base correcta
@CrossOrigin(origins = "*") // 👈 Esto permite que Angular (localhost:4200) acceda sin bloqueos
public class RiskReportController {

    private final DataService dataService;

    public RiskReportController(DataService dataService) {
        this.dataService = dataService;
    }

    // 📊 Nivel de riesgo (gráfico de barras)
    @GetMapping("/risk-summary")
    public List<RiskSummaryDTO> getRiskSummary() {
        long total = dataService.getAllEstudiantes().size();
        if (total == 0) return List.of();

        List<String> niveles = Arrays.asList("BAJO", "MODERADO", "ALTO");

        return niveles.stream().map(nivel -> {
            int count = dataService.countStudentsByRiskLevel(nivel);
            double percentage = (double) count / total;
            RiskSummaryDTO dto = new RiskSummaryDTO();
            dto.setNivel(nivel);
            dto.setCount(count);
            dto.setPercentage(percentage);
            return dto;
        }).collect(Collectors.toList());
    }

    // 🥧 Factores de estrés (gráfico circular)
    @GetMapping("/stress-factors")
    public List<FactorDTO> getStressFactors() {
        return dataService.getTopStressFactors();
    }
}
