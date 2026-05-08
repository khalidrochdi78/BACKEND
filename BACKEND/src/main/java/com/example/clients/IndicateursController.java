package com.example.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/indicateurs")
public class IndicateursController {

    @Autowired
    private IndicateursService indicateursService;

    /**
     * GET /api/indicateurs/i3?debut=2025-01-01&fin=2025-12-31&moyenneCA=500000
     */
    @GetMapping("/i3")
    public ResponseEntity<?> getI3(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam double moyenneCA) {
        try {
            List<Map<String, Object>> data = indicateursService.calculerI3(debut, fin, moyenneCA);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erreur", e.getMessage()));
        }
    }

    /**
     * GET /api/indicateurs/i5?debut=2025-01-01&fin=2025-12-31&moyenneND=120
     */
    @GetMapping("/i5")
    public ResponseEntity<?> getI5(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            @RequestParam double moyenneND) {
        try {
            List<Map<String, Object>> data = indicateursService.calculerI5(debut, fin, moyenneND);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erreur", e.getMessage()));
        }
    }

    /**
     * GET /api/indicateurs/i6?debut=2025-01-01&fin=2025-12-31
     */
    @GetMapping("/i6")
    public ResponseEntity<?> getI6(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        try {
            List<Map<String, Object>> data = indicateursService.calculerI6(debut, fin);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erreur", e.getMessage()));
        }
    }

    /**
     * GET /api/indicateurs/i15?debut=2025-01-01&fin=2025-03-31
     */
    @GetMapping("/i15")
    public ResponseEntity<?> getI15(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        try {
            Map<String, Object> data = indicateursService.calculerI15(debut, fin);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erreur", e.getMessage()));
        }
    }

    /**
     * GET /api/indicateurs/taux-retour?debut=2025-01-01&fin=2025-03-31
     */
    @GetMapping("/taux-retour")
    public ResponseEntity<?> getTauxRetour(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        try {
            Map<String, Object> data = indicateursService.calculerTauxRetour(debut, fin);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("erreur", e.getMessage()));
        }
    }
}
