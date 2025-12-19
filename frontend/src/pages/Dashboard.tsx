import Navbar from '../components/Navbar';
import ResumeUploader from '../components/ResumeUploader';
import { useResumes, useScans } from '../api/hooks';
import { Link } from 'react-router-dom';

export default function Dashboard() {
  const { data: resumes, refetch } = useResumes();
  const { data: scans } = useScans();
  return (
    <div>
      <Navbar />
      <div className="max-w-6xl mx-auto px-6 py-10 space-y-8">
        <div className="flex items-center justify-between">
          <h2 className="text-2xl font-semibold">My Dashboard</h2>
          <Link to="/scan" className="px-4 py-2 rounded bg-cyan-500 text-slate-900 font-semibold">
            New Scan
          </Link>
        </div>
        <div className="grid md:grid-cols-2 gap-6">
          <ResumeUploader onUploaded={() => refetch()} />
          <div className="card">
            <div className="font-semibold mb-2">Recent Scans</div>
            <div className="space-y-2 text-sm">
              {scans?.map((scan) => (
                <Link key={scan.id} to={`/results/${scan.id}`} className="block p-2 rounded bg-slate-800 hover:bg-slate-700">
                  {scan.scanType} — Score {Math.round(scan.result.overallScore)}
                </Link>
              ))}
              {!scans?.length && <div className="text-slate-500">No scans yet</div>}
            </div>
          </div>
        </div>
        <div className="card">
          <div className="font-semibold mb-2">My Resumes</div>
          <div className="space-y-2 text-sm">
            {resumes?.map((resume) => (
              <div key={resume.id} className="p-2 rounded bg-slate-800">
                {resume.name} — {resume.fileType}
              </div>
            ))}
            {!resumes?.length && <div className="text-slate-500">No resumes uploaded</div>}
          </div>
        </div>
      </div>
    </div>
  );
}
