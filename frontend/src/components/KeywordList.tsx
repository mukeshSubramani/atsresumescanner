interface Props {
  title: string;
  items: string[];
}

export default function KeywordList({ title, items }: Props) {
  return (
    <div className="card">
      <div className="font-semibold mb-2">{title}</div>
      <div className="flex flex-wrap gap-2 text-sm">
        {items.map((i) => (
          <span key={i} className="px-2 py-1 rounded bg-slate-800 border border-slate-700">
            {i}
          </span>
        ))}
        {items.length === 0 && <div className="text-slate-500">None</div>}
      </div>
    </div>
  );
}
