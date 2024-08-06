package com.proteccion.tecnica.controller;

import com.proteccion.tecnica.model.FibonacciRequest;
import com.proteccion.tecnica.model.FibonacciSequence;
import com.proteccion.tecnica.repository.FibonacciRepository;
import com.proteccion.tecnica.service.EmailService;
import com.proteccion.tecnica.service.FibonacciService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/fibonacci")
@Validated
public class FibonacciController {

    @Autowired
    private FibonacciService fibonacciService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FibonacciRepository fibonacciRepository;

    @PostMapping("/generate")
    public ResponseEntity<String> generateFibonacci(@Valid @RequestBody FibonacciRequest request) {

        String time = request.getTime();
        String email = request.getEmail();

        String[] timeParts = time.split(":");
        int seedX = Character.getNumericValue(timeParts[1].charAt(0));
        int seedY = Character.getNumericValue(timeParts[1].charAt(1));
        int count = Integer.parseInt(timeParts[2]);

        List<Integer> fibonacciSeries = fibonacciService.generateFibonacciSeries(seedX, seedY, count);

        FibonacciSequence sequence = new FibonacciSequence(LocalTime.parse(time), fibonacciSeries.toString());
        fibonacciRepository.save(sequence);

        String subject = "Prueba Técnica - Sebastián Gutiérrez Jaramillo";
        String content = "Hora de ejecución: " + time + "\nSerie Fibonacci: " + fibonacciSeries;

        emailService.sendSimpleMessage(email, subject, content);

        return ResponseEntity.ok("Fibonacci series generated and sent to email\nHora ingresada: " + time + "\nSerie Fibonacci: " + fibonacciSeries.toString());
    }

    @GetMapping("/sequences")
    public ResponseEntity<List<FibonacciSequence>> getAllSequences() {
        return ResponseEntity.ok(fibonacciRepository.findAll());
    }
}
