import { COLORS } from "./constants/colors";
import StatusBadge from "./badges/StatusBadge";
import EnqueteBadge from "./badges/EnqueteBadge";

/*definition de headers de tableau avec label(ui) et key(client)*/
const HEADERS = [
  { label: "Nr", key: "nr" },
  { label: "Nom Client", key: "nomClient" },
  { label: "Suivi Par", key: "suiviPar" },
  { label: "N Devis", key: "numeroDevis" },
  { label: "Date Devis", key: "dateDevis" },
  { key: "dateDemandeService",   label: "Date Demande" },
  { label: "Somme (DH)", key: "somme" },
  { label: "Somme Encaissee", key: "sommeEncaissee" },
  { label: "N BL", key: "numeroBL" },
  { label: "Date BL", key: "dateBL" },
  { label: "N Facture", key: "numeroFacture" },
  { label: "Date Facture", key: "dateFacture" },
  { label: "Mode Paiement", key: "modePaiement" },
  { label: "Situation", key: "situation" },
  { label: "Enquete", key: "enqueteSatisfaction" },
  { label: "Pdf", key: "pdf"}
];
 
export default function ClientsTable({ clients, loading, error }) {
  /*en cas de loading*/
  if (loading) {
    return (
      <div style={{ padding: "40px", textAlign: "center", color: COLORS.secondary }}>
        Chargement...
      </div>
    );
  }
 /*en cas de error*/
  if (error) {
    return (
      <div style={{ padding: "40px", textAlign: "center", color: COLORS.red }}>
        Erreur: {error}
      </div>
    );
  }
 
  return (
    <div style={{
      backgroundColor: COLORS.white,
      borderRadius: "12px",
      boxShadow: "0 2px 8px rgba(0,0,0,0.07)",
      /*auto scroling because table is too big*/
      overflowX: "auto",
    }}>
      <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "13px" }}>
        <thead>
          <tr style={{ backgroundColor: COLORS.primary, color: "#fff" }}>
            {HEADERS.map((h) => (
              <th key={h.key} style={{
                padding: "12px 14px",
                textAlign: "left",
                fontWeight: "600",
                whiteSpace: "nowrap",
              }}>
                {h.label}
              </th>
            ))}
           
          </tr>
        </thead>
        <tbody>
          
          {clients.length === 0 ? (
            <tr>
              <td colSpan={15} style={{ padding: "30px", textAlign: "center", color: "#aaa" }}>
                Aucun client trouve
              </td>
            </tr>
          ) : (
            clients.map((c, i) => (
              <tr
                key={i}
                style={{
                  backgroundColor: i % 2 === 0 ? COLORS.white : "#f0f5ff",
                  borderBottom: "1px solid #e8edf5",
                }}
                onMouseEnter={(e) => e.currentTarget.style.backgroundColor = "#dce8ff"}
                onMouseLeave={(e) => e.currentTarget.style.backgroundColor = i % 2 === 0 ? COLORS.white : "#f0f5ff"}
              >
                <td style={{ padding: "10px 14px", color: COLORS.secondary, fontWeight: "bold" }}>{c.nr}</td>
                <td style={{ padding: "10px 14px", fontWeight: "600", color: COLORS.text, whiteSpace: "nowrap" }}>{c.nomClient || "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555" }}>{c.suiviPar || "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555" }}>{c.numeroDevis || "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555", whiteSpace: "nowrap" }}>{c.dateDevis || "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555", whiteSpace: "nowrap" }}>{c.dateDemandeService || "—"}</td>
                <td style={{ padding: "10px 14px", fontWeight: "600", color: COLORS.primary }}>{c.somme ? c.somme.toLocaleString() + " DH" : "—"}</td>
                <td style={{ padding: "10px 14px", color: COLORS.green, fontWeight: "600" }}>{c.sommeEncaissee ? c.sommeEncaissee.toLocaleString() + " DH" : "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555" }}>{c.numeroBL || "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555", whiteSpace: "nowrap" }}>{c.dateBL || "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555" }}>{c.numeroFacture || "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555", whiteSpace: "nowrap" }}>{c.dateFacture || "—"}</td>
                <td style={{ padding: "10px 14px", color: "#555" }}>{c.modePaiement || "—"}</td>
                <td style={{ padding: "10px 14px" }}><StatusBadge situation={c.situation} /></td>
                <td style={{ padding: "10px 14px" }}><EnqueteBadge value={c.enqueteSatisfaction} /></td>
                <td style={{ padding: "10px 14px" }}>
      <button
  onClick={async () => {
    try {
      const response = await fetch(`/api/clients/${c.nr}/pdf`);
      
      if (!response.ok) throw new Error("Erreur serveur");
      
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      
      const a = document.createElement('a');
      a.href = url;
      a.download = `fiche_client_${c.nr}.pdf`;
      a.click();
      
      window.URL.revokeObjectURL(url); // n9awdo memory
    } catch (err) {
      console.error("Erreur téléchargement PDF :", err);
    }
  }}
  style={{
    backgroundColor: "#1a73e8",
    color: "white",
    border: "none",
    borderRadius: "5px",
    padding: "5px 10px",
    cursor: "pointer"
  }}
>
  PDF
</button>
                 </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}