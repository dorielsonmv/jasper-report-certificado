package tech.jamersondev.jasperreports.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jamersondev.jasperreports.model.Aluno;
import tech.jamersondev.jasperreports.service.JasperReportService;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/jasper-report")
public class JasperReportController {
    private final JasperReportService jasperReportService;

    public JasperReportController(JasperReportService jasperReportService) {
        this.jasperReportService = jasperReportService;
    }

    @PostMapping("/gerar-certificado")
    public void gerar(@RequestBody Aluno aluno) throws IOException {
        this.jasperReportService.gerar(aluno);
    }
}
