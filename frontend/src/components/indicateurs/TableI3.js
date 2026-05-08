// components/indicateurs/TableI3.jsx

import { formatMois, formatNum, Badge, CardHeader, s } from "../constants/indicateursUtils";

export default function TableI3({ data }) {
  return (
    <div style={{ background: "#fff", borderRadius: 14, boxShadow: "0 2px 14px rgba(31,78,121,0.07)", overflow: "hidden" }}>
      <CardHeader icon="📈" title="I3 — Évolution du Chiffre d'Affaires" />
      <div style={{ overflowX: "auto" }}>
        <table style={s.table}>
          <thead>
            <tr>
              <th style={s.th}>Mois</th>
              <th style={s.th}>CA du mois (MAD)</th>
              <th style={s.th}>Cumul CA (MAD)</th>
              <th style={s.th}>I3 (%)</th>
            </tr>
          </thead>
          <tbody>
            {data.map((r, i) => (
              <tr key={i}>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}>{formatMois(r.mois)}</td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}><span style={s.mono}>{formatNum(r.caMois)}</span></td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}><span style={s.mono}>{formatNum(r.caCumul)}</span></td>
                <td style={i % 2 === 0 ? s.td : s.tdAlt}><Badge val={r.i3} /></td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}