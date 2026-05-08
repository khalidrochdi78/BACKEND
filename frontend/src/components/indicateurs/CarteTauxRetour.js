// components/indicateurs/CarteTauxRetour.jsx

import { formatNum, CardHeader, s } from "../constants/indicateursUtils";

export default function CarteTauxRetour({ data }) {
  return (
    <div style={{ background: "#fff", borderRadius: 14, boxShadow: "0 2px 14px rgba(31,78,121,0.07)", overflow: "hidden" }}>
      <CardHeader icon="🔄" title="Taux de Retour Enquête" />
      {data.message
        ? <div style={s.msgWarn}>⚠️ {data.message}</div>
        : <div style={s.kpiGrid}>
            <div style={s.kpi}>
              <div style={s.kpiVal}>
                {formatNum(data.tauxRetour)}<span style={s.kpiUnit}>%</span>
              </div>
              <div style={s.kpiLabel}>Taux de retour</div>
            </div>
            <div style={s.kpi}>
              <div style={s.kpiVal}>
                {data.ayantRepondu}<span style={s.kpiUnit}> / {data.totalClients}</span>
              </div>
              <div style={s.kpiLabel}>Enquêtes remplies</div>
              <div style={s.kpiSub}>sur {data.totalClients} total</div>
            </div>
          </div>
      }
    </div>
  );
}