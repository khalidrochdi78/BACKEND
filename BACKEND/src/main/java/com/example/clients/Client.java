package com.example.clients;

import java.time.LocalDate;

public class Client {
    private Integer nr;
    private String annee;
    private String nomClient;
    private String suiviPar;
    private String numeroDevis;
    private LocalDate dateDevis;
    private LocalDate dateDemandeService;
    private Double somme;
    private Double sommeEncaissee;
    private String numeroBL;
    private LocalDate dateBL;
    private String numeroFacture;
    private LocalDate dateFacture;
    private String modePaiement;
    private String situation;
    private String enqueteSatisfaction; // "1" = oui, "NON" = non, null = vide

    // Getters et Setters
    public Integer getNr() { return nr; }
    public void setNr(Integer nr) { this.nr = nr; }

    public String getAnnee() { return annee; }
    public void setAnnee(String annee) { this.annee = annee; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public String getSuiviPar() { return suiviPar; }
    public void setSuiviPar(String suiviPar) { this.suiviPar = suiviPar; }

    public String getNumeroDevis() { return numeroDevis; }
    public void setNumeroDevis(String numeroDevis) { this.numeroDevis = numeroDevis; }

    public LocalDate getDateDevis() { return dateDevis; }
    public void setDateDevis(LocalDate dateDevis) { this.dateDevis = dateDevis; }

    public LocalDate getDateDemandeService() { return dateDemandeService; }
    public void setDateDemandeService(LocalDate dateDemandeService) { this.dateDemandeService = dateDemandeService; }

    public Double getSomme() { return somme; }
    public void setSomme(Double somme) { this.somme = somme; }

    public Double getSommeEncaissee() { return sommeEncaissee; }
    public void setSommeEncaissee(Double sommeEncaissee) { this.sommeEncaissee = sommeEncaissee; }

    public String getNumeroBL() { return numeroBL; }
    public void setNumeroBL(String numeroBL) { this.numeroBL = numeroBL; }

    public LocalDate getDateBL() { return dateBL; }
    public void setDateBL(LocalDate dateBL) { this.dateBL = dateBL; }

    public String getNumeroFacture() { return numeroFacture; }
    public void setNumeroFacture(String numeroFacture) { this.numeroFacture = numeroFacture; }

    public LocalDate getDateFacture() { return dateFacture; }
    public void setDateFacture(LocalDate dateFacture) { this.dateFacture = dateFacture; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public String getSituation() { return situation; }
    public void setSituation(String situation) { this.situation = situation; }

    public String getEnqueteSatisfaction() { return enqueteSatisfaction; }
    public void setEnqueteSatisfaction(String enqueteSatisfaction) {
        this.enqueteSatisfaction = enqueteSatisfaction;
    }
}
