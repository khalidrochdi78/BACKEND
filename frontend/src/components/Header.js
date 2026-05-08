import { useNavigate } from "react-router-dom";
import { COLORS } from "./constants/colors";
export default function Header() {
  const navigate = useNavigate();
  const isIndicateurs = window.location.pathname === "/indicateurs";

  return (
    <div style={{
      background: `linear-gradient(to right, ${COLORS.left}, ${COLORS.righhht})`,
      padding: "20px 32px",
      display: "flex",
      alignItems: "center",
      gap: "16px",
      boxShadow: "0 2px 8px rgba(0,0,0,0.2)",
      width: "100%",
      boxSizing: "border-box",
    }}>
      <div style={{
        width: "42px", height: "42px",
        backgroundColor: COLORS.white,
        borderRadius: "50%",
        display: "flex", alignItems: "center", justifyContent: "center",
        fontSize: "20px",
      }}>
      <img 
      src="/logo.jpg" alt="logo-ssc"
      style={{ width: "100%", height: "100%", borderRadius: "50%", objectFit: "cover" }} 
      />
      </div>

      <div>
        <div style={{ color: "#fff", fontWeight: "bold", fontSize: "18px" }}>
          Maroc Meteo — SCC
        </div>
        <div style={{ color: "#a0c4ff", fontSize: "12px" }}>
          Gestion des Clients
        </div>
      </div>

      {/* Bouton selon la page */}
      {isIndicateurs ? (
        <button
          onClick={() => navigate("/")}
          style={{
            marginLeft: "auto",
            background: "#FFD700",
            color: "#1F4E79",
            border: "none",
            borderRadius: "8px",
            padding: "8px 18px",
            fontWeight: 700,
            fontSize: "0.88rem",
            cursor: "pointer",
          }}
        >
          ⬅ Retour
        </button>
      ) : (
        <button
          onClick={() => navigate("/indicateurs")}
          style={{
            marginLeft: "auto",
            background: "#FFD700",
            color: "#1F4E79",
            border: "none",
            borderRadius: "8px",
            padding: "8px 18px",
            fontWeight: 700,
            fontSize: "0.88rem",
            cursor: "pointer",
          }}
        >
          📊 Indicateurs
        </button>
      )}

    </div>
  );
}