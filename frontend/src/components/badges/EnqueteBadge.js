import { COLORS } from "../constants/colors";
 
export default function EnqueteBadge({ value }) {
  const isOui = value === "OUI" || value === "1";
 
  return (
    <span style={{
      backgroundColor: isOui ? COLORS.green : "#aaa",
      color: "#fff",
      padding: "3px 10px",
      borderRadius: "12px",
      fontSize: "11px",
      fontWeight: "bold",
    }}>
      {isOui ? "OUI" : "NON"}
    </span>
  );
}
 