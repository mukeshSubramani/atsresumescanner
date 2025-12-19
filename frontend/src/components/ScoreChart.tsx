import { PieChart, Pie, Cell, ResponsiveContainer } from 'recharts';

export default function ScoreChart({ value }: { value: number }) {
  const data = [
    { name: 'Score', value },
    { name: 'Remaining', value: Math.max(0, 100 - value) },
  ];
  const colors = ['#06b6d4', '#1e293b'];
  return (
    <div className="card">
      <div className="font-semibold mb-2">Overall Score</div>
      <div className="h-48">
        <ResponsiveContainer>
          <PieChart>
            <Pie data={data} dataKey="value" innerRadius={60} outerRadius={80} startAngle={90} endAngle={-270}>
              {data.map((_, idx) => (
                <Cell key={idx} fill={colors[idx % colors.length]} />
              ))}
            </Pie>
          </PieChart>
        </ResponsiveContainer>
        <div className="text-center text-3xl font-bold text-cyan-300 -mt-32">{Math.round(value)}</div>
      </div>
    </div>
  );
}
