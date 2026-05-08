import { COLORS } from "./constants/colors";
 
export default function StatsCards({ clients }) {
  const total = clients.length;
  const encaisse = clients.filter((c) => c.situation === "ENCAISSE").length;
  const nonEncaisse = clients.filter((c) => c.situation && c.situation !== "ENCAISSE").length;
  const totalSomme = clients.reduce((acc, c) => acc + (c.somme || 0), 0);
 
  const stats = [
    { label: "Total Clients", value: total, color: COLORS.secondary },
    { label: "Encaisses", value: encaisse, color: COLORS.green},
    { label: "Non Encaisses", value: nonEncaisse, color: COLORS.red },
    { label: "Total Sommes", value: totalSomme.toLocaleString() + " DH", color: COLORS.orange},
  ];
 
  return (
    <div style={{ display: "flex", gap: "16px", marginBottom: "24px", flexWrap: "wrap" }}>
      {stats.map((stat) => (
        <div key={stat.label} style={{
          backgroundColor: COLORS.white,
          borderRadius: "12px",
          padding: "20px 24px",
          flex: "1",
          minWidth: "160px",
          boxShadow: "0 2px 8px rgba(0,0,0,0.07)",
          borderTop: "4px solid " + stat.color,
        }}>
          <div style={{ fontSize: "26px", marginBottom: "4px" }}>{stat.icon}</div>
          <div style={{ fontSize: "22px", fontWeight: "bold", color: stat.color }}>{stat.value}</div>
          <div style={{ color: "#888", fontSize: "12px" }}>{stat.label}</div>
        </div>
      ))}
    </div>
  );
}