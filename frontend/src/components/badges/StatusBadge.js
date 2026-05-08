import { COLORS } from "../constants/colors";
 
export default function StatusBadge({ situation }) {
  const color =
    situation === "ENCAISSE" ? COLORS.green
    : situation === "CLASSE" ? COLORS.orange
    : COLORS.red;
 
  return (
    <span style={{
      backgroundColor: color,
      color: "#fff",
      padding: "3px 10px",
      borderRadius: "12px",
      fontSize: "11px",
      fontWeight: "bold",
      whiteSpace: "nowrap",
    }}>
      {situation || "—"}
    </span>
  );
}