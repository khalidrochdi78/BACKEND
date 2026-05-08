import { useState, useEffect } from "react";
import { COLORS } from "./components/constants/colors";
import Header from "./components/Header";
import StatsCards from "./components/StatsCards";
import SearchBar from "./components/SearchBar";
import ClientsTable from "./components/ClientsTable";
 
export default function ClientsDashboard() {
  const [clients, setClients] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [search, setSearch] = useState("");
  const [selectedYear, setSelectedYear] = useState("ALL");
 
  useEffect(() => {
    fetch("http://localhost:8080/api/clients")
      .then((res) => {
        if (!res.ok) throw new Error("Erreur API");
        return res.json();
      })
      .then((data) => {
        setClients(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);
 
  const years = [...new Set(clients.map((c) => c.annee).filter(Boolean))].sort((a, b) => b.localeCompare(a));

  const filtered = clients.filter((c) => {
    const matchSearch = (c.nomClient || "").toLowerCase().includes(search.toLowerCase());
    const matchYear = selectedYear === "ALL" || c.annee === selectedYear;
    return matchSearch && matchYear;
  });
 
  return (
    <div style={{ backgroundColor: COLORS.bg, minHeight: "100vh", fontFamily: "Segoe UI, sans-serif" }}>
      <Header />
      <div style={{ padding: "24px 32px" }}>
        <StatsCards clients={clients} />
        <SearchBar
          value={search}
          onChange={setSearch}
          years={years}
          selectedYear={selectedYear}
          onYearChange={setSelectedYear}
        />
        <ClientsTable clients={filtered} loading={loading} error={error} />
        <div style={{ marginTop: "12px", color: "#aaa", fontSize: "12px", textAlign: "right" }}>
          {filtered.length} client(s) affiches sur {clients.length}
        </div>
      </div>
    </div>
  );
}