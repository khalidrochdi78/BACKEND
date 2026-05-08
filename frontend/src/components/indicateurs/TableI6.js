// components/indicateurs/TableI6.jsx

import { formatMois, formatNum, Badge, CardHeader, s } from "../constants/indicateursUtils";

export default function TableI6({ data }) {
  return (
    <div style={{ background: "#fff", borderRadius: 14, boxShadow: "0 2px 14px rgba(31,78,121,0.07)", overflow: "hidden" }}>
      <CardHeader icon="⏱️" title="I6 — Délai de Réactivité Devis (jours ouvrés)" />
      <div style={{ overflowX: "auto" }}>
        <table style={s.table}>
          <thead>
            <tr>
              <th style={s.th}>Mois</th>
              <th style={s.th}>Nb demandes</th>
              <th style={s.th}>Délai moyen I6</th>
              <th style={s.th}>Appréciation</th>
            </tr>
          </thead>
          <tbody>
            {data.map((r, i) => (
              <tr key={i}>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}>{formatMois(r.mois)}</td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}><span style={s.mono}>{r.nbDemandes}</span></td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}>
                  <span style={s.mono}>{r.nbDemandes > 0 ? formatNum(r.i6MoyenJours) + " j" : "—"}</span>
                </td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}><Badge val={r.i6MoyenJours} type="i6" /></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}