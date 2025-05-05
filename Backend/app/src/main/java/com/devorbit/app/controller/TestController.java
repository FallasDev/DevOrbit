package com.devorbit.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devorbit.app.entity.Answer;
import com.devorbit.app.entity.Test;
import com.devorbit.app.service.TestService;
import com.devorbit.app.service.UserService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;

@RestController
@CrossOrigin(origins = "*") // Permitir acceso desde cualquier origen
@RequestMapping("/api/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public Test getTestById(@PathVariable int id) {
        return testService.getTestById(id);
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTest(@PathVariable int id) {
        testService.deleteTest(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Test updateTest(@PathVariable int id, @RequestBody Test test) {
        return testService.updateTest(id, test);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Test saveTest(@RequestBody Test test) {
        return testService.saveTest(test);
    }

    @PostMapping("/{testId}/getScore")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Double> getScore(@RequestBody List<Answer> userAnswers, @PathVariable int testId) {
        if (userAnswers == null || userAnswers.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            int countCorrectAnswers = testService.getCorrectAnswers(userAnswers);
            double score = testService.calculateScore(testId, countCorrectAnswers);
            return ResponseEntity.ok(score);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/course/{courseId}")
    public Test getTestsByCourse(@PathVariable int courseId) {
        return testService.getTestByCourseId(courseId);
    }

    @GetMapping("/{testId}/certificate")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<byte[]> generateCertificate(@PathVariable int testId) {
        try {
            // Crear un documento PDF
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            // Escribir contenido en el PDF
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Certificado de Finalización");
            contentStream.endText();

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("Este certificado es otorgado a: [Nombre del Usuario]");
            contentStream.endText();

            contentStream.close();

            // Guardar el PDF en un ByteArrayOutputStream
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            document.close();

            // Configurar la respuesta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("Certificado.pdf").build());

            return new ResponseEntity<>(out.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
