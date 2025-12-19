import { useForm } from 'react-hook-form';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useRegister } from '../api/hooks';
import { useAuth } from '../context/AuthContext';
import { useNavigate, Link } from 'react-router-dom';
import Navbar from '../components/Navbar';

const schema = z.object({ email: z.string().email(), password: z.string().min(8) });

type FormData = z.infer<typeof schema>;

export default function Signup() {
  const { register, handleSubmit, formState } = useForm<FormData>({ resolver: zodResolver(schema) });
  const mutation = useRegister();
  const { setAuth } = useAuth();
  const navigate = useNavigate();

  const onSubmit = async (values: FormData) => {
    const res = await mutation.mutateAsync(values);
    setAuth(res.token, res.role);
    navigate('/dashboard');
  };

  return (
    <div>
      <Navbar />
      <div className="max-w-md mx-auto mt-16 card space-y-4">
        <h2 className="text-2xl font-semibold">Create account</h2>
        <form className="space-y-3" onSubmit={handleSubmit(onSubmit)}>
          <input {...register('email')} placeholder="Email" className="w-full bg-slate-800 px-3 py-2 rounded" />
          <input
            {...register('password')}
            type="password"
            placeholder="Password (min 8 chars)"
            className="w-full bg-slate-800 px-3 py-2 rounded"
          />
          {formState.errors.email && <div className="text-rose-400 text-sm">{formState.errors.email.message}</div>}
          {formState.errors.password && <div className="text-rose-400 text-sm">{formState.errors.password.message}</div>}
          <button className="w-full py-2 bg-cyan-500 text-slate-900 font-semibold rounded" disabled={mutation.isPending}>
            {mutation.isPending ? 'Creating...' : 'Sign up'}
          </button>
        </form>
        {mutation.error && <div className="text-rose-400 text-sm">{mutation.error.message}</div>}
        <div className="text-sm text-slate-400">
          Already have an account? <Link to="/login" className="text-cyan-300">Login</Link>
        </div>
      </div>
    </div>
  );
}
