// components/indicateurs/IndicateursDashboard.jsx

import { useState } from "react";
import Header from "../Header";
import FormulaireIndicateurs from "./FormulaireIndicateurs";
import TableI3 from "./TableI3";
import TableI5 from "./TableI5";
import TableI6 from "./TableI6";
import CarteI15 from "./CarteI15";
import CarteTauxRetour from "./CarteTauxRetour";
import { BLEU, BLEU_MID, JAUNE } from "../constants/indicateursUtils";

const BASE = "http://localhost:8080/api/indicateurs";

export default function IndicateursDashboard() {
  const [params, setParams] = useState({
    debut:     "2025-01-01",
    fin:       "2025-12-31",
    moyenneCA: "",
    moyenneND: "",
  });
  const [loading, setLoading] = useState(false);
  const [data,    setData]    = useState(null);
  const [erreur,  setErreur]  = useState(null);

  function handleChange(key, val) {
    setParams(prev => ({ ...prev, [key]: val }));
  }

  async function calculer() {
    if (!params.debut || !params.fin) { alert("Veuillez renseigner les dates."); return; }
    setLoading(true);
    setErreur(null);
    setData(null);
    try {
      const mCA = params.moyenneCA || 1;
      const mND = params.moyenneND || 1;
      const [i3, i5, i6, i15, taux] = await Promise.all([
        fetch(`${BASE}/i3?debut=${params.debut}&fin=${params.fin}&moyenneCA=${mCA}`).then(r => r.json()),
        fetch(`${BASE}/i5?debut=${params.debut}&fin=${params.fin}&moyenneND=${mND}`).then(r => r.json()),
        fetch(`${BASE}/i6?debut=${params.debut}&fin=${params.fin}`).then(r => r.json()),
        fetch(`${BASE}/i15?debut=${params.debut}&fin=${params.fin}`).then(r => r.json()),
        fetch(`${BASE}/taux-retour?debut=${params.debut}&fin=${params.fin}`).then(r => r.json()),
      ]);
      setData({ i3, i5, i6, i15, taux });
    } catch (e) {
      setErreur("Erreur connexion backend. Vérifiez que Spring Boot tourne sur localhost:8080.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ fontFamily: "'Segoe UI', Tahoma, sans-serif", background: "#F4F6F9", minHeight: "100vh" }}>

      {/* HEADER */}
      <div>
        <Header/>
      </div>

      {/* BODY */}
      <div style={{ maxWidth: 1050, margin: "0 auto", padding: "28px 20px" }}>

        {/* FORMULAIRE */}
        <FormulaireIndicateurs
          {...params}
          loading={loading}
          onChange={handleChange}
          onCalculer={calculer}
        />

        {/* ERREUR */}
        {erreur && (
          <div style={{ background: "#FADBD8", border: "1.5px solid #E74C3C", borderRadius: 8, padding: "12px 16px", color: "#C0392B", fontWeight: 600, fontSize: "0.85rem", marginBottom: 20 }}>
            ⚠️ {erreur}
          </div>
        )}

        {/* PLACEHOLDER */}
        {!data && !loading && !erreur && (
          <div style={{ background: "#EBF3FB", border: "1.5px solid #BDD7EE", borderRadius: 8, padding: "12px 16px", color: BLEU, fontWeight: 600, fontSize: "0.85rem", marginBottom: 20 }}>
            ℹ️ Renseignez les paramètres et cliquez sur "Calculer" pour afficher les indicateurs.
          </div>
        )}

        {/* RÉSULTATS */}
        {data && (
          <>
            {/* I3 + I5 */}
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 20, marginBottom: 22 }}>
              <TableI3 data={data.i3} />
              <TableI5 data={data.i5} />
            </div>

            {/* I6 */}
            <div style={{ marginBottom: 22 }}>
              <TableI6 data={data.i6} />
            </div>

            {/* I15 + Taux retour */}
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 20 }}>
              <CarteI15 data={data.i15} />
              <CarteTauxRetour data={data.taux} />
            </div>
          </>
        )}

      </div>
    </div>
  );
}