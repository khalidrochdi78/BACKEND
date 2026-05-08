package com.example.clients;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ExcelService excelService;
    @Autowired
    private PdfService pdfService;
   
    @Value("${app.dossier.racine:.}")
    private String dossierRacine;

    /**
     * GET /api/clients → retourne tous les clients
     */
    @GetMapping
public List<Client> getTousClients() throws Exception {
    return excelService.lireTousLesClients(dossierRacine);
    }
    /**
     * GET /api/clients/annee/{annee} → clients d'une année
     */
    @GetMapping("/annee/{annee}")
    public ResponseEntity<List<Client>> getClientsParAnnee(@PathVariable String annee) {
        try {
            String fichier = dossierRacine + "/" + annee 
                           + "/INFORMATION_CLIENT_" + annee + ".xlsx";
            List<Client> clients = excelService.lireClients(fichier);
            return ResponseEntity.ok(clients);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
  @GetMapping("/{nr}/pdf")
public ResponseEntity<byte[]> downloadPdf(@PathVariable Integer nr) {
    try {
        List<Client> clients = excelService.lireTousLesClients(dossierRacine);

        Client client = clients.stream()
                .filter(c -> c.getNr().equals(nr))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Client introuvable : " + nr
                ));

        byte[] pdf = pdfService.generateClientPdf(client);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=fiche_client_" + nr + ".pdf");  // ✅ fix

        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

    } catch (ResponseStatusException e) {
        throw e;
    } catch (Exception e) {
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "Erreur génération PDF : " + e.getMessage()
        );
    }
}
}
