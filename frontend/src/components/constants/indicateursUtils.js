// constants/indicateursUtils.js
import { COLORS } from "./colors";
const MOIS = ["","Janv","Févr","Mars","Avr","Mai","Juin","Juil","Août","Sept","Oct","Nov","Déc"];

export function formatMois(str) {
  const [y, m] = str.split("-");
  return MOIS[parseInt(m)] + " " + y;
}

export function formatNum(n) {
  return new Intl.NumberFormat("fr-MA", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  }).format(n);
}

export const BLEU      = "#1F4E79";
export const BLEU_MID  = "#2E75B6";
export const BLEU_CL   = "#BDD7EE";
export const BLEU_PALE = "#EBF3FB";
export const JAUNE     = "#FFD700";

export const s = {
  cardHeader: { background: `linear-gradient(to right, ${COLORS.left}, ${COLORS.righhht})`,padding: "20px 32px",color:"#ffffff" ,display: "flex",alignItems: "center",gap: "16px",boxShadow: "0 2px 8px rgba(0,0,0,0.2)",width: "100%",boxSizing: "border-box"},
  table:      { width: "100%", borderCollapse: "collapse", fontSize: "0.83rem" },
  th:         { padding: "9px 13px", textAlign: "left", color: "#1F4E79", fontWeight: 700, fontSize: "0.74rem", textTransform: "uppercase", letterSpacing: "0.4px", background: "#EBF3FB", borderBottom: "2px solid #BDD7EE" },
  td:         { padding: "8px 13px", borderBottom: "1px solid #EBF0F5", color: "#333" },
  tdAlt:      { padding: "8px 13px", borderBottom: "1px solid #EBF0F5", color: "#333", background: "#EBF3FB" },
  mono:       { fontFamily: "monospace", fontSize: "0.82rem" },
  badgeG:     { display: "inline-block", padding: "2px 9px", borderRadius: 20, fontWeight: 700, fontSize: "0.8rem", background: "#D5F5E3", color: "#1E8449" },
  badgeY:     { display: "inline-block", padding: "2px 9px", borderRadius: 20, fontWeight: 700, fontSize: "0.8rem", background: "#FEF9E7", color: "#B7950B" },
  badgeR:     { display: "inline-block", padding: "2px 9px", borderRadius: 20, fontWeight: 700, fontSize: "0.8rem", background: "#FADBD8", color: "#C0392B" },
  msgWarn:    { background: "#FEF9E7", border: "1.5px solid #FFD700", borderRadius: 8, padding: "12px 16px", color: "#7D6608", fontWeight: 600, fontSize: "0.85rem", margin: "16px 20px" },
  kpiGrid:    { display: "grid", gridTemplateColumns: "1fr 1fr", gap: 14, padding: "18px 20px" },
  kpi:        { background: "#EBF3FB", borderRadius: 10, padding: "16px", textAlign: "center", border: "1.5px solid #BDD7EE" },
  kpiVal:     { fontSize: "2rem", fontWeight: 800, color: "#1F4E79", lineHeight: 1 },
  kpiUnit:    { fontSize: "1rem", color: "#2E75B6", fontWeight: 600 },
  kpiLabel:   { fontSize: "0.72rem", color: "#666", marginTop: 5, textTransform: "uppercase", letterSpacing: "0.4px" },
  kpiSub:     { fontSize: "0.72rem", color: "#999", marginTop: 3 },
};

export function Badge({ val, type }) {
  if (type === "i6") {
    if (val === 0) return <span style={s.badgeY}>— aucune demande</span>;
    if (val <= 3)  return <span style={s.badgeG}>≤ 3j ✔</span>;
    if (val <= 7)  return <span style={s.badgeY}>{formatNum(val)} j</span>;
    return <span style={s.badgeR}>{formatNum(val)} j ⚠</span>;
  }
  if (val >= 100) return <span style={s.badgeG}>{formatNum(val)} %</span>;
  if (val >= 50)  return <span style={s.badgeY}>{formatNum(val)} %</span>;
  return <span style={s.badgeR}>{formatNum(val)} %</span>;
}

export function CardHeader({ icon, title }) {
  return <div style={s.cardHeader}>{icon} {title}</div>;
}