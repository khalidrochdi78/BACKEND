import { COLORS } from "./constants/colors";
 
export default function SearchBar({ value, onChange, years, selectedYear, onYearChange }) {
  return (
    <div style={{ marginBottom: "16px", display: "flex", gap: "12px", alignItems: "center", flexWrap: "wrap" }}>
      <input
        type="text"
        placeholder="Rechercher par nom client..."
        value={value}
        onChange={(e) => onChange(e.target.value)}
        style={{
          padding: "10px 16px",
          borderRadius: "8px",
          border: "1px solid " + COLORS.accent,
          width: "300px",
          fontSize: "14px",
          outline: "none",
          color: COLORS.text,
        }}
      />

      <select
        value={selectedYear}
        onChange={(e) => onYearChange(e.target.value)}
        style={{
          padding: "10px 12px",
          borderRadius: "8px",
          border: "1px solid " + COLORS.accent,
          minWidth: "170px",
          fontSize: "14px",
          outline: "none",
          color: COLORS.text,
          backgroundColor: COLORS.white,
        }}
      >
        <option value="ALL">Toutes les annees</option>
        {years.map((year) => (
          <option key={year} value={year}>{year}</option>
        ))}
      </select>
    </div>
  );
}