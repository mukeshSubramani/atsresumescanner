import { useForm } from 'react-hook-form';
import Navbar from '../components/Navbar';
import { useJdScan, useMarketScan, useResumes } from '../api/hooks';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';

interface JdFormValues {
  resumeId: string;
  jdText: string;
  title?: string;
  company?: string;
  location?: string;
}

interface MarketFormValues {
  resumeId: string;
  roleKey: string;
  location?: string;
}

type FormValues = JdFormValues | MarketFormValues;

export default function NewScan() {
  const { data: resumes } = useResumes();
  const jdScan = useJdScan();
  const marketScan = useMarketScan();
  const navigate = useNavigate();
  const [mode, setMode] = useState<'jd' | 'market'>('jd');
  const { register, handleSubmit } = useForm<FormValues>();

  const onSubmit = async (values: FormValues) => {
    const resumeId = Number(values.resumeId);
    if (!resumeId) return;
    if (mode === 'jd') {
      const res = await jdScan.mutateAsync({ resumeId, jdText: values.jdText, title: values.title, company: values.company, location: values.location });
      navigate(`/results/${res.id}`);
    } else {
      const res = await marketScan.mutateAsync({ resumeId, roleKey: values.roleKey, location: values.location });
      navigate(`/results/${res.id}`);
    }
  };

  return (
    <div>
      <Navbar />
      <div className="max-w-5xl mx-auto px-6 py-10 space-y-6">
        <div className="flex items-center gap-3">
          <button className={`px-3 py-2 rounded ${mode === 'jd' ? 'bg-cyan-500 text-slate-900' : 'bg-slate-800'}`} onClick={() => setMode('jd')}>JD Match Scan</button>
          <button className={`px-3 py-2 rounded ${mode === 'market' ? 'bg-cyan-500 text-slate-900' : 'bg-slate-800'}`} onClick={() => setMode('market')}>Market Trend Scan</button>
        </div>
        <form className="space-y-4" onSubmit={handleSubmit(onSubmit)}>
          <div>
            <label className="text-sm text-slate-400">Resume</label>
            <select {...register('resumeId')} className="w-full bg-slate-800 px-3 py-2 rounded mt-1">
              <option value="">Select resume</option>
              {resumes?.map((resume) => (
                <option key={resume.id} value={resume.id}>{resume.name}</option>
              ))}
            </select>
          </div>
          {mode === 'jd' && (
            <>
              <input {...register('title')} placeholder="Job Title" className="w-full bg-slate-800 px-3 py-2 rounded" />
              <textarea {...register('jdText')} placeholder="Paste job description" className="w-full bg-slate-800 px-3 py-2 rounded min-h-[200px]" />
            </>
          )}
          {mode === 'market' && (
            <>
              <input {...register('roleKey')} placeholder="Role template key (e.g., java-senior-uk)" className="w-full bg-slate-800 px-3 py-2 rounded" />
              <input {...register('location')} placeholder="Location (default UK)" className="w-full bg-slate-800 px-3 py-2 rounded" />
            </>
          )}
          <button type="submit" className="px-4 py-2 rounded bg-cyan-500 text-slate-900 font-semibold">
            {mode === 'jd' ? (jdScan.isPending ? 'Scanning...' : 'Scan JD Match') : (marketScan.isPending ? 'Scanning...' : 'Scan Market Fit')}
          </button>
        </form>
      </div>
    </div>
  );
}
