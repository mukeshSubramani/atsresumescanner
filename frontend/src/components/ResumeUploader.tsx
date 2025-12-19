import { useUploadResume } from '../api/hooks';
import { useState } from 'react';

export default function ResumeUploader({ onUploaded }: { onUploaded: (resume: any) => void }) {
  const [file, setFile] = useState<File | null>(null);
  const mutation = useUploadResume();

  const submit = async () => {
    if (!file) return;
    const res = await mutation.mutateAsync(file);
    onUploaded(res);
  };

  return (
    <div className="card space-y-3">
      <div className="font-semibold">Upload Resume</div>
      <input
        type="file"
        accept=".pdf,.doc,.docx,.txt"
        onChange={(e) => setFile(e.target.files?.[0] || null)}
        className="text-sm"
      />
      <button
        onClick={submit}
        disabled={!file || mutation.isPending}
        className="px-4 py-2 rounded bg-cyan-500 text-slate-900 font-semibold disabled:opacity-50"
      >
        {mutation.isPending ? 'Uploading...' : 'Upload'}
      </button>
      {mutation.error && <div className="text-rose-400 text-sm">{mutation.error.message}</div>}
    </div>
  );
}
