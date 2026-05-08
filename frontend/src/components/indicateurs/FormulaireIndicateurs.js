// components/indicateurs/FormulaireIndicateurs.jsx

import { BLEU, BLEU_MID, BLEU_CL,CardHeader } from "../constants/indicateursUtils";

export default function FormulaireIndicateurs({ debut, fin, moyenneCA, moyenneND, loading, onChange, onCalculer }) {
  return (
    <div style={{ background: "#fff", borderRadius: 14, boxShadow: "0 2px 14px rgba(31,78,121,0.07)", overflow: "hidden", marginBottom: 22 }}>
      <div>
       <CardHeader icon={'⚙️'}  title='Paramètres de calcul'/>
      </div>
      <div style={{ padding: "20px 24px" }}>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))", gap: 14, marginBottom: 16 }}>

          <div style={{ display: "flex", flexDirection: "column", gap: 5 }}>
            <label style={{ fontSize: "0.72rem", fontWeight: 700, color: BLEU, textTransform: "uppercase", letterSpacing: "0.5px" }}>Date début</label>
            <input
              type="date"
              value={debut}
              onChange={e => onChange("debut", e.target.value)}
              style={{ padding: "9px 12px", borderRadius: 8, border: `1.5px solid ${BLEU_CL}`, fontSize: "0.88rem", outline: "none" }}
            />
          </div>

          <div style={{ display: "flex", flexDirection: "column", gap: 5 }}>
            <label style={{ fontSize: "0.72rem", fontWeight: 700, color: BLEU, textTransform: "uppercase", letterSpacing: "0.5px" }}>Date fin</label>
            <input
              type="date"
              value={fin}
              onChange={e => onChange("fin", e.target.value)}
              style={{ padding: "9px 12px", borderRadius: 8, border: `1.5px solid ${BLEU_CL}`, fontSize: "0.88rem", outline: "none" }}
            />
          </div>

          <div style={{ display: "flex", flexDirection: "column", gap: 5 }}>
            <label style={{ fontSize: "0.72rem", fontWeight: 700, color: BLEU, textTransform: "uppercase", letterSpacing: "0.5px" }}>Moyenne CA — 5 ans (MAD)</label>
            <input
              type="number"
              placeholder="ex: 500000"
              value={moyenneCA}
              onChange={e => onChange("moyenneCA", e.target.value)}
              style={{ padding: "9px 12px", borderRadius: 8, border: `1.5px solid ${BLEU_CL}`, fontSize: "0.88rem", outline: "none" }}
            />
            <span style={{ fontSize: "0.68rem", color: "#888" }}>Pour I3</span>
          </div>

          <div style={{ display: "flex", flexDirection: "column", gap: 5 }}>
            <label style={{ fontSize: "0.72rem", fontWeight: 700, color: BLEU, textTransform: "uppercase", letterSpacing: "0.5px" }}>Moyenne ND — 5 ans</label>
            <input
              type="number"
              placeholder="ex: 120"
              value={moyenneND}
              onChange={e => onChange("moyenneND", e.target.value)}
              style={{ padding: "9px 12px", borderRadius: 8, border: `1.5px solid ${BLEU_CL}`, fontSize: "0.88rem", outline: "none" }}
            />
            <span style={{ fontSize: "0.68rem", color: "#888" }}>Pour I5</span>
          </div>

        </div>

        <button
          onClick={onCalculer}
          disabled={loading}
          style={{ background: `linear-gradient(135deg, ${BLEU} 0%, ${BLEU_MID} 100%)`, color: "#fff", border: "none", borderRadius: 9, padding: "11px 28px", fontSize: "0.9rem", fontWeight: 700, cursor: loading ? "not-allowed" : "pointer", opacity: loading ? 0.7 : 1 }}
        >
          {loading ? "⏳ Calcul en cours..." : "▶  Calculer les indicateurs"}
        </button>
      </div>
    </div>
  );
}