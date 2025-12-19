import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';

export default function Landing() {
  return (
    <div>
      <Navbar />
      <div className="px-6 py-16 max-w-6xl mx-auto grid md:grid-cols-2 gap-10 items-center">
        <div>
          <p className="uppercase text-xs tracking-[0.3em] text-cyan-300">ATS Resume Scanner</p>
          <h1 className="text-4xl md:text-5xl font-bold mt-4 mb-6 text-white">
            Ship resumes that pass ATS and match the market.
          </h1>
          <p className="text-slate-300 mb-8">
            Upload your resume, paste a job description, and get a clear ATS score, keyword coverage, missing skills,
            and market trend insights — all in seconds.
          </p>
          <div className="flex gap-4">
            <Link to="/signup" className="px-5 py-3 rounded bg-cyan-500 text-slate-900 font-semibold">
              Get Started
            </Link>
            <Link to="/login" className="px-5 py-3 rounded border border-slate-700 text-slate-200">
              Login
            </Link>
          </div>
        </div>
        <div className="card shadow-xl">
          <div className="text-sm text-slate-400 mb-2">Quick Preview</div>
          <div className="text-2xl font-semibold text-cyan-300 mb-2">92 / 100</div>
          <p className="text-slate-300">Strong keyword match for Senior Java role, missing: Terraform, Grafana.</p>
          <ul className="mt-4 space-y-2 text-sm text-slate-200">
            <li>• Keyword coverage: 88%</li>
            <li>• Skills match: 85%</li>
            <li>• Formatting: Clean (ATS-friendly)</li>
            <li>• Market trend: Add "Helm", "ArgoCD" for UK roles</li>
          </ul>
        </div>
      </div>
    </div>
  );
}
