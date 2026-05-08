import { BrowserRouter, Routes, Route } from "react-router-dom";
import IndicateursDashboard from "./components/indicateurs/IndicateursDashboard";
import ClientsDashboard from './ClientsDashboard';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<ClientsDashboard />} />
        <Route path="/indicateurs" element={<IndicateursDashboard />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;