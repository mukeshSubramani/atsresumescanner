import { useMarketRoles } from '../api/hooks';

interface Props {
  onSelect: (roleKey: string) => void;
}

export default function MarketSelector({ onSelect }: Props) {
  const { data: roles } = useMarketRoles();
  return (
    <div className="card">
      <div className="font-semibold mb-2">Role Template</div>
      <select
        onChange={(e) => onSelect(e.target.value)}
        className="w-full bg-slate-800 border border-slate-700 rounded px-3 py-2"
      >
        <option value="">Select role</option>
        {roles?.map((role) => (
          <option key={role.key} value={role.key}>
            {role.title} ({role.seniority}) - {role.location}
          </option>
        ))}
      </select>
    </div>
  );
}
