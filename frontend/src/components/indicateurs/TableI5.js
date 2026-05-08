// components/indicateurs/TableI5.jsx

import { formatMois, formatNum, Badge, CardHeader, s } from "../constants/indicateursUtils";

export default function TableI5({ data }) {
  return (
    <div style={{ background: "#fff", borderRadius: 14, boxShadow: "0 2px 14px rgba(31,78,121,0.07)", overflow: "hidden" }}>
      <CardHeader icon="📋" title="I5 — Évolution des Demandes" />
      <div style={{ overflowX: "auto" }}>
        <table style={s.table}>
          <thead>
            <tr>
              <th style={s.th}>Mois</th>
              <th style={s.th}>ND du mois</th>
              <th style={s.th}>Cumul ND</th>
              <th style={s.th}>I5 (%)</th>
            </tr>
          </thead>
          <tbody>
            {data.map((r, i) => (
              <tr key={i}>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}>{formatMois(r.mois)}</td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}><span style={s.mono}>{r.ndMois}</span></td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}><span style={s.mono}>{r.ndCumul}</span></td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}><Badge val={r.i5} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}