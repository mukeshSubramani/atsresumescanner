import Navbar from '../components/Navbar';
import { useAuth } from '../context/AuthContext';

export default function Settings() {
  const { role, token } = useAuth();
  return (
    <div>
      <Navbar />
      <div className="max-w-3xl mx-auto px-6 py-10 space-y-4">
        <h2 className="text-2xl font-semibold">Settings</h2>
        <div className="card space-y-2 text-sm text-slate-300">
          <div>Role: {role}</div>
          <div>Token present: {token ? 'Yes' : 'No'}</div>
          <div className="text-slate-400">Future: profile, notification settings, feature flags.</div>
        </div>
      </div>
    </div>
  );
}
