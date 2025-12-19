interface Suggestion {
  message: string;
  severity: 'INFO' | 'WARN' | 'CRITICAL';
}

export default function SuggestionsPanel({ suggestions }: { suggestions: Suggestion[] }) {
  const colorMap = {
    INFO: 'bg-slate-800 text-slate-200',
    WARN: 'bg-amber-900 text-amber-100',
    CRITICAL: 'bg-rose-900 text-rose-100',
  } as const;
  return (
    <div className="card space-y-2">
      <div className="font-semibold">Suggestions</div>
      {suggestions.map((s, idx) => (
        <div key={idx} className={`px-3 py-2 rounded ${colorMap[s.severity]}`}>
          <span className="text-xs mr-2 opacity-70">{s.severity}</span>
          {s.message}
        </div>
      ))}
      {suggestions.length === 0 && <div className="text-slate-500">No suggestions</div>}
    </div>
  );
}
