
package com.example.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndicateursService {

    @Autowired
    private ExcelService excelService;

    @Value("${app.dossier.racine:.}")
    private String dossierRacine;

    // ─────────────────────────────────────────────────────────────────────────
    // Jours fériés Maroc (fixes) — tu peux en ajouter
    // ─────────────────────────────────────────────────────────────────────────
    private static final Set<String> JOURS_FERIES = Set.of(
        "01-01", // Nouvel An
        "01-05", // Fête du Travail
        "30-07", // Fête du Trône
        "14-08", // Allégeance Oued Eddahab
        "20-08", // Révolution du Roi
        "21-08", // Fête de la Jeunesse
        "06-11", // Marche Verte
        "18-11"  // Fête de l'Indépendance
    );

    private boolean isJourOuvre(LocalDate date) {
        DayOfWeek jour = date.getDayOfWeek();
        if (jour == DayOfWeek.SATURDAY || jour == DayOfWeek.SUNDAY) return false;
        String mmdd = String.format("%02d-%02d", date.getMonthValue(), date.getDayOfMonth());
        return !JOURS_FERIES.contains(mmdd);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Charger tous les clients dans la période (moisDebut → moisFin)
    // On filtre par dateFacture (= date d'encaissement)
    // ─────────────────────────────────────────────────────────────────────────
    private List<Client> clientsDansPeriode(LocalDate debut, LocalDate fin) throws Exception {
        List<Client> tous = excelService.lireTousLesClients(dossierRacine);
        return tous.stream()
            .filter(c -> c.getDateFacture() != null
                      && !c.getDateFacture().isBefore(debut)
                      && !c.getDateFacture().isAfter(fin))
            .collect(Collectors.toList());
    }

    // Clients encaissés = sommeEncaissee > 0
    private boolean estEncaisse(Client c) {
        return c.getSommeEncaissee() != null && c.getSommeEncaissee() > 0;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // I3 — % évolution CA mensuel
    // formule : cumul CA jusqu'au mois M ÷ moyenneCA5ans × 100
    // ─────────────────────────────────────────────────────────────────────────
    public List<Map<String, Object>> calculerI3(
            LocalDate debut, LocalDate fin, double moyenneCA5ans) throws Exception {

        List<Client> clients = clientsDansPeriode(debut, fin).stream()
                .filter(this::estEncaisse)
                .collect(Collectors.toList());

        List<Map<String, Object>> resultats = new ArrayList<>();
        double cumulCA = 0;

        YearMonth moisCourant = YearMonth.from(debut);
        YearMonth moisFin     = YearMonth.from(fin);

        while (!moisCourant.isAfter(moisFin)) {
            final YearMonth mois = moisCourant;

            double caduMois = clients.stream()
                .filter(c -> YearMonth.from(c.getDateFacture()).equals(mois))
                .mapToDouble(c -> c.getSommeEncaissee() != null ? c.getSommeEncaissee() : 0)
                .sum();

            cumulCA += caduMois;

            double i3 = moyenneCA5ans > 0 ? (cumulCA / moyenneCA5ans) * 100 : 0;

            Map<String, Object> ligne = new LinkedHashMap<>();
            ligne.put("mois",    mois.toString());         // ex: "2025-03"
            ligne.put("caMois",  Math.round(caduMois * 100.0) / 100.0);
            ligne.put("caCumul", Math.round(cumulCA  * 100.0) / 100.0);
            ligne.put("i3",      Math.round(i3       * 100.0) / 100.0);
            resultats.add(ligne);

            moisCourant = moisCourant.plusMonths(1);
        }
        return resultats;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // I5 — % évolution nombre de demandes mensuel
    // formule : cumul ND jusqu'au mois M ÷ moyenneND5ans × 100
    // ─────────────────────────────────────────────────────────────────────────
    public List<Map<String, Object>> calculerI5(
            LocalDate debut, LocalDate fin, double moyenneND5ans) throws Exception {

        List<Client> clients = clientsDansPeriode(debut, fin).stream()
                .filter(this::estEncaisse)
                .collect(Collectors.toList());

        List<Map<String, Object>> resultats = new ArrayList<>();
        long cumulND = 0;

        YearMonth moisCourant = YearMonth.from(debut);
        YearMonth moisFin     = YearMonth.from(fin);

        while (!moisCourant.isAfter(moisFin)) {
            final YearMonth mois = moisCourant;

            long ndDuMois = clients.stream()
                .filter(c -> YearMonth.from(c.getDateFacture()).equals(mois))
                .count();

            cumulND += ndDuMois;

            double i5 = moyenneND5ans > 0 ? (cumulND / moyenneND5ans) * 100 : 0;

            Map<String, Object> ligne = new LinkedHashMap<>();
            ligne.put("mois",     mois.toString());
            ligne.put("ndMois",   ndDuMois);
            ligne.put("ndCumul",  cumulND);
            ligne.put("i5",       Math.round(i5 * 100.0) / 100.0);
            resultats.add(ligne);

            moisCourant = moisCourant.plusMonths(1);
        }
        return resultats;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // I6 — délai de réactivité devis (jours ouvrés)
    // formule : dateDevis - dateDemandeService (sans weekends ni fériés)
    // ─────────────────────────────────────────────────────────────────────────
    public List<Map<String, Object>> calculerI6(
            LocalDate debut, LocalDate fin) throws Exception {

        // I6 filtre par dateDemandeService
        List<Client> tous = excelService.lireTousLesClients(dossierRacine);
        List<Client> clients = tous.stream()
            .filter(c -> c.getDateDemandeService() != null
                      && c.getDateDevis() != null
                      && !c.getDateDemandeService().isBefore(debut)
                      && !c.getDateDemandeService().isAfter(fin))
            .collect(Collectors.toList());

        List<Map<String, Object>> resultats = new ArrayList<>();

        YearMonth moisCourant = YearMonth.from(debut);
        YearMonth moisFin     = YearMonth.from(fin);

        while (!moisCourant.isAfter(moisFin)) {
            final YearMonth mois = moisCourant;

            List<Client> duMois = clients.stream()
                .filter(c -> YearMonth.from(c.getDateDemandeService()).equals(mois))
                .collect(Collectors.toList());

            double delaiMoyen = 0;
            if (!duMois.isEmpty()) {
                double totalDelai = duMois.stream()
                    .mapToDouble(c -> joursOuvres(c.getDateDemandeService(), c.getDateDevis()))
                    .sum();
                delaiMoyen = totalDelai / duMois.size();
            }

            Map<String, Object> ligne = new LinkedHashMap<>();
            ligne.put("mois",         mois.toString());
            ligne.put("nbDemandes",   duMois.size());
            ligne.put("i6MoyenJours", Math.round(delaiMoyen * 100.0) / 100.0);
            resultats.add(ligne);

            moisCourant = moisCourant.plusMonths(1);
        }
        return resultats;
    }

    // Compte les jours ouvrés entre deux dates
    private long joursOuvres(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || !fin.isAfter(debut)) return 0;
        long count = 0;
        LocalDate d = debut.plusDays(1);
        while (!d.isAfter(fin)) {
            if (isJourOuvre(d)) count++;
            d = d.plusDays(1);
        }
        return count;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // I15 — Note de satisfaction globale
    // ─────────────────────────────────────────────────────────────────────────
    public Map<String, Object> calculerI15(LocalDate debut, LocalDate fin) throws Exception {
        List<Client> clients = clientsDansPeriode(debut, fin);
        Map<String, Object> resultat = new LinkedHashMap<>();

        if (clients.isEmpty()) {
            resultat.put("message", "Pas de client dans la période");
            return resultat;
        }

        List<Double> notes = clients.stream()
            .map(Client::getEnqueteSatisfaction)
            .filter(e -> e != null && !e.equalsIgnoreCase("NON") && !e.isBlank())
            .map(e -> {
                try { return Double.parseDouble(e); }
                catch (NumberFormatException ex) { return null; }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        if (notes.isEmpty()) {
            resultat.put("message", "Pas d'enquête de satisfaction");
            return resultat;
        }

        double moyenne = notes.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        resultat.put("i15",          Math.round(moyenne * 100.0) / 100.0);
        resultat.put("nbReponses",   notes.size());
        resultat.put("nbClients",    clients.size());
        return resultat;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Taux de retour — % clients ayant rempli l'enquête
    // ─────────────────────────────────────────────────────────────────────────
    public Map<String, Object> calculerTauxRetour(LocalDate debut, LocalDate fin) throws Exception {
        List<Client> clients = clientsDansPeriode(debut, fin);
        Map<String, Object> resultat = new LinkedHashMap<>();

        if (clients.isEmpty()) {
            resultat.put("message", "Pas de client dans la période");
            return resultat;
        }

        long ayantRepondu = clients.stream()
            .filter(c -> c.getEnqueteSatisfaction() != null
                      && !c.getEnqueteSatisfaction().equalsIgnoreCase("NON")
                      && !c.getEnqueteSatisfaction().isBlank())
            .count();

        double taux = ((double) ayantRepondu / clients.size()) * 100;

        resultat.put("tauxRetour",    Math.round(taux * 100.0) / 100.0);
        resultat.put("ayantRepondu",  ayantRepondu);
        resultat.put("totalClients",  clients.size());
        return resultat;
    }
}