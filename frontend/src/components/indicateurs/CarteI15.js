// components/indicateurs/CarteI15.jsx

import { formatNum, CardHeader, s } from "../constants/indicateursUtils";

export default function CarteI15({ data }) {
  return (
    <div style={{ background: "#fff", borderRadius: 14, boxShadow: "0 2px 14px rgba(31,78,121,0.07)", overflow: "hidden" }}>
      <CardHeader icon="⭐" title="I15 — Note de Satisfaction Globale" />
      {data.message
        ? <div style={s.msgWarn}>⚠️ {data.message}</div>
        : <div style={s.kpiGrid}>
            <div style={s.kpi}>
              <div style={s.kpiVal}>
                {formatNum(data.i15)}<span style={s.kpiUnit}>%</span>
              </div>
              <div style={s.kpiLabel}>Note moyenne I15</div>
            </div>
            <div style={s.kpi}>
              <div style={s.kpiVal}>
                {data.nbReponses}<span style={s.kpiUnit}> / {data.nbClients}</span>
              </div>
              <div style={s.kpiLabel}>Clients ayant répondu</div>
              <div style={s.kpiSub}>sur {data.nbClients} total</div>
            </div>
          </div>
      }
    </div>
  );
}