import Navbar from '../components/Navbar';
import MarketSelector from '../components/MarketSelector';
import { useState } from 'react';
import { useMarketScan, useResumes } from '../api/hooks';
import { useNavigate } from 'react-router-dom';

export default function MarketTrend() {
  const [roleKey, setRoleKey] = useState('');
  const { data: resumes } = useResumes();
  const [resumeId, setResumeId] = useState('');
  const marketScan = useMarketScan();
  const navigate = useNavigate();

  const submit = async () => {
    if (!roleKey || !resumeId) return;
    const res = await marketScan.mutateAsync({ roleKey, resumeId: Number(resumeId) });
    navigate(`/results/${res.id}`);
  };

  return (
    <div>
      <Navbar />
      <div className="max-w-4xl mx-auto px-6 py-10 space-y-4">
        <h2 className="text-2xl font-semibold">Market Trend Scan</h2>
        <MarketSelector onSelect={setRoleKey} />
        <div className="card space-y-3">
          <div className="text-sm text-slate-400">Select Resume</div>
          <select value={resumeId} onChange={(e) => setResumeId(e.target.value)} className="w-full bg-slate-800 px-3 py-2 rounded">
            <option value="">Choose resume</option>
            {resumes?.map((r: any) => (
              <option key={r.id} value={r.id}>{r.name}</option>
            ))}
          </select>
          <button onClick={submit} className="px-4 py-2 rounded bg-cyan-500 text-slate-900 font-semibold">
            {marketScan.isPending ? 'Scanning...' : 'Run Market Scan'}
          </button>
        </div>
      </div>
    </div>
  );
}
