package com.example.clients;


import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ExcelService {

    // Ligne d'en-tête dans le fichier (index 0 = row 1)
    private static final int HEADER_ROW_INDEX = 6; // Row 7 dans Excel = index 6
    private static final int DATA_START_ROW = 7;   // Row 8 = début données
    private static final DateTimeFormatter[] DATE_FORMATS = new DateTimeFormatter[] {
        DateTimeFormatter.ofPattern("d/M/uuuu"),
        DateTimeFormatter.ofPattern("dd/MM/uuuu"),
        DateTimeFormatter.ofPattern("uuuu-MM-dd")
    };

    /**
     * Lit tous les clients depuis un fichier Excel
     */
    public List<Client> lireClients(String cheminFichier) throws Exception {
        List<Client> clients = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(cheminFichier));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> colonnes = construireMapColonnes(sheet.getRow(HEADER_ROW_INDEX));

            int idxNr = trouverColonne(colonnes, 0, "nr", "n", "numero", "n°");
            int idxNomClient = trouverColonne(colonnes, 1, "nom client", "noms du client", "client");
            int idxSuiviPar = trouverColonne(colonnes, 2, "suivi par");
            int idxNumeroDevis = trouverColonne(colonnes, 3, "n devis", "numero devis");
            int idxDateDevis = trouverColonne(colonnes, 4, "date devis", "dt devis");
            int idxSomme = trouverColonne(colonnes, 5, "somme", "somme dh", "montant");
            int idxSommeEncaissee = trouverColonne(colonnes, 6, "somme encaissee", "somme encaissee dh", "encaissee");
            int idxNumeroBL = trouverColonne(colonnes, 7, "n bl", "numero bl", "bl");
            int idxDateBL = trouverColonne(colonnes, 8, "date bl", "dt bl");
            int idxNumeroFacture = trouverColonne(colonnes, 9, "n facture", "n fact", "numero facture");
            int idxDateFacture = trouverColonne(colonnes, 10, "date facture", "dt fact");
            int idxModePaiement = trouverColonne(colonnes, 11, "mode paiement", "mode de paiement", "paiement", "md pay");
            int idxSituation = trouverColonne(colonnes, 12, "situation", "etat");
            int idxEnquete = trouverColonne(colonnes, 13, "enquete", "enquete satisfaction", "satisfaction");
            int idxDateDemande = trouverColonne(colonnes, 14, "date demande", "date de demande", "date demande service", "demande service");

            for (int i = DATA_START_ROW; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Ignorer les lignes vides ou de totaux
                Cell premiereCellule = row.getCell(idxNr);
                if (premiereCellule == null) continue;
                if (premiereCellule.getCellType() != CellType.NUMERIC) continue;

                Client client = new Client();

                client.setNr((int) premiereCellule.getNumericCellValue());
                client.setNomClient(lireString(row.getCell(idxNomClient)));
                client.setSuiviPar(lireString(row.getCell(idxSuiviPar)));
                client.setNumeroDevis(lireString(row.getCell(idxNumeroDevis)));
                client.setDateDevis(lireDate(row.getCell(idxDateDevis)));
                client.setSomme(lireDouble(row.getCell(idxSomme)));
                client.setSommeEncaissee(lireDouble(row.getCell(idxSommeEncaissee)));
                client.setNumeroBL(lireString(row.getCell(idxNumeroBL)));
                client.setDateBL(lireDate(row.getCell(idxDateBL)));
                client.setNumeroFacture(lireString(row.getCell(idxNumeroFacture)));
                client.setDateFacture(lireDate(row.getCell(idxDateFacture)));
                client.setModePaiement(normaliserModePaiement(lireString(row.getCell(idxModePaiement))));
                client.setSituation(normaliserSituation(lireString(row.getCell(idxSituation))));
                client.setEnqueteSatisfaction(normaliserEnquete(lireEnquete(row.getCell(idxEnquete))));
                client.setDateDemandeService(lireDate(row.getCell(idxDateDemande)));

                clients.add(client);
            }
        }
        return clients;
    }

    /**
     * Lit les fichiers Excel de tous les dossiers annuels
     * Exemple de structure : /data/2023/INFORMATION_CLIENT_2023.xlsx
     *                         /data/2024/INFORMATION_CLIENT_2024.xlsx
     */
    public List<Client> lireTousLesClients(String dossierRacine) throws Exception {
        List<Client> tousClients = new ArrayList<>();
        File dossier = new File(dossierRacine);

        if (!dossier.exists() || !dossier.isDirectory()) {
            throw new Exception("Dossier introuvable : " + dossierRacine);
        }

        File[] sousDossiers = dossier.listFiles(File::isDirectory);
        if (sousDossiers == null) return tousClients;

        for (File sousDossier : sousDossiers) {
            String annee = sousDossier.getName();
            File[] fichiers = sousDossier.listFiles(
                f -> f.getName().endsWith(".xlsx") && f.getName().contains("INFORMATION_CLIENT")
            );
            if (fichiers == null) continue;

            for (File fichier : fichiers) {
                List<Client> clientsAnnee = lireClients(fichier.getAbsolutePath());
                for (Client client : clientsAnnee) {
                    client.setAnnee(annee);
                }
                tousClients.addAll(clientsAnnee);
            }
        }
        return tousClients;
    }

    // ── Méthodes utilitaires ──────────────────────────────────────

    private Map<String, Integer> construireMapColonnes(Row headerRow) {
        Map<String, Integer> colonnes = new HashMap<>();
        if (headerRow == null) return colonnes;

        for (Cell cell : headerRow) {
            String nom = normalize(lireString(cell));
            if (nom != null && !nom.isBlank()) {
                colonnes.put(nom, cell.getColumnIndex());
            }
        }
        return colonnes;
    }

    private int trouverColonne(Map<String, Integer> colonnes, int fallback, String... alias) {
        for (String a : alias) {
            Integer idx = colonnes.get(normalize(a));
            if (idx != null) return idx;
        }
        return fallback;
    }

    private String normalize(String value) {
        if (value == null) return null;
        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT)
            .replace('°', ' ')
            .replaceAll("[^a-z0-9 ]", " ")
            .replaceAll("\\s+", " ")
            .trim();
        return normalized.isEmpty() ? null : normalized;
    }

    private String lireString(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> corrigerEncodage(cell.getStringCellValue().trim());
            case NUMERIC -> {
                double value = cell.getNumericCellValue();
                if (DateUtil.isCellDateFormatted(cell)) {
                    LocalDate date = toLocalDate(value);
                    yield date != null ? date.toString() : null;
                }
                yield BigDecimal.valueOf(value).stripTrailingZeros().toPlainString();
            }
            default -> null;
        };
    }

    private Double lireDouble(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC)
            return cell.getNumericCellValue();
        return null;
    }

    private LocalDate lireDate(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            double value = cell.getNumericCellValue();
            if (DateUtil.isCellDateFormatted(cell) || isPossibleExcelDate(value)) {
                return toLocalDate(value);
            }
        }

        if (cell.getCellType() == CellType.STRING) {
            String raw = cell.getStringCellValue();
            if (raw == null) return null;
            String value = raw.trim();
            if (value.isEmpty()) return null;

            for (DateTimeFormatter formatter : DATE_FORMATS) {
                try {
                    return LocalDate.parse(value, formatter);
                } catch (DateTimeParseException ignored) {
                }
            }
            return null;
        }

        return null;
    }

    private boolean isPossibleExcelDate(double value) {
        return value >= 20000 && value <= 80000;
    }

    private LocalDate toLocalDate(double excelValue) {
        Date date = DateUtil.getJavaDate(excelValue);
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private String lireEnquete(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case NUMERIC -> "OUI"; // valeur 1 = a rempli
            case STRING -> corrigerEncodage(cell.getStringCellValue().trim()).toUpperCase(); // "NON"
            default -> null;
        };
    }

    private String corrigerEncodage(String value) {
        if (value == null) return null;
        String corrige = value;
        if (corrige.contains("Ã") || corrige.contains("Â")) {
            corrige = new String(corrige.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            corrige = corrige
                .replace("Ã©", "é")
                .replace("Ã¨", "è")
                .replace("Ãª", "ê")
                .replace("Ã", "é")
                .replace("Â", "");
        }
        return corrige;
    }

    private String normaliserSituation(String value) {
        if (value == null || value.isBlank()) return null;
        String brut = corrigerEncodage(value).trim();
        String cle = normalize(brut);

        if (cle == null) return null;
        if (cle.contains("encaisse")) return "ENCAISSE";
        if (cle.contains("classe")) return "CLASSE";
        if (cle.contains("impaye") || cle.contains("non paye") || cle.contains("non regle")) return "IMPAYE";
        if (cle.contains("partiel") || cle.contains("partiellement")) return "PARTIEL";

        return brut.toUpperCase(Locale.ROOT);
    }

    private String normaliserModePaiement(String value) {
        if (value == null || value.isBlank()) return null;
        String brut = corrigerEncodage(value).trim();
        String cle = normalize(brut);

        if (cle == null) return null;
        if (cle.equals("vir") || cle.contains("virement")) return "VIR";
        if (cle.equals("chq") || cle.contains("cheque")) return "CHEQUE";
        if (cle.equals("esp") || cle.contains("espece")) return "ESPECE";
        if (cle.contains("traite") || cle.contains("effet") || cle.contains("letre change") || cle.contains("lettre change")) return "TRAITE";
        if (cle.contains("carte")) return "CARTE";

        return brut.toUpperCase(Locale.ROOT);
    }

    private String normaliserEnquete(String value) {
        if (value == null || value.isBlank()) return null;
        String cle = normalize(corrigerEncodage(value));
        if (cle == null) return null;

        if (cle.equals("1") || cle.equals("oui") || cle.contains("ok") || cle.contains("rempl")) return "OUI";
        if (cle.equals("0") || cle.equals("non") || cle.contains("pas")) return "NON";
        if (cle.contains("encais")) return "ENCAISSE";
        if (cle.contains("class")) return "CLASSE";

        return cle.toUpperCase(Locale.ROOT);
    }
}