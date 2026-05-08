package com.example.clients;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

    // ── Couleurs Maroc Meteo ──────────────────────────────────────────────────
    private static final BaseColor BLEU_HEADER  = new BaseColor(0x1F, 0x4E, 0x79); // bleu foncé
    private static final BaseColor BLEU_CLAIR   = new BaseColor(0xBD, 0xD7, 0xEE); // bleu clair pour les labels
    private static final BaseColor GRIS_LIGNE   = new BaseColor(0xF2, 0xF2, 0xF2); // fond lignes alternées
    private static final BaseColor BLANC        = BaseColor.WHITE;

    // ── Fonts ─────────────────────────────────────────────────────────────────
    private static final Font FONT_TITRE        = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD,   BLANC);
    private static final Font FONT_SOUS_TITRE   = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD,   BLEU_HEADER);
    private static final Font FONT_LABEL        = new Font(Font.FontFamily.HELVETICA, 8,  Font.BOLD,   BLEU_HEADER);
    private static final Font FONT_VALEUR       = new Font(Font.FontFamily.HELVETICA, 8,  Font.NORMAL, BaseColor.BLACK);
    private static final Font FONT_FOOTER       = new Font(Font.FontFamily.HELVETICA, 7,  Font.ITALIC, BaseColor.GRAY);
    private static final Font FONT_SECTION      = new Font(Font.FontFamily.HELVETICA, 9,  Font.BOLD,   BLANC);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ─────────────────────────────────────────────────────────────────────────
    public byte[] generateClientPdf(Client c) throws Exception {
        Document doc = new Document(PageSize.A4, 36, 36, 36, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);

        // Footer automatique
        writer.setPageEvent(new FooterEvent());

        doc.open();

        // ── 1. EN-TÊTE ────────────────────────────────────────────────────────
        doc.add(buildHeader(c));
        doc.add(Chunk.NEWLINE);

        // ── 2. CADRE DOSSIER / DEVIS / BL / FACTURE ──────────────────────────
        doc.add(buildSectionTitle("RÉFÉRENCES DU DOSSIER"));
        doc.add(buildReferences(c));
        doc.add(Chunk.NEWLINE);

        // ── 3. INFORMATIONS CLIENT ────────────────────────────────────────────
        doc.add(buildSectionTitle("INFORMATIONS CLIENT"));
        doc.add(buildInfoClient(c));
        doc.add(Chunk.NEWLINE);

        // ── 4. PRESTATIONS ────────────────────────────────────────────────────
        doc.add(buildSectionTitle("PRESTATIONS MÉTÉOROLOGIQUES"));
        doc.add(buildPrestations(c));
        doc.add(Chunk.NEWLINE);

        // ── 5. SITUATION FINANCIÈRE ───────────────────────────────────────────
        doc.add(buildSectionTitle("SITUATION FINANCIÈRE"));
        doc.add(buildFinance(c));
        doc.add(Chunk.NEWLINE);

        // ── 6. SUIVI ──────────────────────────────────────────────────────────
        doc.add(buildSectionTitle("SUIVI"));
        doc.add(buildSuivi(c));

        doc.close();
        return baos.toByteArray();
    }

    // ── EN-TÊTE ───────────────────────────────────────────────────────────────
    private PdfPTable buildHeader(Client c) throws Exception {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1f, 2f});

        // Colonne gauche : institution
        PdfPCell left = new PdfPCell();
        left.setBorder(Rectangle.NO_BORDER);
        left.addElement(para("ROYAUME DU MAROC",
                new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BLEU_HEADER)));
        left.addElement(para("Ministère de l'Equipement et de l'Eau",
                new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, BLEU_HEADER)));
        left.addElement(para("DIRECTION GÉNÉRALE DE LA MÉTÉOROLOGIE",
                new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD, BLEU_HEADER)));
        left.addElement(para("Direction Régionale du Sud – SCC",
                new Font(Font.FontFamily.HELVETICA, 7, Font.ITALIC, BLEU_HEADER)));
        table.addCell(left);

        // Colonne droite : titre de la fiche
        PdfPCell right = new PdfPCell();
        right.setBackgroundColor(BLEU_HEADER);
        right.setPadding(10);
        right.setVerticalAlignment(Element.ALIGN_MIDDLE);
        right.setBorder(Rectangle.NO_BORDER);
        right.addElement(para("FICHE CLIENT – PRESTATIONS MÉTÉOROLOGIQUES",
                new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BLANC)));
        right.addElement(para("Année  " + safe(c.getAnnee()),
                new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, new BaseColor(0xFF, 0xD7, 0x00))));
        table.addCell(right);

        return table;
    }

    // ── TITRE DE SECTION ──────────────────────────────────────────────────────
    private PdfPTable buildSectionTitle(String titre) throws Exception {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);
        t.setSpacingBefore(4);
        PdfPCell cell = new PdfPCell(new Phrase(titre, FONT_SECTION));
        cell.setBackgroundColor(BLEU_HEADER);
        cell.setPadding(5);
        cell.setBorder(Rectangle.NO_BORDER);
        t.addCell(cell);
        return t;
    }

    // ── RÉFÉRENCES ────────────────────────────────────────────────────────────
    private PdfPTable buildReferences(Client c) throws Exception {
        PdfPTable t = new PdfPTable(4);
        t.setWidthPercentage(100);
        t.setWidths(new float[]{1f, 1f, 1f, 1f});

        addLabelValue(t, "Dossier N°",  "N° " + safe(c.getNr()) + "/" + safe(c.getAnnee()), GRIS_LIGNE);
        addLabelValue(t, "Devis N°",    safe(c.getNumeroDevis()), BLANC);
        addLabelValue(t, "BL N°",       safe(c.getNumeroBL()), GRIS_LIGNE);
        addLabelValue(t, "Facture N°",  safe(c.getNumeroFacture()), BLANC);

        return t;
    }

    // ── INFO CLIENT ───────────────────────────────────────────────────────────
    private PdfPTable buildInfoClient(Client c) throws Exception {
        PdfPTable t = new PdfPTable(2);
        t.setWidthPercentage(100);

        addRow2(t, "Nom du client",       safe(c.getNomClient()),            GRIS_LIGNE);
        addRow2(t, "Date de demande",     formatDate(c.getDateDemandeService()), BLANC);
        addRow2(t, "Suivi par",           safe(c.getSuiviPar()),             GRIS_LIGNE);
        addRow2(t, "Mode de paiement",    safe(c.getModePaiement()),         BLANC);
        addRow2(t, "Enquête satisfaction",formatEnquete(c.getEnqueteSatisfaction()), GRIS_LIGNE);

        return t;
    }

    // ── PRESTATIONS ───────────────────────────────────────────────────────────
    private PdfPTable buildPrestations(Client c) throws Exception {
        PdfPTable t = new PdfPTable(2);
        t.setWidthPercentage(100);

        addRow2(t, "Entité productrice",  "SCC/DRMS",                        GRIS_LIGNE);
        addRow2(t, "Date du devis",       formatDate(c.getDateDevis()),       BLANC);
        addRow2(t, "Situation",           safe(c.getSituation()),             GRIS_LIGNE);

        return t;
    }

    // ── FINANCES ─────────────────────────────────────────────────────────────
    private PdfPTable buildFinance(Client c) throws Exception {
        PdfPTable t = new PdfPTable(2);
        t.setWidthPercentage(100);

        addRow2(t, "Somme totale (MAD)",   formatMontant(c.getSomme()),          GRIS_LIGNE);
        addRow2(t, "Somme encaissée (MAD)",formatMontant(c.getSommeEncaissee()), BLANC);

        double reste = 0;
        if (c.getSomme() != null && c.getSommeEncaissee() != null) {
            reste = c.getSomme() - c.getSommeEncaissee();
        }
        addRow2(t, "Reste à payer (MAD)",  String.format("%.2f", reste),          GRIS_LIGNE);

        addRow2(t, "Date BL",              formatDate(c.getDateBL()),             BLANC);
        addRow2(t, "Date facture",         formatDate(c.getDateFacture()),        GRIS_LIGNE);

        return t;
    }

    // ── SUIVI ─────────────────────────────────────────────────────────────────
    private PdfPTable buildSuivi(Client c) throws Exception {
        PdfPTable t = new PdfPTable(1);
        t.setWidthPercentage(100);

        String situation = safe(c.getSituation()).toUpperCase();
        String texte;
        if (situation.contains("CONCRET") || situation.contains("PAYÉ") || situation.contains("PAYE")) {
            texte = "✔  Concrétisé";
        } else if (situation.contains("CLASS")) {
            texte = "✔  Classé";
        } else {
            texte = "•  " + safe(c.getSituation());
        }

        PdfPCell cell = new PdfPCell(new Phrase(texte,
                new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BLEU_HEADER)));
        cell.setPadding(8);
        cell.setBackgroundColor(BLEU_CLAIR);
        cell.setBorderColor(BLEU_HEADER);
        t.addCell(cell);

        return t;
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────

    /** Ajoute label + valeur dans une table à 4 colonnes (références) */
    private void addLabelValue(PdfPTable t, String label, String value, BaseColor bg) {
        PdfPCell lbl = new PdfPCell(new Phrase(label, FONT_LABEL));
        lbl.setBackgroundColor(BLEU_CLAIR);
        lbl.setPadding(5);
        lbl.setBorderColor(BaseColor.LIGHT_GRAY);
        t.addCell(lbl);

        PdfPCell val = new PdfPCell(new Phrase(value, FONT_VALEUR));
        val.setBackgroundColor(bg);
        val.setPadding(5);
        val.setBorderColor(BaseColor.LIGHT_GRAY);
        t.addCell(val);
    }

    /** Ajoute une ligne label | valeur dans une table à 2 colonnes */
    private void addRow2(PdfPTable t, String label, String value, BaseColor bg) {
        PdfPCell lbl = new PdfPCell(new Phrase(label, FONT_LABEL));
        lbl.setBackgroundColor(BLEU_CLAIR);
        lbl.setPadding(5);
        lbl.setBorderColor(BaseColor.LIGHT_GRAY);
        lbl.setMinimumHeight(20f);
        t.addCell(lbl);

        PdfPCell val = new PdfPCell(new Phrase(value, FONT_VALEUR));
        val.setBackgroundColor(bg);
        val.setPadding(5);
        val.setBorderColor(BaseColor.LIGHT_GRAY);
        t.addCell(val);
    }

    private Paragraph para(String text, Font font) {
        return new Paragraph(text, font);
    }

    private String safe(Object o) {
        if (o == null) return "—";
        String s = o.toString().trim();
        return s.isEmpty() ? "—" : s;
    }

    private String formatDate(java.time.LocalDate d) {
        return d == null ? "—" : d.format(FMT);
    }

    private String formatMontant(Double v) {
        return v == null ? "—" : String.format("%,.2f", v);
    }

    private String formatEnquete(String e) {
        if (e == null) return "—";
        return switch (e.trim().toUpperCase()) {
            case "1", "OUI" -> "Oui ✔";
            case "NON"       -> "Non ✘";
            default          -> e;
        };
    }

    // ── FOOTER (numéro de page + adresse) ────────────────────────────────────
    static class FooterEvent extends PdfPageEventHelper {
        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();

            // Ligne de séparation
            cb.setColorStroke(new BaseColor(0x1F, 0x4E, 0x79));
            cb.setLineWidth(0.5f);
            cb.moveTo(36, 42);
            cb.lineTo(document.getPageSize().getWidth() - 36, 42);
            cb.stroke();

            // Texte footer
            Font fFooter = new Font(Font.FontFamily.HELVETICA, 6, Font.ITALIC, BaseColor.GRAY);
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("Boulevard Mohamed Taïeb Naciri B.P 8106 Casa Oasis, Hay Hassani, Casablanca  |  " +
                               "http://www.marocmeteo.ma  |  Tél : 05 22 65 49 71  |  IF : 24923150  |  ICE : 003382543000040",
                               fFooter),
                    document.getPageSize().getWidth() / 2, 30, 0);

            cb.restoreState();
        }
    }
}