import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { token, logout } = useAuth();
  return (
    <nav className="flex items-center justify-between px-6 py-4 border-b border-slate-800 bg-slate-900/60 backdrop-blur">
      <Link to="/" className="text-xl font-semibold text-cyan-300">
        ATS Resume Scanner
      </Link>
      <div className="flex items-center gap-4 text-sm">
        {token && (
          <>
            <Link to="/dashboard" className="hover:text-cyan-300">
              Dashboard
            </Link>
            <Link to="/scan" className="hover:text-cyan-300">
              New Scan
            </Link>
            <Link to="/market-trend" className="hover:text-cyan-300">
              Market Trends
            </Link>
            <Link to="/settings" className="hover:text-cyan-300">
              Settings
            </Link>
            <button onClick={logout} className="px-3 py-1 rounded bg-cyan-500 text-slate-900 font-semibold">
              Logout
            </button>
          </>
        )}
        {!token && (
          <>
            <Link to="/login" className="hover:text-cyan-300">
              Login
            </Link>
            <Link to="/signup" className="hover:text-cyan-300">
              Sign Up
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}
