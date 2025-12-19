interface Props {
  title: string;
  value: number;
}

export default function ScoreCard({ title, value }: Props) {
  return (
    <div className="card">
      <div className="text-slate-400 text-sm">{title}</div>
      <div className="text-3xl font-bold text-cyan-300">{Math.round(value)}</div>
    </div>
  );
}
